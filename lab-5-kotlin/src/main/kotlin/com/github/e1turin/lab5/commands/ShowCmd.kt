package com.github.e1turin.lab5.commands

import com.github.e1turin.lab5.collection.MusicBandStorage
import com.github.e1turin.lab5.containers.Message
import com.github.e1turin.lab5.containers.Response
import com.github.e1turin.lab5.containers.ResponseType
import com.github.e1turin.lab5.util.IOStream

class ShowCmd(target: MusicBandStorage, cmdName: String, ioStream: IOStream) :
    Command(
        cmdName, "Вывести в стандартный поток вывода все элементы " +
                "коллекции в строковом представлении"
    ) {
    override fun exec(arg: String, target: MusicBandStorage, ioStream: IOStream): Message {
        ioStream.writeln("CОДЕРЖИМОЕ КОЛЛЕКЦИИ " + target.name)
        var i: Int=1
        for(it in target.toArray()){
            ioStream.writeln("${i++}: $it")
        }
        ioStream.writeln("<Конец коллекции>")
        return Response(
            cmdName, ResponseType.TASK_COMPLETED,
            content = "$cmdName executed with argument: '$arg'"
        )
    }
}
