package com.github.e1turin.lab5.common.commands

import com.github.e1turin.lab5.common.containers.*
import com.github.e1turin.lab5.common.util.IOStream
import com.github.e1turin.lab5.protocol.LdpRequest
import com.github.e1turin.lab5.protocol.LdpResponse

class HelpCmd(cmdName: String) :
    Command(cmdName, "Вывести справку по доступным командам") {

    object ARGS {
        const val doc_str = "doc_str"
    }

    override fun execute(arg: String, ioStream: IOStream): LdpRequest {
        ioStream.writeln("ОПИСАНИЕ КОМАНД МЕНЕДЖЕРА КОЛЛЕКЦИИ")
        return Request(
            cmdName,
            RequestType.DO_TASK,
            "GIVE_HELP",
            content = "$cmdName executed with arg=$arg, sent Request, waits Response (doc strings)"
        )
    }

    override fun handleResponse(taskResponse: Response, ioStream: IOStream): LdpResponse {
        val helpStrings = taskResponse.arg as String
        ioStream.writeln(helpStrings)
        return Response(
            cmdName, ResponseStatus.TASK_COMPLETED,
            content = "$cmdName got Response, after ${taskResponse.sender}"
        )
    }
}
