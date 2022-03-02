package com.github.e1turin.lab5.commands

import com.github.e1turin.lab5.collection.MusicBandStorage
import com.github.e1turin.lab5.containers.Message
import com.github.e1turin.lab5.containers.Response
import com.github.e1turin.lab5.containers.ResponseType
import com.github.e1turin.lab5.util.IOStream
import java.io.StringReader

class CountLessThanAlbumsCountCmd(target: MusicBandStorage, cmdName: String, ioStream: IOStream) :
    Command(
        cmdName, "Вывести количество элементов, значение поля albumsCount " +
                "которых меньше заданного"
    ) {
    override fun exec(arg: String, target: MusicBandStorage, ioStream: IOStream): Message {
        ioStream.writeln("ПОДСЧЕТ КОЛИЧЕСТВА ЭЛЕМЕНТОВ МЕНЬШЕ ЗАДАНОГО")

        val stringIOStream = IOStream(StringReader(arg), ioStream.writer, false)
        val albumsCount = stringIOStream.readLongOrNull()
        if (albumsCount == null || albumsCount < 0) {
            ioStream.writeln("Не верный параметр запроса! Должно быть натуральное число от 1 " +
                    "(long)")
            return Response(
                cmdName, ResponseType.TASK_FAILED,
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
            var countElements: Int = target.count { (it.getAlbumsCount() < albumsCount) }
            ioStream.writeln("Количество элементов: $countElements")

            return Response(
                cmdName, ResponseType.TASK_COMPLETED,
                content = "$cmdName executed with argument: $arg"
            )
        }

    }
}
