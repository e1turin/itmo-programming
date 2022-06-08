package com.github.e1turin

import com.github.e1turin.app.CommandManager
import com.github.e1turin.util.Manager
import com.github.e1turin.app.MusicBand
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
    private var username: String? = null
    private var password: String? = null

    fun start(args: Array<String>) {
        cmdManager.attachManager(this)
        cmdManager.loop()
        stop()
    }

    override fun request(method: Method, args: Map<String, Any>): LdpResponse {
        return try {
            when (method) {
                Method.GET -> handleMethodGet(args)
                Method.POST -> handleMethodPost(args)
                Method.CONNECTION -> handleMethodConnect(args)
                Method.AUTH -> handleMethodAuth(args)
                else -> {
                    TODO("Wrong method Is Not Implemented")
                }
            }
        } catch (e: Exception) {
            responseOfHandlingRequestException(e)
        }
    }

    private fun responseOfHandlingRequestException(e: Exception): LdpResponse {
        return when (e) {
            is LdpConnectionException -> {
                LdpResponse(
                    LdpOptions.StatusCode.FAIL, body = "LDP connection error: ${e.message}"
                )
            }
            is SocketTimeoutException -> {
                LdpResponse(
                    LdpOptions.StatusCode.FAIL, body = "Timeout error happened: ${e.message}"
                )
            }
            is AsynchronousCloseException -> {
                LdpResponse(
                    LdpOptions.StatusCode.FAIL,
                    body = "Client was disconnected before: ${e.message}"
                )
            }
            else -> {
                LdpResponse(
                    LdpOptions.StatusCode.FAIL, body = "Error message: ${e.message}"
                )
            }
        }
    }

    private fun handleMethodAuth(args: Map<String, Any>): LdpResponse {
        when (args[Opt.`do`] ?: "No task") {
            Task.Auth.login -> {
                if (this.username != null) return LdpResponse(
                    LdpOptions.StatusCode.FAIL,
                    body = "User already logged in, you should log out before"
                )
                val login = (args[Opt.Auth.login] ?: return LdpResponse(
                    LdpOptions.StatusCode.FAIL, body = "There is no login"
                )) as String
                if (login.isBlank()) return LdpResponse(
                    LdpOptions.StatusCode.FAIL, body = "Login is empty"
                )
                val passwd = (args[Opt.Auth.password] ?: return LdpResponse(
                    LdpOptions.StatusCode.FAIL, body = "There is no password"
                )) as String
                if (passwd.isBlank()) return LdpResponse(
                    LdpOptions.StatusCode.FAIL, body = "Password is empty"
                )
                val request = LdpRequest.newBuilder().method(LdpRequest.METHOD.GET)
                    .header(LdpHeaders.Headers.CMD_NAME, LdpHeaders.Values.Cmd.auth)
                    .header(LdpHeaders.Headers.USER, login)
                    .header(LdpHeaders.Headers.PASSWD, passwd)
                    .build()

                val response = ldpClient.send(request)
                if (response.status == LdpOptions.StatusCode.OK) {
                    ldpClient.setLogin(login)
                    ldpClient.setPassword(passwd)

                    username = login
                    password = passwd
                }
                return response
            }
            Task.Auth.logout -> {
                if (username == null) {
                    return LdpResponse(LdpOptions.StatusCode.FAIL, body = "User is not authorised")
                }
                ldpClient.deleteUser()
                username = null
                password = null
                return LdpResponse(LdpOptions.StatusCode.OK, body = "Successfully logged out")
            }
            Task.Auth.signup -> {
                if (this.username != null) return LdpResponse(
                    LdpOptions.StatusCode.FAIL,
                    body = "User already logged in, you should log out before"
                )
                val login = (args[Opt.Auth.login] ?: return LdpResponse(
                    LdpOptions.StatusCode.FAIL, body = "There is no login"
                )) as String
                val passwd = (args[Opt.Auth.password] ?: return LdpResponse(
                    LdpOptions.StatusCode.FAIL, body = "There is no password"
                )) as String
                val request = LdpRequest.newBuilder().method(LdpRequest.METHOD.POST)
                    .header(LdpHeaders.Headers.CMD_NAME, LdpHeaders.Values.Cmd.auth)
                    .header(LdpHeaders.Headers.USER, login)
                    .header(LdpHeaders.Headers.PASSWD, passwd)
                    .build()

                return ldpClient.send(request)
            }
            Task.Auth.signout -> {
                TODO("TODO: Task.Auth.signout")
            }
            else -> {
                throw IOException("Set wrong task for method AUTH: ${args[Opt.`do`] ?: "No task"} ")
            }
        }

    }

    private fun handleMethodConnect(args: Map<String, Any>): LdpResponse {
        return when (args[Opt.`do`] ?: "No task") {
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
    }

    private fun handleMethodPost(args: Map<String, Any>): LdpResponse {
        return when (args[Opt.`do`] ?: "No task") {
            Task.add -> {
                requestToAdd(args)
            }
            Task.clear -> {
                val id = args[Opt.first_arg] as String
                val request = LdpRequest.newBuilder().method(LdpRequest.METHOD.POST)
                    .header(LdpHeaders.Headers.CMD_NAME, LdpHeaders.Values.Cmd.remove)
                    .header(LdpHeaders.Headers.CONDITION, LdpHeaders.Values.Condition.property)
                    .header(LdpHeaders.Headers.Condition.property, "id")
                    .header(LdpHeaders.Headers.SECOND_CONDITION, LdpHeaders.Values.Condition.equals)
                    .header("id", id).build()

                return ldpClient.send(request)
            }
            Task.update -> {
                TODO("Todo update request")
            }
            else -> {
                throw IOException("Set wrong task for method POST: ${args[Opt.`do`] ?: "no task"}")
            }
        }
    }

    private fun handleMethodGet(args: Map<String, Any>): LdpResponse {
        return when (args[Opt.`do`] ?: "No task") {
            Task.get -> {
                requestToGet(args)
            }
            else -> {
                throw IOException(
                    "Set wrong task for method GET: ${args[Opt.`do`] ?: "no  task"}"
                )
            }
        }
    }


    private fun requestToAdd(args: Map<String, Any>): LdpResponse {
        val obj: MusicBand
        try {
            obj = args[Opt.first_arg] as MusicBand
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
            .header(LdpHeaders.Headers.Args.FIRST_ARG, LdpHeaders.Values.Condition.Args.all).build()

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