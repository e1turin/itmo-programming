package com.github.e1turin.lab5.common.commands

import com.github.e1turin.lab5.client.Client
import com.github.e1turin.lab5.common.containers.*
import com.github.e1turin.lab5.common.util.IOStream
import com.github.e1turin.lab5.protocol.LdpRequest
import com.github.e1turin.lab5.protocol.LdpResponse

class HistoryCmd(
    cmdName: String,
) : Command(cmdName) {
    init {
        this.description = "Вывести последние 6 команд (без их аргументов)"
    }

    object ARGS {
        const val history_list = "history_list"
    }

    override fun execute(arg: String, ioStream: IOStream): LdpRequest {
        ioStream.writeln("ИСТОРИЯ КОМАНД")
        return LdpRequest().apply {
            command(Client.TASKS.HISTORY)
        }
    }

    override fun handleResponse(taskResponse: Response, ioStream: IOStream): LdpResponse {
        ioStream.writeln(taskResponse.arg as String)
        return LdpResponse(LdpResponse.Status.OK)
    }

}

