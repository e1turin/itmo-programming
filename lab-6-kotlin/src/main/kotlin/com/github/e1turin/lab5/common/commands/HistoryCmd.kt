package com.github.e1turin.lab5.common.commands

import com.github.e1turin.lab5.common.collection.StorageManager
import com.github.e1turin.lab5.common.containers.*
import com.github.e1turin.lab5.common.util.IOStream

class HistoryCmd(
    cmdName: String,
) : Command(cmdName, "Вывести последние 6 команд (без их аргументов)"), Requestable {

    override fun execute(arg: String, ioStream: IOStream): Message {
        ioStream.writeln("ИСТОРИЯ КОМАНД МЕНЕДЖЕРА КОЛЛЕКЦИИ " + target.name)
        return Request(cmdName,RequestType.DO_TASK, StorageManager.TaskType.GIVE_HISTORY,
            content = "$cmdName executed with arg=$arg, sent Request, waits Response"
        )
    }

    override fun getResponse(taskResponse: Response, ioStream: IOStream): Message {
        ioStream.writeln(taskResponse.content)
        return Response(cmdName, ResponseType.TASK_COMPLETED,
            content = "$cmdName got Response, after ${taskResponse.sender}"
        )
    }

}

