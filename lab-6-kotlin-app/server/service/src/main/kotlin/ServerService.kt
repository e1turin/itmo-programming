import com.github.e1turin.application.MusicBand
import com.github.e1turin.application.MusicBandStorage
import com.github.e1turin.protocol.api.*
import com.github.e1turin.util.IOStream
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.lang.Thread.sleep
import java.net.SocketAddress

class ServerService(
    val stdio: IOStream, val localPort: Int
) {
    private var WORK = true
    private val ldpServer = LdpServer.newBuilder().localPort(localPort).build()
    private val storage = MusicBandStorage()
    private val clients = mutableSetOf<SocketAddress>()

    fun start(args: Array<String>) {
        ldpServer.start()
        loop()

    }

    private fun loop() {
        while (true) {
            stdio.writeln("Is waiting request...")
            val (request, address) = ldpServer.receiveRequest() ?: continue
            val response = handleRequest(request, address)
            ldpServer.send(response, address)
            sleep(1000)
        }
    }

    private fun handleRequest(request: LdpRequest, address: SocketAddress): LdpResponse {
        when (request.method) {
            LdpRequest.METHOD.GET -> {
                when (request.headers[LdpHeaders.Headers.CMD_NAME]) {
                    LdpHeaders.Values.Cmd.get -> {
                        TODO("TODO: get cmd get handler")
                    }
                    LdpHeaders.Values.Cmd.connect -> {
                        clients.add(address)
                        println(clients)
                        return LdpResponse(
                            LdpOptions.StatusCode.OK,
                            body = "Client connected"
                        )
                    }
                    LdpHeaders.Values.Cmd.disconnect -> {
                        clients.remove(address)
                        println(clients)
                        return LdpResponse(
                            LdpOptions.StatusCode.OK,
                            body = "Client disconnected"
                        )
                    }
                    else -> {
                        TODO("TODO: get cmd else handler")
                    }
                }

            }
            LdpRequest.METHOD.POST -> {
                when (request.headers[LdpHeaders.Headers.CMD_NAME]) {
                    LdpHeaders.Values.Cmd.add -> {
                        val obj: MusicBand
                        try {
                            obj = Json.decodeFromString(
                                request.headers[LdpHeaders.Headers.Args.FIRST_ARG] ?:
                                throw NoSuchElementException("Method POST:cmd:add has no argument")
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
                            else -> { //"none"
                                return try {
                                    storage.add(obj)
                                    println(storage.toList())
                                    LdpResponse(
                                        LdpOptions.StatusCode.OK,
                                        body = "Object successfully added to collection"
                                    )
                                } catch (e: Exception) {
                                    LdpResponse(
                                        LdpOptions.StatusCode.FAIL,
                                        body = "Problem with appending an object to a collection, " +
                                                "command failed (${e.message})"
                                    )

                                }

                            }

                        }

                    }
                    LdpHeaders.Values.Cmd.clear -> {
                        TODO("TODO: post cmd clear handler")
                    }
                    LdpHeaders.Values.Cmd.update -> {
                        TODO("TODO: post cmd update handler")
                    }
                    else -> {
                        return LdpResponse(
                            LdpOptions.StatusCode.FAIL, body = "Got unknown command for method GET"
                        )
                    }
                }
            }
            else -> {
                TODO("TODO: different method handler")
            }
        }
    }

}
