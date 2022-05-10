package com.github.e1turin.lab5.common.application

import com.github.e1turin.lab5.common.commands.*
import com.github.e1turin.lab5.common.containers.Message
import com.github.e1turin.lab5.common.exceptions.NonexistentCommandException
import com.github.e1turin.lab5.common.util.IOStream
import com.github.e1turin.lab5.protocol.LdpRequest
import com.github.e1turin.lab5.protocol.LdpResponse
import java.util.HashMap
import java.util.LinkedList

class CommandManager {
    private val commands: MutableMap<String, Command> = HashMap<String, Command>()
    private val history: MutableList<String> = LinkedList()


    constructor(vararg commands: Command) {
        for (command in commands) {
            this.commands[command.cmdName] = command
        }
    }

    private fun attachToHistory(cmdName: String) {
        history.add(cmdName)
        if (history.size > 6) history.removeFirst()
    }

    fun processCmd(cmdName: String, arg: String, ioStream: IOStream): LdpRequest =
        executeCmd(cmdName, arg, ioStream)

    fun getValidCmdWithArg(ioStream: IOStream): Pair<String, String> {
        var cmdName = ""
        var arg = ""
        try {
            val cmdAndArg = ioStream.termInputUntil(sep = ">>>",
                hint = "Ошибка! Такой команды нет, попробуйте снова",
                condition = { it != null && validateCmd(it.first) },
                query = { ioStream.readCmdWithArg() })
                ?: throw NonexistentCommandException("cmdAndArg")
            cmdName = cmdAndArg.first
            arg = cmdAndArg.second
        } catch (e: InterruptedException) {
            ioStream.writeln("<<Прерывание работы>>")
            ioStream.writeln("Для выхода из менеджера используйте команду exit")
            e.message?.let { ioStream.writeln(it) }
        }
        return Pair(cmdName, arg)
    }

    fun validateCmd(cmd: String): Boolean = commands.containsKey(cmd)

    private fun executeCmd(cmdName: String, arg: String, ioStream: IOStream): LdpRequest {
        if (!validateCmd(cmdName)) {
            throw NonexistentCommandException(
                "Attempt to execute nonexistent command"
            )
        }
        attachToHistory(cmdName)
        return commands[cmdName]!!.execute(arg, ioStream)
    }

    fun giveResponseToCmd(
        cmdName: String, response: LdpResponse, ioStream: IOStream
    ): LdpResponse {
        val cmd = commands[cmdName]!!
        return cmd.handleResponse(response, ioStream)
    }

    fun getHistoryList(): List<String> {
        return history
    }

    fun getHelpMap(): Map<String, String> {
        val helpMap: MutableMap<String, String> = hashMapOf()
        for(cmd in commands.values){
            helpMap[cmd.cmdName] = cmd.getDescription()
        }
        return helpMap
    }

}