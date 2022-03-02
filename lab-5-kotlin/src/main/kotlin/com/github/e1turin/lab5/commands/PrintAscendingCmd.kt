package com.github.e1turin.lab5.commands

import com.github.e1turin.lab5.collection.MusicBandStorage
import com.github.e1turin.lab5.containers.Message
import com.github.e1turin.lab5.containers.Response
import com.github.e1turin.lab5.containers.ResponseType
import com.github.e1turin.lab5.util.IOStream

class PrintAscendingCmd(target: MusicBandStorage, cmdName: String, ioStream: IOStream) :
    Command(
        cmdName, "Вывести элементы коллекции в порядке возрастания"
    ) {
    override fun exec(arg: String, target: MusicBandStorage, ioStream: IOStream): Message {
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
