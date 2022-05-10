package com.github.e1turin.lab5.common.commands

import com.github.e1turin.lab5.common.containers.*
import com.github.e1turin.lab5.common.util.IOStream

class ClearCmd(cmdName: String) : Command(cmdName, "Очистить коллекцию") {
    override fun execute(arg: String, ioStream: IOStream): Message {
        ioStream.writeln("ОЧИСТКА КОЛЛЕКЦИИ")
        ioStream.write("Вы уверены то хотите очистить коллекцию? [Yes/No] ")
        return if (ioStream.yesAnswer()) {
            Request(
                cmdName,
                RequestType.DO_TASK,
                "CLEAR_COLLECTION",
                content = "$cmdName executed with arg=$arg, sent Request, waits Response"
            )
        } else {
            Response(
                cmdName,
                ResponseStatus.TASK_FAILED,
                content = "$cmdName execution interrupted with argument: $arg"
            )
        }
    }

    override fun handleResponse(taskResponse: Response, ioStream: IOStream): Message {
        ioStream.writeln(
            when (taskResponse.status) {
                ResponseStatus.TASK_COMPLETED -> "Коллекция успешно очищена"
                ResponseStatus.TASK_FAILED -> "Ошибка! Коллекция не очищена"
                else -> taskResponse.status.toString()
            }
        )
        return Response(
            cmdName,
            taskResponse.status,
            content = "$cmdName got Response, after ${taskResponse.sender}"
        )
    }
}
