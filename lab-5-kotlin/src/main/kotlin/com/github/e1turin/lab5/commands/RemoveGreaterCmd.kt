package com.github.e1turin.lab5.commands

import com.github.e1turin.lab5.collection.MusicBand
import com.github.e1turin.lab5.collection.MusicBandStorage
import com.github.e1turin.lab5.containers.Message
import com.github.e1turin.lab5.containers.Response
import com.github.e1turin.lab5.containers.ResponseType
import com.github.e1turin.lab5.util.IOStream

class RemoveGreaterCmd(target: MusicBandStorage, cmdName: String, ioStream: IOStream) :
    Command(cmdName, "Удалить из коллекции все элементы, превышающие заданный") {
    override fun exec(arg: String, target: MusicBandStorage, ioStream: IOStream): Message {
        ioStream.writeln("ФИЛЬТРАЦИЯ ЭЛЕМЕНТОВ БОЛЬШЕ ЗАДАНОГО")
        val musicBand: MusicBand =
            ioStream.readMusicBand("Необходимо заполнить значения следующих свойств:")
        target.filter { it > musicBand }
        ioStream.writeln("Элементы отфильтрованы")
        return Response(
            cmdName, ResponseType.TASK_COMPLETED,
            content = "$cmdName successfully executed with argument: '$arg'"
        )
    }
}
