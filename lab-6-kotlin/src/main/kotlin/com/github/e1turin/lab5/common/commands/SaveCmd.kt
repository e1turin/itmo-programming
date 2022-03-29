package com.github.e1turin.lab5.common.commands

import com.github.e1turin.lab5.common.collection.StorageManager
import com.github.e1turin.lab5.common.containers.*
import com.github.e1turin.lab5.common.util.IOStream

class SaveCmd(cmdName: String) : Command(cmdName, "Сохранить коллекцию в файл"), Requestable {
    override fun execute(arg: String, ioStream: IOStream): Message {
        ioStream.writeln("СОХРАНЕНИЕ КОЛЛЕКЦИИ " + target.name)
        val savingDataPath: String = target.name //TODO: Input path
        ioStream.write("Вы уверены, что хотите сохранить файл по этому пути? (Y[es]/N[o])")
        if (ioStream.yesAnswer()) {
            ioStream.writeln("Сохранение данных...")
            return Request(
                cmdName,
                RequestType.DO_TASK,
                StorageManager.TaskType.SAVE_DATA,
                arg = savingDataPath,
                content = "$cmdName executed with argument: '$arg' and agreement, saving data to $savingDataPath"
            )
        } else {
            return Response(
                cmdName, ResponseType.TASK_FAILED,
                content = "Execution of $cmdName with argument: '$arg' was interrupted"
            )
        }
    }

    override fun getResponse(taskResponse: Response, ioStream: IOStream): Message {
        ioStream.writeln(taskResponse.content)
        return Response(
            cmdName,
            taskResponse.type,
            content = "$cmdName got Response, after ${taskResponse.sender}"
        )
    }
}

