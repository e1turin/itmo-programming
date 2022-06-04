import com.github.e1turin.app.*
import com.github.e1turin.app.MusicBandDatabaseManager
import com.github.e1turin.app.MusicBandStorage
import com.github.e1turin.protocol.api.*
import com.github.e1turin.util.IOStream
import com.github.e1turin.util.sha256
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.io.IOException
import java.net.SocketAddress
import kotlin.text.toIntOrNull

class ServerService(
    val stdio: IOStream, val localPort: Int, storageName: String
) {
    private var WORK = true
    private val ldpServer = LdpServer.newBuilder().localPort(localPort).build()
    private val storageManager = MusicBandDatabaseManager(MusicBandStorage(storageName))

    private val clients = mutableSetOf<SocketAddress>()

    fun start(args: Array<String>) {
        ldpServer.use {
            start(args)
            loop()
        }
    }

    fun stop() {
        WORK = false
        storageManager.saveDataTo(File(storageManager.storeName))
    }

    private fun loop() {
        while (WORK) {
            stdio.writeln("Is waiting request...")
            val (request, address) = ldpServer.receiveRequest() ?: continue
            val response: LdpResponse = try {
                handleRequest(request, address)
            } catch (e: Exception) {
                LdpResponse(
                    LdpOptions.StatusCode.FAIL, body = "Error while handling request: ${e.message}"
                )
            }
            ldpServer.send(response, address)
//            sleep(1000)
        }
    }

    private fun handleRequest(request: LdpRequest, address: SocketAddress): LdpResponse {
        return when (request.method) {
            LdpRequest.METHOD.GET -> handleRequestMethodGet(request, address)
            LdpRequest.METHOD.POST -> handleRequestMethodPost(request, address)
            else -> {
                LdpResponse(
                    LdpOptions.StatusCode.FAIL, body = "Wrong method used"
                )
            }
        }
    }

    private fun handleRequestMethodPost(request: LdpRequest, address: SocketAddress): LdpResponse {
        val login = request.headers[LdpHeaders.Headers.USER] ?: return LdpResponse(
            LdpOptions.StatusCode.FAIL, body = "Not authorised user"
        )
        val password = request.headers[LdpHeaders.Headers.PASSWD]?.sha256() ?: return LdpResponse(
            LdpOptions.StatusCode.FAIL, body = "Password is required but got no such one"
        )
        val isValidUser = try {
            storageManager.checkPassword(login, password)
        } catch (e: Exception) {
            return LdpResponse(
                LdpOptions.StatusCode.FAIL, body = "Error in authentication (${e.message})"
            )
        }
        if (!isValidUser) return LdpResponse(
            LdpOptions.StatusCode.FAIL, body = "Wrong password"
        )
        return when (request.headers[LdpHeaders.Headers.CMD_NAME]) {
            LdpHeaders.Values.Cmd.add -> handleRequestMethodPostCmdAdd(request)

            LdpHeaders.Values.Cmd.remove -> handleRequestMethodPostCmdRemove(request)

            LdpHeaders.Values.Cmd.update -> handleRequestMethodPostCmdUpdate(request)

            LdpHeaders.Values.Cmd.auth -> {
                return try {
                    storageManager.authoriseNewUser(login, password)
                    LdpResponse(LdpOptions.StatusCode.OK, body = "New user is authorised")
                } catch (e: Exception) {
                    LdpResponse(LdpOptions.StatusCode.FAIL, body = "User is not authorised")
                }
            }

            else -> {
                LdpResponse(
                    LdpOptions.StatusCode.FAIL, body = "Got wrong command for method POST"
                )
            }
        }
    }

    private fun handleRequestMethodPostCmdUpdate(request: LdpRequest): LdpResponse {
        val login = request.headers[LdpHeaders.Headers.USER]!!
        when (request.headers[LdpHeaders.Headers.CONDITION]) {
            LdpHeaders.Values.Condition.property -> {
                when (request.headers[LdpHeaders.Headers.Condition.property]) {
                    "id" -> when (request.headers[LdpHeaders.Headers.SECOND_CONDITION]) {
                        LdpHeaders.Values.Condition.equals -> {
                            try {
                                val id = request.headers["id"]?.toInt() ?: throw IOException(
                                    "Request asks object via id but header named 'id' was not found "
                                )
                                val obj: MusicBand
                                try {
                                    obj = Json.decodeFromString(
                                        request.headers[LdpHeaders.Headers.Args.FIRST_ARG]
                                            ?: throw NoSuchElementException(
                                                "Method POST:cmd:update has no argument"
                                            )
                                    )
                                } catch (e: Exception) {
                                    return LdpResponse(
                                        LdpOptions.StatusCode.FAIL,
                                        body = "Problem with decoding an object (${e.message})"
                                    )
                                }
                                return if (storageManager.hasPermissionOn(id, login)) {
                                    storageManager.updateWithId(id, obj)
                                    LdpResponse(
                                        LdpOptions.StatusCode.OK,
                                        body = "Successfully updated object"
                                    )
                                } else {
                                    LdpResponse(
                                        LdpOptions.StatusCode.FAIL,
                                        body = "User has not enough permissions"
                                    )
                                }
                            } catch (e: Exception) {
                                return LdpResponse(
                                    LdpOptions.StatusCode.FAIL,
                                    body = "Problem with decoding objects' id (${e.message})"
                                )
                            }
                        }
                        else -> return LdpResponse(
                            LdpOptions.StatusCode.FAIL,
                            body = "Not implemented method:get condition:property:... request"
                        )
                    }
                    else -> return LdpResponse(
                        LdpOptions.StatusCode.FAIL,
                        body = "Not implemented method:post:update condition:property request"
                    )
                }
            }
            else -> TODO()
        }
    }

    private fun handleRequestMethodPostCmdRemove(request: LdpRequest): LdpResponse {
        val login = request.headers[LdpHeaders.Headers.USER]!!
        when (request.headers[LdpHeaders.Headers.CONDITION]) {
            LdpHeaders.Values.Condition.property -> {
                when (request.headers[LdpHeaders.Headers.Condition.property]) {
                    "id" -> when (request.headers[LdpHeaders.Headers.SECOND_CONDITION]) {
                        LdpHeaders.Values.Condition.equals -> {
                            try {
                                val id = request.headers["id"]?.toInt() ?: throw IOException(
                                    "Request asks object via id but header named 'id' was not found "
                                )
                                return if (storageManager.hasPermissionOn(id, login)) {
                                    storageManager.removeWithId(id)
                                    LdpResponse(
                                        LdpOptions.StatusCode.OK,
                                        body = "Successfully removed object"
                                    )
                                } else {
                                    LdpResponse(
                                        LdpOptions.StatusCode.FAIL,
                                        body = "User has not enough permissions"
                                    )
                                }
                            } catch (e: Exception) {
                                return LdpResponse(
                                    LdpOptions.StatusCode.FAIL,
                                    body = "Problem with decoding objects' id (${e.message})"
                                )
                            }
                        }
                        else -> return LdpResponse(
                            LdpOptions.StatusCode.FAIL,
                            body = "Not implemented method:get condition:property:... request"
                        )
                    }
                    else -> return LdpResponse(
                        LdpOptions.StatusCode.FAIL,
                        body = "Not implemented method:get condition:property request"
                    )
                }
            }
            LdpHeaders.Values.Condition.none -> when (LdpHeaders.Headers.Condition.none) {
                LdpHeaders.Values.Condition.greater -> TODO("TODO: post cmd:clear cond:none:greater handler")
                LdpHeaders.Values.Condition.less -> TODO("TODO: post cmd:clear cond:none:less handler")
                LdpHeaders.Values.Condition.equals -> {
                    val obj: MusicBand
                    try {
                        obj = Json.decodeFromString(
                            request.headers[LdpHeaders.Headers.Args.FIRST_ARG]
                                ?: throw NoSuchElementException("Method POST:cmd:add has no argument")
                        )
                    } catch (e: Exception) {
                        return LdpResponse(
                            LdpOptions.StatusCode.FAIL,
                            body = "Problem with decoding a collection object (${e.message})"
                        )
                    }
                    val id = storageManager.find { it == obj }?.id ?: return LdpResponse(
                        LdpOptions.StatusCode.FAIL, body = "Equal object was not found"
                    )
                    if (storageManager.hasPermissionOn(id, login)) {
                        return try {
                            storageManager.removeWithId(id)
                            LdpResponse(
                                LdpOptions.StatusCode.OK, body = "Successfully removed object"
                            )
                        } catch (e: Exception) {
                            LdpResponse(
                                LdpOptions.StatusCode.FAIL,
                                body = "Equal object was not removed ($e)"
                            )
                        }
                    } else {
                        return LdpResponse(
                            LdpOptions.StatusCode.FAIL, body = "User has not enough permissions"
                        )
                    }
                }
                LdpHeaders.Values.Condition.amount -> {
                    val amountStr: String =
                        request.headers[LdpHeaders.Headers.Args.FIRST_ARG] ?: return LdpResponse(
                            LdpOptions.StatusCode.FAIL, body = "Problem with identifying amount"
                        )
                    if (amountStr == LdpHeaders.Values.Condition.Args.all) {
                        storageManager.clearStore()
                        return LdpResponse(
                            LdpOptions.StatusCode.OK, body = "Collection successfully cleared"
                        )
                    }
                    val amount = amountStr.trim().toIntOrNull()
                    TODO("TODO: post cmd:clear cond:none:amount Int handler")
                }
                else -> return LdpResponse(
                    LdpOptions.StatusCode.FAIL,
                    body = "Not implemented method:get condition:none request"
                )

            }
            else -> TODO("TODO: post cmd:clear cond handler")
        }
    }

    private fun handleRequestMethodPostCmdAdd(request: LdpRequest): LdpResponse {
        val login = request.headers[LdpHeaders.Headers.USER]!!
        val obj: MusicBand
        try {
            obj = Json.decodeFromString(
                request.headers[LdpHeaders.Headers.Args.FIRST_ARG]
                    ?: throw NoSuchElementException("Method POST:cmd:add has no argument")
            )
        } catch (e: Exception) {
            return LdpResponse(
                LdpOptions.StatusCode.FAIL,
                body = "Problem with decoding a collection object (${e.message})"
            )
        }
        when (request.headers[LdpHeaders.Headers.CONDITION]) {
            LdpHeaders.Values.Condition.greater -> TODO("TODO: post cmd:add cond:greater handler")
            LdpHeaders.Values.Condition.less -> TODO("TODO: post cmd:add cond:less handler")
            LdpHeaders.Values.Condition.equals -> TODO("TODO: post cmd:add cond:equals handler")
            else -> {
                return try {
                    storageManager.addElementWithOwner(obj, login)
                    println(storageManager.dataAsList)
                    LdpResponse(
                        LdpOptions.StatusCode.OK, body = "Object successfully added to collection"
                    )
                } catch (e: Exception) {
                    LdpResponse(
                        LdpOptions.StatusCode.FAIL,
                        body = "Problem with appending an object to a collection, " + "command failed (${e.message})"
                    )
                }
            }
        }
    }

    private fun handleRequestMethodGet(request: LdpRequest, address: SocketAddress): LdpResponse {
        when (request.headers[LdpHeaders.Headers.CMD_NAME]) {
            LdpHeaders.Values.Cmd.get -> {
                when (request.headers[LdpHeaders.Headers.CONDITION]) {

                    LdpHeaders.Values.Condition.equals -> when (request.headers[LdpHeaders.Headers.Args.FIRST_ARG]) {
                        //returns lists as DATA
                        "id" -> try {
                            val id = request.headers["id"]?.toInt() ?: throw IOException(
                                "Request asks object via id but header named 'id' was not" + " found "
                            )
                            val obj = storageManager.find { it.id == id } ?: throw IOException(
                                "Request asks object via id but header named 'id' was not " + "found "
                            )
                            return LdpResponse(
                                LdpOptions.StatusCode.OK, LdpHeaders().add(
                                    LdpHeaders.Headers.DATA, Json.encodeToString(listOf(obj))
                                )
                            )
                        } catch (e: Exception) {
                            return LdpResponse(
                                LdpOptions.StatusCode.FAIL,
                                body = "Problem with decoding objects' id (${e.message})"
                            )
                        }
                        "name" -> try {
                            val name = request.headers["name"] ?: throw IOException(
                                "Request asks via name but header named 'name' was not found"
                            )
                            val objs =
                                storageManager.filter { it.name == name } ?: throw IOException(
                                    "Request asks object via id but header named 'id' was not found"
                                )
                            return LdpResponse(
                                LdpOptions.StatusCode.OK, LdpHeaders().add(
                                    LdpHeaders.Headers.DATA, Json.encodeToString(objs)
                                )
                            )
                        } catch (e: Exception) {
                            return LdpResponse(
                                LdpOptions.StatusCode.FAIL,
                                body = "Problem with decoding objects' name (${e.message})"
                            )
                        }
                        "genre" -> try {
                            val genre = request.headers["genre"] ?: throw IOException(
                                "Request asks via ganre but header named 'genre' was not found"
                            )
                            val objs = storageManager.find {
                                it.genre == MusicGenre.valueOf(genre.uppercase())
                            } ?: throw IOException(
                                "Request asks object via id but header named 'id' was not found"
                            )
                            return LdpResponse(
                                LdpOptions.StatusCode.OK, LdpHeaders().add(
                                    LdpHeaders.Headers.DATA, Json.encodeToString(objs)
                                )
                            )
                        } catch (e: Exception) {
                            return LdpResponse(
                                LdpOptions.StatusCode.FAIL,
                                body = "Problem with decoding objects' genre (${e.message})"
                            )
                        }
                        else -> return LdpResponse(
                            LdpOptions.StatusCode.FAIL,
                            body = "Request handler for get:equals with such argument is not " + "implemented"
                        )
                    }

                    LdpHeaders.Values.Condition.greater -> when (request.headers[LdpHeaders.Headers.Args.FIRST_ARG]) {
                        //returns lists as DATA
                        "id" -> try {
                            val id = request.headers["id"]?.toInt() ?: throw IOException(
                                "Request asks object via id but header named 'id' was not" + " found "
                            )
                            val obj = storageManager.find { it.id > id } ?: throw IOException(
                                "Request asks object via id but header named 'id' was not " + "found "
                            )
                            return LdpResponse(
                                LdpOptions.StatusCode.OK, LdpHeaders().add(
                                    LdpHeaders.Headers.DATA, Json.encodeToString(listOf(obj))
                                )
                            )
                        } catch (e: Exception) {
                            return LdpResponse(
                                LdpOptions.StatusCode.FAIL,
                                body = "Problem with decoding objects' id (${e.message})"
                            )
                        }
                        "name" -> try {
                            val name = request.headers["name"] ?: throw IOException(
                                "Request asks via name but header named 'name' was not found"
                            )
                            val objs =
                                storageManager.filter { it.name > name } ?: throw IOException(
                                    "Request asks object via id but header named 'id' was not found"
                                )
                            return LdpResponse(
                                LdpOptions.StatusCode.OK, LdpHeaders().add(
                                    LdpHeaders.Headers.DATA, Json.encodeToString(objs)
                                )
                            )
                        } catch (e: Exception) {
                            return LdpResponse(
                                LdpOptions.StatusCode.FAIL,
                                body = "Problem with decoding objects' name (${e.message})"
                            )
                        }
                        "genre" -> try {
                            val genre = request.headers["genre"] ?: throw IOException(
                                "Request asks via ganre but header named 'genre' was not found"
                            )
                            val objs = storageManager.find {
                                it.genre > MusicGenre.valueOf(genre.uppercase())
                            } ?: throw IOException(
                                "Request asks object via id but header named 'id' was not found"
                            )
                            return LdpResponse(
                                LdpOptions.StatusCode.OK, LdpHeaders().add(
                                    LdpHeaders.Headers.DATA, Json.encodeToString(objs)
                                )
                            )
                        } catch (e: Exception) {
                            return LdpResponse(
                                LdpOptions.StatusCode.FAIL,
                                body = "Problem with decoding objects' genre (${e.message})"
                            )
                        }
                        else -> return LdpResponse(
                            LdpOptions.StatusCode.FAIL,
                            body = "Request handler for get:greater with such argument is not implemented"
                        )
                    }

                    LdpHeaders.Values.Condition.less -> when (request.headers[LdpHeaders.Headers.Args.FIRST_ARG]) {
                        //returns lists as DATA
                        "id" -> try {
                            val id = request.headers["id"]?.toInt() ?: throw IOException(
                                "Request asks object via id but header named 'id' was not" + " found "
                            )
                            val obj = storageManager.find { it.id < id } ?: throw IOException(
                                "Request asks object via id but header named 'id' was not " + "found "
                            )
                            return LdpResponse(
                                LdpOptions.StatusCode.OK, LdpHeaders().add(
                                    LdpHeaders.Headers.DATA, Json.encodeToString(listOf(obj))
                                )
                            )
                        } catch (e: Exception) {
                            return LdpResponse(
                                LdpOptions.StatusCode.FAIL,
                                body = "Problem with decoding objects' id (${e.message})"
                            )
                        }
                        "name" -> try {
                            val name = request.headers["name"] ?: throw IOException(
                                "Request asks via name but header named 'name' was not found"
                            )
                            val objs =
                                storageManager.filter { it.name < name } ?: throw IOException(
                                    "Request asks object via id but header named 'id' was not found"
                                )
                            return LdpResponse(
                                LdpOptions.StatusCode.OK, LdpHeaders().add(
                                    LdpHeaders.Headers.DATA, Json.encodeToString(objs)
                                )
                            )
                        } catch (e: Exception) {
                            return LdpResponse(
                                LdpOptions.StatusCode.FAIL,
                                body = "Problem with decoding objects' name (${e.message})"
                            )
                        }
                        "genre" -> try {
                            val genre = request.headers["genre"] ?: throw IOException(
                                "Request asks via ganre but header named 'genre' was not found"
                            )
                            val objs = storageManager.find {
                                it.genre < MusicGenre.valueOf(genre.uppercase())
                            } ?: throw IOException(
                                "Request asks object via id but header named 'id' was not found"
                            )
                            return LdpResponse(
                                LdpOptions.StatusCode.OK, LdpHeaders().add(
                                    LdpHeaders.Headers.DATA, Json.encodeToString(objs)
                                )
                            )
                        } catch (e: Exception) {
                            return LdpResponse(
                                LdpOptions.StatusCode.FAIL,
                                body = "Problem with decoding objects' genre (${e.message})"
                            )
                        }
                        else -> return LdpResponse(
                            LdpOptions.StatusCode.FAIL,
                            body = "Request handler for get:less with such argument is not " + "implemented"
                        )
                    }

                    LdpHeaders.Values.Condition.amount -> when (request.headers[LdpHeaders.Headers.Args.FIRST_ARG]) {
                        LdpHeaders.Values.Condition.Args.all -> return LdpResponse(
                            LdpOptions.StatusCode.OK, headers = LdpHeaders().add(
                                LdpHeaders.Headers.DATA,
                                Json.encodeToString(storageManager.dataAsList)
                            )
                        )
                        else -> {
                            try {
                                val str = request.headers[LdpHeaders.Headers.Args.FIRST_ARG]
                                val amount = str?.toInt() ?: throw IOException(
                                    "Request asks amount of object but header " + "stood for it was not found (amount='$str') "
                                )
                                val objs = storageManager.slice(0 until amount)
                                return LdpResponse(
                                    LdpOptions.StatusCode.OK, LdpHeaders().add(
                                        LdpHeaders.Headers.DATA, Json.encodeToString(objs)
                                    )
                                )
                            } catch (e: Exception) {
                                return LdpResponse(
                                    LdpOptions.StatusCode.FAIL,
                                    body = "Problem with decoding amount (${e.message})"
                                )
                            }
                        }
                    }

                    LdpHeaders.Values.Condition.range -> {
                        var begin =
                            request.headers[LdpHeaders.Headers.Condition.begin]?.toInt() ?: 0
                        var end = request.headers[LdpHeaders.Headers.Condition.end]?.toInt()
                            ?: (storageManager.storeSize - 1)
                        if (begin < 0 || begin >= storageManager.storeSize) begin = 0
                        if (end < 0 || end >= storageManager.storeSize) end =
                            (storageManager.storeSize - 1)
                        val objs = storageManager.slice(begin..end)
                        return LdpResponse(
                            LdpOptions.StatusCode.OK, LdpHeaders().add(
                                LdpHeaders.Headers.DATA,
                                Json.encodeToString(objs),
                            ), body = "Elements with indices in range [$begin .. $end]"
                        )
                    }

                    else -> return LdpResponse(
                        LdpOptions.StatusCode.FAIL,
                        body = "Got wrong condition for method GET, command get:range"
                    )
                }
            }

            LdpHeaders.Values.Cmd.connect -> {
                clients.add(address)
                println(clients)
                return LdpResponse(
                    LdpOptions.StatusCode.OK, body = "Client connected"
                )
            }

            LdpHeaders.Values.Cmd.disconnect -> {
                clients.remove(address)
                println(clients)
                return LdpResponse(
                    LdpOptions.StatusCode.OK, body = "Client disconnected"
                )
            }
            LdpHeaders.Values.Cmd.auth -> {
                val login = request.headers[LdpHeaders.Headers.USER] ?: return LdpResponse(
                    LdpOptions.StatusCode.FAIL, body = "Not authorised user"
                )
                val password =
                    request.headers[LdpHeaders.Headers.PASSWD]?.sha256() ?: return LdpResponse(
                        LdpOptions.StatusCode.FAIL,
                        body = "Password is required but got no such one"
                    )
                return try {
                    if (storageManager.checkPassword(login, password)) {
                        LdpResponse(LdpOptions.StatusCode.OK, body = "User is authorised")
                    } else {
                        LdpResponse(LdpOptions.StatusCode.FAIL, body = "Wrong password")
                    }
                } catch (e: Exception) {
                    LdpResponse(LdpOptions.StatusCode.FAIL, body = "Wrong login or password")
                }
            }
            else -> return LdpResponse(
                LdpOptions.StatusCode.FAIL, body = "Got wrong command for method GET"
            )
        }
    }
}