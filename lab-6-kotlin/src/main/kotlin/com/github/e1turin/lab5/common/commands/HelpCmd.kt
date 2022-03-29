package com.github.e1turin.lab5.common.commands

import com.github.e1turin.lab5.common.collection.StorageManager
import com.github.e1turin.lab5.common.containers.*
import com.github.e1turin.lab5.common.util.IOStream

class HelpCmd(cmdName: String) :
    Command(cmdName, "Вывести справку по доступным командам"),
    Requestable {

    override fun execute(arg: String, ioStream: IOStream): Message {
        ioStream.writeln("ОПИСАНИЕ КОМАНД МЕНЕДЖЕРА КОЛЛЕКЦИИ " + target.name)
        return Request(
            cmdName,
            RequestType.DO_TASK,
            StorageManager.TaskType.GIVE_HELP,
            content = "$cmdName executed with arg=$arg, sent Request, waits Response (doc strings)"
        )
    }

    override fun getResponse(taskResponse: Response, ioStream: IOStream): Message {
        val helpStrings = taskResponse.arg as String
        ioStream.writeln(helpStrings)
        return Response(
            cmdName, ResponseType.TASK_COMPLETED,
            content = "$cmdName got Response, after ${taskResponse.sender}"
        )
    }
}
