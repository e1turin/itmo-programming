package com.github.e1turin.lab5.commands

import com.github.e1turin.lab5.collection.MusicBandStorage
import com.github.e1turin.lab5.collection.StorageManager
import com.github.e1turin.lab5.containers.*
import com.github.e1turin.lab5.util.IOStream

class HelpCmd(target: MusicBandStorage, cmdName: String, ioStream: IOStream) :
    Command(cmdName, "Вывести справку по доступным командам"),
    Requestable {

    override fun exec(arg: String, target: MusicBandStorage, ioStream: IOStream): Message {
        ioStream.writeln("ОПИСАНИЕ КОМАНД МЕНЕДЖЕРА КОЛЛЕКЦИИ" + target.name)
        return Request(
            cmdName,
            RequestType.DO_TASK,
            StorageManager.TaskType.GIVE_HELP,
            content = "$cmdName executed with arg=$arg, sent Request, waits Response (doc strings)"
        )
    }

    override fun exec(taskResponse: Response, ioStream: IOStream): Message {
        val helpStrings = taskResponse.arg as String
        ioStream.writeln(helpStrings)
        return Response(
            cmdName, ResponseType.TASK_COMPLETED,
            content = "$cmdName got Response, after ${taskResponse.sender}"
        )
    }
}
