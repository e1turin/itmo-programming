package com.github.e1turin.lab5.common.commands

import com.github.e1turin.lab5.common.collection.MusicBand
import com.github.e1turin.lab5.common.containers.Message
import com.github.e1turin.lab5.common.containers.Response
import com.github.e1turin.lab5.common.containers.ResponseType
import com.github.e1turin.lab5.common.util.IOStream

class RemoveGreaterCmd( cmdName: String) :
    Command(cmdName, "Удалить из коллекции все элементы, превышающие заданный") {
    override fun execute(arg: String, ioStream: IOStream): Message {
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
