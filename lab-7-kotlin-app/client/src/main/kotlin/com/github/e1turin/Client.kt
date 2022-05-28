package com.github.e1turin

import com.github.e1turin.application.CommandManager
import com.github.e1turin.util.Manager
import com.github.e1turin.application.MusicBand
import com.github.e1turin.exceptions.InvalidCmdArgumentException
import com.github.e1turin.protocol.api.*
import com.github.e1turin.protocol.exceptions.LdpConnectionException
import com.github.e1turin.util.IOStream
import com.github.e1turin.util.serialize
import java.io.IOException
import java.net.InetAddress
import java.net.SocketTimeoutException
import java.nio.channels.AsynchronousCloseException

class Client(
    override val stdio: IOStream,
    private val cmdManager: CommandManager,
    private val serverPort: Int,
    private val serverAddress: InetAddress,
) : Manager() {
    override var WORK = true
        private set
    private val ldpClient: LdpClient =
        LdpClient.newBuilder().port(serverPort).host(serverAddress).build()

    fun start(args: Array<String>) {
        cmdManager.attachManager(this)
        cmdManager.loop()
        stop()
    }

    override fun request(method: Method, args: Map<String, Any>): LdpResponse {
        try {
            return when (method) {
                Method.GET -> when (args[Opt.`do`] ?: "No task") {
                    Task.get -> {
                        requestToGet(args)
                    }
                    else -> {
                        throw IOException(
                            "Set wrong task for method GET: ${args[Opt.`do`] ?: "no  task"}"
                        )
                    }
                }
                Method.POST -> when (args[Opt.`do`] ?: "No task") {
                    Task.add -> {
                        requestToAdd(args)
                    }
                    Task.clear -> { //todo: rename remove:all
                        TODO("Todo clear request")
                    }
                    Task.update -> {
                        TODO("Todo update request")
                    }
                    else -> {
                        throw IOException("Set wrong task for method POST: ${args[Opt.`do`] ?: "no task"}")
                    }
                }
                Method.CONNECTION -> when (args[Opt.`do`] ?: "No task") {
                    Task.connect -> {
                        try {
                            LdpResponse(ldpClient.connect())
                        } catch (e: LdpConnectionException) {
                            LdpResponse(LdpOptions.StatusCode.FAIL, body = "${e.message}")
                        }
                    }
                    Task.disconnect -> {
                        try {
                            LdpResponse(ldpClient.disconnect())
                        } catch (e: LdpConnectionException) {
                            LdpResponse(LdpOptions.StatusCode.FAIL, body = "${e.message}")
                        }
                    }
                    else -> {
                        throw IOException("Set wrong task for method CONNECT: ${args[Opt.`do`] ?: "No task"} ")
                    }
                }
                else -> {
                    TODO()
                }
            }
        } catch (e: LdpConnectionException) {
            return LdpResponse(
                LdpOptions.StatusCode.FAIL, body = "LDP connection error: ${e.message}"
            )
        } catch (e: SocketTimeoutException) {
            return LdpResponse(
                LdpOptions.StatusCode.FAIL, body = "Timeout error happened: ${e.message}"
            )
        } catch (e: AsynchronousCloseException) {
            return LdpResponse(
                LdpOptions.StatusCode.FAIL, body = "Client was disconnected before: ${e.message}"
            )
        } catch (e: Exception) {
            return LdpResponse(
                LdpOptions.StatusCode.FAIL, body = "Error message: ${e.message}"
            )
        }
    }


    private fun requestToAdd(args: Map<String, Any>): LdpResponse {
        val obj: MusicBand
        try {
            obj = args[Opt.single_arg] as MusicBand
        } catch (e: Exception) {
            throw InvalidCmdArgumentException(
                "Wrong task arguments, expected object of MusicBand class as single arg"
            )
        }
        val request = LdpRequest.newBuilder().method(LdpRequest.METHOD.POST)
            .header(LdpHeaders.Headers.CMD_NAME, LdpHeaders.Values.Cmd.add)
            .header(LdpHeaders.Headers.Args.FIRST_ARG, obj.serialize()).build()

        return ldpClient.send(request)
    }

    private fun requestToGet(args: Map<String, Any>): LdpResponse {
        val request = LdpRequest.newBuilder().method(LdpRequest.METHOD.GET)
            .header(LdpHeaders.Headers.CMD_NAME, LdpHeaders.Values.Cmd.get)
            .header(LdpHeaders.Headers.CONDITION, LdpHeaders.Values.Condition.amount)
            .header(LdpHeaders.Headers.Args.FIRST_ARG, LdpHeaders.Values.Args.all).build()

        return ldpClient.send(request)

    }


    override fun stop() {
        WORK = false
        if (ldpClient.isOpen) {
            ldpClient.close()
        }
        if (this.cmdManager.WORK) {
            this.cmdManager.stop()
        }
        stdio.writeln("Client application has finished working")
    }

}