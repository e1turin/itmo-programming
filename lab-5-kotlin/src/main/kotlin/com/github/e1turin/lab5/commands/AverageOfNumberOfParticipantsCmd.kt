package com.github.e1turin.lab5.commands

import com.github.e1turin.lab5.collection.MusicBandStorage
import com.github.e1turin.lab5.containers.Message
import com.github.e1turin.lab5.containers.Response
import com.github.e1turin.lab5.containers.ResponseType
import com.github.e1turin.lab5.util.IOStream

class AverageOfNumberOfParticipantsCmd(
    target: MusicBandStorage,
    cmdName: String,
    ioStream: IOStream
) : Command(cmdName, "Вывести среднее значение поля numberOfParticipants") {
    override fun exec(arg: String, target: MusicBandStorage, ioStream: IOStream): Message {
        var sumOfNumberOfParticipants = 0
        target.toArray().forEach { sumOfNumberOfParticipants += it.getNumberOfParticipants() }
        ioStream.writeln(
            "CРЕДНЕЕ ЗНАЧЕНИЕ ПОЛЯ numberOfParticipants = " +
                    sumOfNumberOfParticipants / target.size
        )

        return Response(
            cmdName, ResponseType.TASK_COMPLETED,
            content = "$cmdName executed with argument: arg='$arg'"
        )
    }


}
