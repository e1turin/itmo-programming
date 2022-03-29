package com.github.e1turin.lab5.common.collection

import com.github.e1turin.lab5.common.commands.Command
import com.github.e1turin.lab5.common.containers.Message
import com.github.e1turin.lab5.common.containers.Requestable
import com.github.e1turin.lab5.common.containers.Response
import com.github.e1turin.lab5.common.exceptions.InvalidCmdArgumentException
import com.github.e1turin.lab5.common.exceptions.NonexistentCommandException
import com.github.e1turin.lab5.common.exceptions.NotRequestableCommandException
import com.github.e1turin.lab5.common.util.IOStream
import java.util.HashMap

class CommandManager (
    vararg commands: Command,
){
    val commands: MutableMap<String, Command> = HashMap<String, Command>()

    init {
        for (command in commands) {
            this.commands[command.cmdName] = command
//            stdIOStream.write(command.cmdName + "; ")
        }
    }

    fun getCmdWithArg(ioStream: IOStream): Pair<String, String> {
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

    fun processCmd(cmdName: String, arg: String, ioStream: IOStream): Message {
        TODO()
    }

    fun validateCmd(cmd: String): Boolean = commands.containsKey(cmd)
    fun executeCmd(cmdName: String, arg: String, ioStream: IOStream): Message {
        return commands[cmdName]!!.execute(arg, ioStream)
    }
    fun giveResponseToCmd(
        cmdName: String, response: Response, ioStream: IOStream
    ): Message {
        val cmd = commands[cmdName]!!
//        if (cmd is Requestable) {
            return cmd.getResponse(response, ioStream)
//        } else {
//            throw NotRequestableCommandException()
//        }
    }

}