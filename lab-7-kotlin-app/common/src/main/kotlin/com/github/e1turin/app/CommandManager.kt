package com.github.e1turin.app

import com.github.e1turin.commands.Command
import com.github.e1turin.exceptions.NonexistentCommandException
import com.github.e1turin.protocol.api.LdpHeaders
import com.github.e1turin.protocol.api.LdpOptions
import com.github.e1turin.protocol.api.LdpResponse
import com.github.e1turin.util.IOStream
import com.github.e1turin.util.Manager
import kotlinx.coroutines.channels.Channel
import java.util.*

class CommandManager(vararg commands: Command) : Manager() {
    private val commands: MutableMap<String, Command> = HashMap<String, Command>()
    private val history: MutableList<String> = LinkedList()
    override lateinit var stdio: IOStream
        private set
    private lateinit var client: Manager
    override var WORK = true
        private set
    private val cmdChannel = Channel<Command>()

    init {
        for (command in commands) {
            this.commands[command.cmdName] = command
        }
    }

    val historyList: List<String>
        get() {
            return history
        }
    val helpAsMap: Map<String, String>
        get() {
            val helpMap: MutableMap<String, String> = hashMapOf()
            for (cmd in commands.values) {
                helpMap[cmd.cmdName] = cmd.getDescription()
            }
            return helpMap
        }
    val helpAsString: String
        get() {
            var helpString = "Команда | <Описание>\n"
            for (cmd in commands.values) {

                helpString += "${cmd.cmdName}\t: ${cmd.getDescription()}\n"
            }
            return helpString
        }

    fun attachManager(respondent: Manager) {
        this.client = respondent
    }

    fun setStdIO(ioStream: IOStream) {
        stdio = ioStream
    }

    override fun request(method: Method, args: Map<String, Any>): LdpResponse {
        return when (method) {
            Method.GET -> handleMethodGet(args)
            else -> {
                client.request(method, args)
            }
        }
    }

    private fun handleMethodGet(args: Map<String, Any>): LdpResponse {
        return when (args[Opt.`do`]) {
            Task.get -> {
                when (args[Opt.first_arg]) {
                    Value.Get.help -> {
                        LdpResponse(
                            LdpOptions.StatusCode.OK,
                            LdpHeaders().add(LdpHeaders.Headers.DATA, helpAsString)
                        )
                    }
                    else -> { //todo: info
                        client.request(Method.GET, args)
                    }
                }
            }
            else -> {
                client.request(Method.GET, args)
            }
        }

    }

    fun loop() { //todo: runBlocking | IO
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

    override fun stop() {
        this.WORK = false
        stdio.writeln("Command manager is switched off")
        stdio.writeln("Shutting down...")
    }
}