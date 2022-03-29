package com.github.e1turin.lab5.common.commands

import com.github.e1turin.lab5.common.containers.Message
import com.github.e1turin.lab5.common.containers.Response
import com.github.e1turin.lab5.common.containers.ResponseType
import com.github.e1turin.lab5.common.util.IOStream
import java.io.StringReader

class RemoveWithIDCmd(cmdName: String) :
    Command(cmdName, "Удалить элемент из коллекции по его id") {
    override fun execute(arg: String, ioStream: IOStream): Message {
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
