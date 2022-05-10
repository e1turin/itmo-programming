package com.github.e1turin.lab5.common.commands

import com.github.e1turin.lab5.common.containers.*
import com.github.e1turin.lab5.common.util.IOStream

class InfoCmd(cmdName: String) :
    Command(
        cmdName, "вывести в стандартный поток вывода информацию о " +
                "коллекции (тип, дата инициализации, количество элементов и т.д."
    ) {
    override fun execute(arg: String, ioStream: IOStream): Message {
        ioStream.writeln("ИНФОРМАЦИЯ О КОЛЛЕКЦИИ")
        return Request(
            cmdName,
            RequestType.DO_TASK,
            "GIVE_COLLECTION_INFO",
            content = "$cmdName executed with arg=$arg, sent Request, waits Response arg as string"
        )
    }

    override fun handleResponse(taskResponse: Response, ioStream: IOStream): Message {
        val infoString = taskResponse.arg as String
        ioStream.writeln(infoString)
        return Response(
            cmdName, ResponseStatus.TASK_COMPLETED,
            content = "$cmdName got Response, after ${taskResponse.sender}"
        )
    }
}

