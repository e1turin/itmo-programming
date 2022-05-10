package com.github.e1turin.lab5.common.commands

import com.github.e1turin.lab5.common.application.MusicBand
import com.github.e1turin.lab5.common.containers.*
import com.github.e1turin.lab5.common.util.IOStream

class RemoveAllGreaterCmd(cmdName: String) :
    Command(cmdName, "Удалить из коллекции все элементы, превышающие заданный") {
    override fun execute(arg: String, ioStream: IOStream): Message {
        ioStream.writeln("ФИЛЬТРАЦИЯ ЭЛЕМЕНТОВ БОЛЬШЕ ЗАДАНОГО")
        val musicBand: MusicBand =
            ioStream.readMusicBand("Необходимо заполнить значения следующих свойств:")

        return Request(
            cmdName, RequestType.DO_TASK, "REMOVE_ELEMENTS",
            content = "$cmdName executed with arg=$arg, sent Request, waits Response with content" +
                    " as String",
            arg = RequestArg("GREATER",
                -1, //amount
                musicBand //comparingElement
            )
        )
    }

    override fun handleResponse(taskResponse: Response, ioStream: IOStream): Message {
        ioStream.writeln(
            when (taskResponse.status) {
                ResponseStatus.TASK_COMPLETED -> "Коллекция успешно отфильтрована"
                ResponseStatus.TASK_FAILED -> "Ошибка! Коллекция не отфильтрована"
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
