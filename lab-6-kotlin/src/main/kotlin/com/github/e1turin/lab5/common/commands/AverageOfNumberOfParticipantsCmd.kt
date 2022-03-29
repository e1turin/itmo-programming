package com.github.e1turin.lab5.common.commands

import com.github.e1turin.lab5.common.containers.Message
import com.github.e1turin.lab5.common.containers.Response
import com.github.e1turin.lab5.common.containers.ResponseType
import com.github.e1turin.lab5.common.util.IOStream

class AverageOfNumberOfParticipantsCmd(
    cmdName: String
) : Command(cmdName, "Вывести среднее значение поля numberOfParticipants") {
    override fun execute(arg: String, ioStream: IOStream): Message {
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
