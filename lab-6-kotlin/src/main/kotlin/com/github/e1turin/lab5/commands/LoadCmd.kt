package com.github.e1turin.lab5.commands

import com.github.e1turin.lab5.collection.MusicBandStorage
import com.github.e1turin.lab5.collection.StorageManager
import com.github.e1turin.lab5.containers.*
import com.github.e1turin.lab5.util.IOStream

class LoadCmd(
    cmdName: String,
) : Command(cmdName, "Загрузка содержимого файла в коллекцию"),
    Requestable {
    override fun exec(arg: String, target: MusicBandStorage, ioStream: IOStream): Message {
        ioStream.writeln("ЗАГРУЗКА ДАННЫХ ИЗ ФАЙЛА " + target.name)

        val loadingDataPath: String = target.name //TODO file path input
        ioStream.write("Вы уверены, что хотите загрузить данный из этого файла? (Y[es]/N[o])")
        if (ioStream.yesAnswer()) {
            ioStream.writeln("Загрузка данных...")
            return Request(
                cmdName,
                RequestType.DO_TASK,
                StorageManager.TaskType.LOAD_DATA,
                content = "$cmdName executed with argument: '$arg' and agreement, loading data " +
                        "from $loadingDataPath"
            )
        } else {
            return Response(
                cmdName, ResponseType.TASK_FAILED,
                content = "Execution of $cmdName with argument: '$arg' was interrupted"
            )
        }
    }

    override fun exec(taskResponse: Response, ioStream: IOStream): Message {
        ioStream.writeln(taskResponse.content)
        return Response(
            cmdName,
            taskResponse.type,
            content = "$cmdName got Response, after ${taskResponse.sender}"
        )
    }
}