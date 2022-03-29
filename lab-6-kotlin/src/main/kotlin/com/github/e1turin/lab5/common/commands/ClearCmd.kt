package com.github.e1turin.lab5.common.commands

import com.github.e1turin.lab5.common.collection.StorageManager
import com.github.e1turin.lab5.common.containers.*
import com.github.e1turin.lab5.common.util.IOStream

class ClearCmd(cmdName: String) :
    Command(cmdName, "Очистить коллекцию"),
    Requestable {
    override fun execute(arg: String, ioStream: IOStream): Message {
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

    override fun getResponse(taskResponse: Response, ioStream: IOStream): Message {
        ioStream.writeln(taskResponse.content)
        return Response(cmdName, taskResponse.type,
            content = "$cmdName got Response, after ${taskResponse.sender}"
        )
    }
}
