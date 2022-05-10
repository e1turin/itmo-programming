package com.github.e1turin.lab5.common.commands

import com.github.e1turin.lab5.common.containers.Message
import com.github.e1turin.lab5.common.containers.Response
import com.github.e1turin.lab5.common.containers.ResponseStatus
import com.github.e1turin.lab5.common.util.IOStream

class ShowCmd(cmdName: String) :
    Command(
        cmdName, "Вывести в стандартный поток вывода все элементы " +
                "коллекции в строковом представлении"
    ) {
    override fun execute(arg: String, ioStream: IOStream): Message {
        ioStream.writeln("CОДЕРЖИМОЕ КОЛЛЕКЦИИ " + target.name)
        var i: Int=1
        for(it in target.toArray()){
            ioStream.writeln("${i++}: $it")
        }
        ioStream.writeln("<Конец коллекции>")
        return Response(
            cmdName, ResponseStatus.TASK_COMPLETED,
            content = "$cmdName executed with argument: '$arg'"
        )
    }
}
