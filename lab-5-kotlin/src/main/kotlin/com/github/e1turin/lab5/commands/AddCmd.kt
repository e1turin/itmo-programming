package com.github.e1turin.lab5.commands

import com.github.e1turin.lab5.collection.MusicBand
import com.github.e1turin.lab5.collection.MusicBandStorage
import com.github.e1turin.lab5.containers.Message
import com.github.e1turin.lab5.containers.Response
import com.github.e1turin.lab5.containers.ResponseType
import com.github.e1turin.lab5.util.IOStream

class AddCmd(cmdName: String) :
    Command(cmdName, "Добавить новый элемент в коллекцию") {
    override fun exec(arg: String, target: MusicBandStorage, ioStream: IOStream): Message {
        ioStream.writeln("ДОБАВЛЕНИЕ НОВОГО ЭЛЕМЕНТА В КОЛЛЕКЦИЮ " + target.name)
        val musicBand: MusicBand = ioStream.readMusicBand(
            "Необходимо заполнить значения следующих свойств: "
        )
        musicBand.setId(target.nextElementId())
        target.add(musicBand)
        ioStream.writeln("Новый элемент добавлен")

        return Response(
            cmdName, ResponseType.TASK_COMPLETED,
            content = "$cmdName executed with argument: arg='$arg'"
        )
    }
}
