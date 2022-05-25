import com.github.e1turin.application.MusicBand
import com.github.e1turin.application.MusicBandStorage
import com.github.e1turin.application.MusicBandStorageManager
import com.github.e1turin.protocol.api.*
import com.github.e1turin.util.IOStream
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.lang.Thread.sleep
import java.net.SocketAddress
import kotlin.text.toIntOrNull

class ServerService(
    val stdio: IOStream, val localPort: Int, storageName: String
) {
    private var WORK = true
    private val ldpServer = LdpServer.newBuilder().localPort(localPort).build()
    private val storageManager = MusicBandStorageManager(MusicBandStorage(storageName))
    private val clients = mutableSetOf<SocketAddress>()

    fun start(args: Array<String>) {
        ldpServer.start()
        loop()
    }

    fun stop(){
        WORK = false
        storageManager.saveDataTo(File(storageManager.storageName))
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
            sleep(1000)
        }
//        LdpServer.close()
    }

    private fun handleRequest(request: LdpRequest, address: SocketAddress): LdpResponse {
        when (request.method) {
            LdpRequest.METHOD.GET -> {
                when (request.headers[LdpHeaders.Headers.CMD_NAME]) {
                    LdpHeaders.Values.Cmd.get -> {
                        when (request.headers[LdpHeaders.Headers.CONDITION]) {
                            LdpHeaders.Values.Condition.equals -> {
                                TODO("TODO: GET:get:cond=equals")
                            }
                            LdpHeaders.Values.Condition.greater -> {
                                TODO("TODO: GET:get:cond=greater")
                            }
                            LdpHeaders.Values.Condition.less -> {
                                TODO("TODO: GET:get:cond=less")
                            }
                            LdpHeaders.Values.Condition.amount -> {
                                if (request.headers[LdpHeaders.Headers.Args.FIRST_ARG]
                                    == LdpHeaders.Values.Args.all
                                ) {
                                    return LdpResponse(
                                        LdpOptions.StatusCode.OK, headers = LdpHeaders().add(
                                            LdpHeaders.Headers.DATA,
                                            Json.encodeToString(storageManager.dataAsList)
                                        )
                                    )
                                }
                                TODO("TODO: GET:get:cond=amount")
                            }
                            else -> {
                                return LdpResponse(
                                    LdpOptions.StatusCode.FAIL,
                                    body = "Got wrong condition for method GET, command get"
                                )
                            }
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
                    else -> {
                        return LdpResponse(
                            LdpOptions.StatusCode.FAIL, body = "Got wrong command for method GET"
                        )
                    }
                }

            }
            LdpRequest.METHOD.POST -> {
                when (request.headers[LdpHeaders.Headers.CMD_NAME]) {
                    LdpHeaders.Values.Cmd.add -> {
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
                            LdpHeaders.Values.Condition.greater -> {
                                TODO("TODO: post cmd:add cond:greater handler")
                            }
                            LdpHeaders.Values.Condition.less -> {
                                TODO("TODO: post cmd:add cond:less handler")
                            }
                            LdpHeaders.Values.Condition.equals -> {
                                TODO("TODO: post cmd:add cond:equals handler")
                            }
                            else -> { //todo: "none"
                                return try {
                                    storageManager.addElement(obj)
                                    println(storageManager.dataAsList)
                                    LdpResponse(
                                        LdpOptions.StatusCode.OK,
                                        body = "Object successfully added to collection"
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
                    LdpHeaders.Values.Cmd.clear -> {
                        when (request.headers[LdpHeaders.Headers.CONDITION]) {
                            LdpHeaders.Values.Condition.greater -> {
                                TODO("TODO: post cmd:clear cond:greater handler")
                            }
                            LdpHeaders.Values.Condition.less -> {
                                TODO("TODO: post cmd:clear cond:less handler")
                            }
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
                                storageManager.removeIf { it == obj }
                                return LdpResponse(
                                    LdpOptions.StatusCode.OK,
                                    body = "Successfully removed"
                                )
                            }
                            LdpHeaders.Values.Condition.amount -> {
                                val amountStr: String =
                                    request.headers[LdpHeaders.Headers.Args.FIRST_ARG]
                                        ?: return LdpResponse(
                                            LdpOptions.StatusCode.FAIL,
                                            body = "Problem with identifying amount"
                                        )
                                if (amountStr == LdpHeaders.Values.Args.all) {
                                    storageManager.clearStore()
                                    return LdpResponse(
                                        LdpOptions.StatusCode.OK,
                                        body = "Collection successfully cleared"
                                    )
                                }
                                val amount = amountStr.trim().toIntOrNull()
                                TODO("TODO: post cmd:clear cond:amount Int handler")
                            }
                            else -> {
                                //todo: "none"
                                TODO("TODO: post cmd:clear cond:equals handler")
                            }
                        }
                    }
                    LdpHeaders.Values.Cmd.update -> {
                        TODO("TODO: post cmd update handler")
                    }
                    else -> {
                        return LdpResponse(
                            LdpOptions.StatusCode.FAIL, body = "Got wrong command for method POST"
                        )
                    }
                }
            }
            else -> {
                return LdpResponse(
                    LdpOptions.StatusCode.FAIL, body = "Wrong method used"
                )
            }
        }
    }


}
