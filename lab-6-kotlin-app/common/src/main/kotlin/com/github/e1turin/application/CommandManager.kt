package com.github.e1turin.application

import com.github.e1turin.commands.*
import com.github.e1turin.exceptions.NonexistentCommandException
import com.github.e1turin.protocol.api.LdpResponse
import com.github.e1turin.util.IOStream
import com.github.e1turin.util.Manager
import java.util.HashMap
import java.util.LinkedList

class CommandManager : Manager {
    private val commands: MutableMap<String, Command> = HashMap<String, Command>()
    private val history: MutableList<String> = LinkedList()
    override lateinit var stdio: IOStream
        private set
    private lateinit var client: Manager
    override var WORK = true
        private set


    constructor(vararg commands: Command) {
        for (command in commands) {
            this.commands[command.cmdName] = command
        }
    }

    fun attachManager(respondent: Manager) {
        this.client = respondent
    }

    fun setStdIO(ioStream: IOStream) {
        stdio = ioStream
    }

    override fun request(method: Method, args: Map<String, Any>): LdpResponse =
        when (method) {
            Method.GET -> {
                TODO("Todo get request")
            }
            else -> {
                client.request(method, args)
            }
        }


    fun loop() {
        while (WORK) {
            val cmdWithArg = getValidCmdWithArg(stdio)
            val cmdName = cmdWithArg.first
            val arg = cmdWithArg.second

            val resultStatus = executeCmd(cmdName, arg, this)
        }
    }

    private fun attachToHistory(cmdName: String) {
        history.add(cmdName)
        if (history.size > 6) history.removeFirst()
    }

    private fun validateCmd(cmd: String): Boolean = commands.containsKey(cmd)

    private fun getValidCmdWithArg(ioStream: IOStream = stdio): Pair<String, String> {
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

    private fun executeCmd(cmdName: String, arg: String, respondent: CommandManager): Int {
        attachToHistory(cmdName)
        return commands[cmdName]!!.execute(arg, respondent)
    }

    override fun stop(){
        this.WORK = false
        stdio.writeln("Command manager is switched off")
        stdio.writeln("Shutting down...")
    }
    /*
    private fun executeCmd(cmdName: String, arg: String, ioStream: IOStream=stdio): LdpRequest {
        attachToHistory(cmdName)
        return commands[cmdName]!!.execute(arg, ioStream)
    }
     */
    /*
    fun processCmd(cmdName: String, arg: String, ioStream: IOStream=stdio): LdpRequest {
        if (!validateCmd(cmdName)) {
            throw NonexistentCommandException(
                "Attempt to execute nonexistent command"
            )
        }
        return executeCmd(cmdName, arg, this)
    }
     */

    /*
    fun giveResponseToCmd(
        cmdName: String, response: LdpResponse, ioStream: IOStream=stdio
    ): LdpResponse {
        val cmd = commands[cmdName]!!
        return cmd.handleResponse(response, ioStream)
    }
     */

    /*
    fun getHistoryList(): List<String> {
        return history
    }
     */

    /*
    fun getHelpMap(): Map<String, String> {
        val helpMap: MutableMap<String, String> = hashMapOf()
        for(cmd in commands.values){
            helpMap[cmd.cmdName] = cmd.getDescription()
        }
        return helpMap
    }
     */

}