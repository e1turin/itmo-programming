package com.github.e1turin.lab5.commands

import com.github.e1turin.lab5.collection.MusicBand
import com.github.e1turin.lab5.collection.MusicBandStorage
import com.github.e1turin.lab5.containers.Message
import com.github.e1turin.lab5.containers.Response
import com.github.e1turin.lab5.containers.ResponseType
import com.github.e1turin.lab5.util.IOStream

class AddIfMaxCmd(
    cmdName: String,
) : Command(
    cmdName,
    "Добавить новый элемент в коллекцию, если его значение " + "превышает значения наибольшего элемента этой коллекции"
) {
    override fun exec(arg: String, target: MusicBandStorage, ioStream: IOStream): Message {
        ioStream.writeln("ДОБАВЛЕНИЕ НОВОГО МАКСИМАЛЬНОГО ЭЛЕМЕНТА" + target.name)
        val musicBand: MusicBand = ioStream.readMusicBand(
            "Необходимо заполнить значения следующих свойств: "
        )
        musicBand.setId(target.nextElementId())
        if (musicBand > target.getMax()) {
            target.add(musicBand)
            ioStream.writeln("Новый элемент добавлен")
        } else {
            ioStream.writeln("Элемент не добавлен, так как он не максимальный")
        }
        return Response(
            cmdName, ResponseType.NONE, content = "$cmdName executed with argument: arg=$arg"
        )
    }
}

