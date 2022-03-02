package com.github.e1turin.lab5.commands

import com.github.e1turin.lab5.collection.MusicBandStorage
import com.github.e1turin.lab5.collection.StorageManager
import com.github.e1turin.lab5.containers.*
import com.github.e1turin.lab5.util.IOStream
import java.util.LinkedList

class HistoryCmd(
    target: MusicBandStorage,
    cmdName: String,
    ioStream: IOStream,
    val history: LinkedList<String>
) : Command(cmdName, "Вывести последние 6 команд (без их аргументов)"), Requestable {

    override fun exec(arg: String, target: MusicBandStorage, ioStream: IOStream): Message {
        ioStream.writeln("ИСТОРИЯ КОМАНД МЕНЕДЖЕРА КОЛЛЕКЦИИ " + target.name)
        if (history.isNotEmpty()) {
            for (it in 1..history.size) {
                ioStream.writeln("$it: ${history[it - 1]}")
            }
        } else {
            ioStream.writeln("<История команд пуста>")
        }
        return Request(cmdName,RequestType.DO_TASK, StorageManager.TaskType.GIVE_HISTORY,
            content = "$cmdName executed with arg=$arg, sent Request, waits Response"
        )
    }

    override fun exec(taskResponse: Response, ioStream: IOStream): Message{
        ioStream.writeln(taskResponse.content)
        return Response(cmdName, ResponseType.TASK_COMPLETED,
            content = "$cmdName got Response, after ${taskResponse.sender}"
        )
    }

}

