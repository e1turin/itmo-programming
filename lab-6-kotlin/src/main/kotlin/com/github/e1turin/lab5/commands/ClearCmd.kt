package com.github.e1turin.lab5.commands

import com.github.e1turin.lab5.collection.MusicBandStorage
import com.github.e1turin.lab5.collection.StorageManager
import com.github.e1turin.lab5.containers.*
import com.github.e1turin.lab5.util.IOStream

class ClearCmd(cmdName: String) :
    Command(cmdName, "Очистить коллекцию"),
    Requestable {
    override fun exec(arg: String, target: MusicBandStorage, ioStream: IOStream): Message {
        ioStream.writeln("ОЧИСТКА КОЛЛЕКЦИИ " + target.name)
        ioStream.writeln("Вы уверены то хотите очистить коллекцию? [Yes/No]")
        return if(ioStream.yesAnswer()){
            Request(cmdName, RequestType.DO_TASK, StorageManager.TaskType.CLEAR_COLLECTION,
                content = "$cmdName executed with arg=$arg, sent Request, waits Response"
            )
        } else {
            Response(
                cmdName, ResponseType.TASK_FAILED,
                content = "$cmdName execution interrupted with argument: $arg"
            )
        }
    }

    override fun exec(taskResponse: Response, ioStream: IOStream): Message{
        ioStream.writeln(taskResponse.content)
        return Response(cmdName, taskResponse.type,
            content = "$cmdName got Response, after ${taskResponse.sender}"
        )
    }
}
