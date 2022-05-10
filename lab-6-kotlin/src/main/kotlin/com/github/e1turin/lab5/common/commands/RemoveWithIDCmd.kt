package com.github.e1turin.lab5.common.commands

import com.github.e1turin.lab5.common.containers.*
import com.github.e1turin.lab5.common.util.IOStream
import java.io.StringReader

class RemoveWithIDCmd(cmdName: String) :
    Command(cmdName, "Удалить элемент из коллекции по его id") {
    override fun execute(arg: String, ioStream: IOStream): Message {
        ioStream.writeln("УДАЛЕНИЕ ОБЪЕКТА")
        val stringIOStream = IOStream(StringReader(arg), ioStream.writer, false)
        val id = stringIOStream.readIntOrNull()
        if (id == null || id < 0 ) {
            ioStream.writeln("Не верный параметр запроса! Должно быть натуральное число от 1 " +
                    "(int)")
            return Response(
                cmdName, ResponseStatus.TASK_FAILED,
                content = "$cmdName failed with wrong argument: arg='$arg'"
            )
        } else {
            return Request(
                cmdName, RequestType.DO_TASK, "REMOVE_ELEMENTS",
                content = "$cmdName executed with arg=$arg, sent Request, waits Response with content" +
                        " as String",
                arg = RequestArg("ID",
                    id,
                    0) //mock
            )
            return Response(
                cmdName, ResponseStatus.TASK_COMPLETED,
                content = "$cmdName executed with argument: $arg"
            )
        }
    }

    override fun handleResponse(taskResponse: Response, ioStream: IOStream): Message {
        ioStream.writeln(
            when (taskResponse.status) {
                ResponseStatus.TASK_COMPLETED -> "Элемент успешно удален"
                ResponseStatus.TASK_FAILED -> "Ошибка! Элемент не удален"
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
