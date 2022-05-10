package com.github.e1turin.lab5.client

import com.github.e1turin.lab5.common.application.CommandManager
import com.github.e1turin.lab5.common.commands.HelpCmd
import com.github.e1turin.lab5.common.commands.HistoryCmd
import com.github.e1turin.lab5.common.exceptions.CallstackOverflowException
import com.github.e1turin.lab5.common.exceptions.InvalidCmdArgumentException
import com.github.e1turin.lab5.common.exceptions.NonexistentCommandException
import com.github.e1turin.lab5.common.exceptions.RecursiveScriptException
import com.github.e1turin.lab5.common.util.IOStream
import com.github.e1turin.lab5.protocol.LdpClient
import com.github.e1turin.lab5.protocol.LdpMessage
import com.github.e1turin.lab5.protocol.LdpRequest
import com.github.e1turin.lab5.protocol.LdpResponse
import java.io.IOException
import java.net.InetAddress

class Client(
    private var stdIOStream: IOStream,
    private val commandManager: CommandManager //TODO: setup stdOIstream
) {
    private var __WORKING__ = true
    private val uri: String = InetAddress.getLocalHost().hostName//todo: connection()
    private val port = 35047
    private val ldpClient = LdpClient(uri, port)

    object TASKS {
        const val EXIT = "Exit"
        const val HELP = "Help"
        const val HISTORY = "History"
    }

    fun start(args: Array<String>) {
//        val uri: String = InetAddress.getLocalHost().hostName
//        val port = 35047
//        val client = LdpClient(uri, port) //todo: connection()
        //todo: authorisation()


        loop()

        ldpClient.stop()
        stdIOStream.writeln("Работа менеджера завершена")
    }

    private fun loop() {
        while (__WORKING__) {
            val cmdWithArg = commandManager.getValidCmdWithArg(stdIOStream)
            val cmdName = cmdWithArg.first
            val cmdArgs = cmdWithArg.second
            val request: LdpRequest = commandManager.processCmd(cmdName, cmdArgs, stdIOStream)

            var response: LdpResponse? = null
            try {
                response = handleRequest(request)
            } catch (e: IOException) {
                e.message?.let { stdIOStream.writeln(it) }
            }
            response?.let {
                commandManager.giveResponseToCmd(cmdName, it, stdIOStream)
            }
        }
    }

    private fun processLoopExceptions(e: Throwable, ioStream: IOStream) {
        when (e) {
            is InterruptedException -> {
                return
            }
            is RecursiveScriptException -> {
                if (ioStream.interactive) {
                    ioStream.writeln("Обнаружена рекурсия в скрипте. Выполнение прекращено.")
                } else {
                    throw e
                }
            }
            is CallstackOverflowException -> {
                if (ioStream.interactive) {
                    ioStream.writeln(
                        "Произошло слишком много дополнительных вызовов выполнения " + "скрипта (максимум = scriptCallstackLimit). Выполнение " + "остановлено."
                    )
                } else {
                    throw e
                }
            }
            is NonexistentCommandException -> {
                if (ioStream.interactive) {
                    ioStream.writeln("Попытка выполнить несуществующую команду ${e.cmd}")
                } else {
                    throw e
                }
            }
            is InvalidCmdArgumentException -> {
                if (ioStream.interactive) {
                    ioStream.writeln("Не верные значения параметров (${e.arg})")
                } else {
                    throw e
                }
            }

            else -> {
                throw e
            }
        }
    }

    private fun handleRequest(request: LdpRequest): LdpResponse {
        val cmdName = request.getHeader(LdpMessage.HEADERS.CMD_NAME)
        val response: LdpResponse =
            when (cmdName) {
                TASKS.EXIT -> doExit(cmdName)
                TASKS.HELP -> doGiveHelp(cmdName)
                TASKS.HISTORY -> doGiveHistory(cmdName)
                else -> TODO("query to server")
            }

        return response
    }

    private fun doGiveHistory(cmdName: String): LdpResponse {
        var historyString = ""
        for ((index, name) in commandManager.getHistoryList().reversed().withIndex()) {
            historyString += "$index) $name\n"
        }
        val response = LdpResponse(LdpResponse.Status.OK).apply {
            command(TASKS.HISTORY)
            commandArg(HistoryCmd.ARGS.history_list, historyString)
        }
        return response
    }

    private fun doGiveHelp(cmdName: String): LdpResponse {
        var helpString = "Имя команды : описание\n"
        for ((name, cmdHelpString) in commandManager.getHelpMap()) {
            helpString += "$name : $cmdHelpString\n"
        }
        val response = LdpResponse(LdpResponse.Status.OK).apply {
            command(TASKS.HELP)
            commandArg(HelpCmd.ARGS.doc_str, helpString)
        }
        return response
    }

    private fun doExit(cmdName: String): LdpResponse {
        __WORKING__ = false
        //TODO: Log.log
        return LdpResponse(LdpResponse.Status.OK)
    }


}