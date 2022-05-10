package com.github.e1turin.lab5.common.commands

import com.github.e1turin.lab5.common.containers.*
import com.github.e1turin.lab5.common.util.IOStream

class PrintAscendingCmd(cmdName: String) : Command(
    cmdName, "Вывести элементы коллекции в порядке возрастания"
) {
    override fun execute(arg: String, ioStream: IOStream): Message {
        ioStream.writeln("ЭЛЕМЕНТЫ КОЛЛЕКЦИИ В ПОРЯДКЕ ВОЗРАСТАНИЯ")
        return Request(
            cmdName,
            RequestType.DO_TASK,
            "GIVE_LIST_OF_ELEMENTS",
            content = "$cmdName executed with arg=$arg, sent Request, waits Response arg as string",
            arg = "ASCENDING"
        )
    }

    override fun handleResponse(taskResponse: Response, ioStream: IOStream): Message {
        val stringListOfElements = taskResponse.arg as String
        ioStream.writeln(stringListOfElements)
        return Response(
            cmdName,
            ResponseStatus.TASK_COMPLETED,
            content = "$cmdName got Response, after ${taskResponse.sender}"
        )
    }
}
