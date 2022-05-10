package com.github.e1turin.lab5.common.commands

import com.github.e1turin.lab5.common.containers.*
import com.github.e1turin.lab5.common.util.IOStream
import java.io.StringReader
import java.io.Writer

class CountLessThanAlbumsCountCmd(cmdName: String) : Command(
    cmdName, "Вывести количество элементов, значение поля albumsCount которых меньше заданного"
) {
    override fun execute(arg: String, ioStream: IOStream): Message {
        ioStream.writeln("ПОДСЧЕТ КОЛИЧЕСТВА ЭЛЕМЕНТОВ МЕНЬШЕ ЗАДАНОГО")

        val stringIOStream = IOStream(StringReader(arg), Writer.nullWriter(), false)
        val albumsCount = stringIOStream.readLongOrNull()
        if (albumsCount == null || albumsCount < 0) {
            ioStream.writeln(
                "Не верный параметр запроса! Должно быть натуральное число больше 1 " + "(long)"
            )
            return Response(
                cmdName,
                ResponseStatus.TASK_FAILED,
                content = "$cmdName failed with wrong argument: '$arg'"
            )

        } else {
            /*
                val albumsCount: Long = ioStream.termInputUntil(
                    sep = "albumsCount = ",
                    message = "Введите значение поля, относительно которого требуется считать",
                    condition = { it != null && it > 0 },
                    hint = "Значение поля должно быть натуральным числом (long)",
                    query = { ioStream.readLongOrNull() }
                )!!
             */

            return Request(
                cmdName,
                RequestType.DO_TASK,
                "COUNT_ELEMENTS",
                content = "$cmdName executed with arg=$arg, sent Request, waits Response as Long)",
                arg = RequestArg(
                    "LESS",
                    "albumsCount", //element property
                    albumsCount //referencing value
                )
            )

            /*
            return Response(
                cmdName,
                ResponseType.TASK_COMPLETED,
                content = "$cmdName executed with argument: $arg"
            )

             */
        }

    }

    override fun handleResponse(taskResponse: Response, ioStream: IOStream): Message {
        ioStream.writeln("Количество элементов = ${taskResponse.arg as Long}")
        return Response( cmdName,
            taskResponse.status,
            content = "$cmdName got Response, after ${taskResponse.sender}"
        )
    }
}
