package com.github.e1turin.lab5.common.commands

import com.github.e1turin.lab5.common.collection.MusicBand
import com.github.e1turin.lab5.common.collection.StorageManager
import com.github.e1turin.lab5.common.containers.*
import com.github.e1turin.lab5.common.util.IOStream

class AddCmd(cmdName: String) :
    Command(cmdName, "Добавить новый элемент в коллекцию"){
    override fun execute(arg: String, ioStream: IOStream): Message {
        ioStream.writeln("ДОБАВЛЕНИЕ НОВОГО ЭЛЕМЕНТА В КОЛЛЕКЦИЮ")
        val musicBand: MusicBand = ioStream.readMusicBand(
            "Необходимо заполнить значения следующих свойств: "
        )

        return Request(
            cmdName, RequestType.DO_TASK, "ADD_ELEMENT",
            content = "$cmdName executed with arg=$arg, sent Request, waits Response (doc strings)",
            arg = musicBand
        )
/*
        musicBand.setId(target.nextElementId())
        target.add(musicBand)

        return Response(
            cmdName, ResponseType.TASK_COMPLETED,
            content = "$cmdName executed with argument: arg='$arg'"
        )

 */

    }

    override fun getResponse(taskResponse: Response, ioStream: IOStream): Message {
//        ioStream.writeln("Новый элемент добавлен")
        ioStream.writeln(taskResponse.content)

        return Response(
            cmdName,
            taskResponse.type,
            content = "$cmdName got Response, after ${taskResponse.sender}"
        )
    }
}
