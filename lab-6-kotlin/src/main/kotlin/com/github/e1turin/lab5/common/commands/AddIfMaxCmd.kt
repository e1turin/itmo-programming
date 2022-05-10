package com.github.e1turin.lab5.common.commands

import com.github.e1turin.lab5.common.application.MusicBand
import com.github.e1turin.lab5.common.containers.*
import com.github.e1turin.lab5.common.util.IOStream

class AddIfMaxCmd(
    cmdName: String,
) : Command(
    cmdName,
    "Добавить новый элемент в коллекцию, если его значение " + "превышает значения наибольшего элемента этой коллекции"
) {
    override fun execute(arg: String, ioStream: IOStream): Message {
        ioStream.writeln("ДОБАВЛЕНИЕ НОВОГО МАКСИМАЛЬНОГО ЭЛЕМЕНТА")
        val musicBand: MusicBand = ioStream.readMusicBand(
            "Необходимо заполнить значения следующих свойств: "
        )
        return Request(
            cmdName, RequestType.DO_TASK, "ADD_IF_MAX",
            content = "$cmdName executed with arg=$arg, sent Request, waits Response (doc strings)",
            arg = musicBand
        )
        /*
        musicBand.setId(target.nextElementId())
        if (musicBand > target.getMax()) {
            target.add(musicBand)
            ioStream.writeln("Новый элемент добавлен")
        } else {
            ioStream.writeln("Элемент не добавлен, так как он не максимальный")
        }

         */

        /*
        return Response(
            cmdName, ResponseType.NONE, content = "$cmdName executed with argument: arg=$arg"
        )
         */
    }

    override fun handleResponse(taskResponse: Response, ioStream: IOStream): Message {
//        ioStream.writeln("Новый элемент добавлен")
        ioStream.writeln(taskResponse.content)

        return Response(
            cmdName,
            taskResponse.status,
            content = "$cmdName got Response, after ${taskResponse.sender}"
        )
    }
}

