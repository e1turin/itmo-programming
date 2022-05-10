package com.github.e1turin.lab5.common.commands

import com.github.e1turin.lab5.common.containers.*
import com.github.e1turin.lab5.common.util.IOStream

class AverageOfNumberOfParticipantsCmd(
    cmdName: String
) : Command(cmdName, "Вывести среднее значение поля numberOfParticipants") {
    override fun execute(arg: String, ioStream: IOStream): Message {
        ioStream.writeln(
            "ПОДСЧЕТ CРЕДНЕГО ЗНАЧЕНИЕ ПОЛЯ numberOfParticipants"
        )
        /*
        var sumOfNumberOfParticipants = 0
        target.toArray().forEach { sumOfNumberOfParticipants += it.getNumberOfParticipants() }
        ioStream.writeln(
            "CРЕДНЕЕ ЗНАЧЕНИЕ ПОЛЯ numberOfParticipants = " +
                    sumOfNumberOfParticipants / target.size
        )

         */
        return Request(
            cmdName, RequestType.DO_TASK, "GIVE_AVERAGE_VALUE",
            content = "$cmdName executed with arg=$arg, sent Request, waits Response as Int",
            arg = "numberOfParticipants"
        )

        /*
        return Response(
            cmdName, ResponseType.TASK_COMPLETED,
            content = "$cmdName executed with argument: arg='$arg'"
        )

         */
    }

    override fun handleResponse(taskResponse: Response, ioStream: IOStream): Message {
        ioStream.writeln("Среднее значение поля numberOfParticipants = ${taskResponse.arg as Int}")

        return Response(
            cmdName,
            taskResponse.status,
            content = "$cmdName got Response, after ${taskResponse.sender}"
        )
    }


}
