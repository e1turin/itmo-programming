package com.github.e1turin.lab5.common.commands

import com.github.e1turin.lab5.common.containers.Message
import com.github.e1turin.lab5.common.containers.Response
import com.github.e1turin.lab5.common.containers.ResponseType
import com.github.e1turin.lab5.common.util.IOStream

class PrintAscendingCmd(cmdName: String) :
    Command(
        cmdName, "Вывести элементы коллекции в порядке возрастания"
    ) {
    override fun execute(arg: String, ioStream: IOStream): Message {
        ioStream.writeln("ЭЛЕМЕНТЫ КОЛЛЕКЦИИ В ПОРЯДКЕ ВОЗРАСТАНИЯ")

        val sortedElements = target.toArray()
        sortedElements.sort()
        for(i in sortedElements){
            ioStream.writeln(i.toString())
        }

        return Response(
            cmdName, ResponseType.TASK_COMPLETED,
            content = "$cmdName executed with argument: '$arg'"
        )
    }
}
