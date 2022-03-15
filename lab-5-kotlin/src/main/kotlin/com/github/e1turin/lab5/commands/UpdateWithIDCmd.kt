package com.github.e1turin.lab5.commands

import com.github.e1turin.lab5.collection.MusicBand
import com.github.e1turin.lab5.collection.MusicBandStorage
import com.github.e1turin.lab5.containers.Message
import com.github.e1turin.lab5.containers.Response
import com.github.e1turin.lab5.containers.ResponseType
import com.github.e1turin.lab5.util.IOStream
import java.io.StringReader

class UpdateWithIDCmd(cmdName: String) :
    Command(
        cmdName, "Обновить значение элемента коллекции, id которого равен " +
                "заданному"
    ) {
    override fun exec(arg: String, target: MusicBandStorage, ioStream: IOStream): Message {
        ioStream.writeln("ОБНОВЛЕНИЕ ЭЛЕМЕНТА " + target.name)
        val stringIOStream = IOStream(StringReader(arg), ioStream.writer, false)
        val id = stringIOStream.readIntOrNull()
        if (id == null || id < 0 || !target.hasElementWithID(id)) {
            ioStream.writeln("Не верный параметр запроса! Должно быть натуральное число от 1 " +
                    "(int), либо такого id не существует")
            return Response(
                cmdName, ResponseType.TASK_FAILED,
                content = "$cmdName failed with wrong argument: arg='$arg'"
            )
        } else {
            val musicBand: MusicBand = ioStream.readMusicBand(
                "Необходимо заполнить значения следующих свойств: "
            )
            musicBand.setId(id)
            target.add(musicBand)
            ioStream.writeln("Элемент изменен")

            return Response(
                cmdName, ResponseType.TASK_COMPLETED,
                content = "$cmdName executed with argument: $arg"
            )
        }
    }
}
