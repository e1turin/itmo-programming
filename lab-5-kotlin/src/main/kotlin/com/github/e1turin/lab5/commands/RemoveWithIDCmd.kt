package com.github.e1turin.lab5.commands

import com.github.e1turin.lab5.collection.MusicBandStorage
import com.github.e1turin.lab5.containers.Message
import com.github.e1turin.lab5.containers.Response
import com.github.e1turin.lab5.containers.ResponseType
import com.github.e1turin.lab5.util.IOStream
import java.io.StringReader

class RemoveWithIDCmd(target: MusicBandStorage, cmdName: String, ioStream: IOStream) :
    Command(cmdName, "Удалить элемент из коллекции по его id") {
    override fun exec(arg: String, target: MusicBandStorage, ioStream: IOStream): Message {
        ioStream.writeln("УДАЛЕНИЕ ОБЪЕКТА")
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
            val result = target.deleteWithID(id)
            if (result) {
                ioStream.writeln("объект удален из коллекции")
            } else {
                ioStream.writeln("объект не удален из коллекции")
            }
            return Response(
                cmdName, ResponseType.TASK_COMPLETED,
                content = "$cmdName executed with argument: $arg"
            )
        }
    }
}
