package com.github.e1turin.lab5.commands

import com.github.e1turin.lab5.collection.MusicBandStorage
import com.github.e1turin.lab5.containers.Message
import com.github.e1turin.lab5.containers.Response
import com.github.e1turin.lab5.containers.ResponseType
import com.github.e1turin.lab5.util.IOStream

class InfoCmd(cmdName: String) :
    Command(
        cmdName, "вывести в стандартный поток вывода информацию о " +
                "коллекции (тип, дата инициализации, количество элементов и т.д."
    ) {
    override fun exec(arg: String, target: MusicBandStorage, ioStream: IOStream): Message {
        ioStream.writeln("ИНФОРМАЦИЯ О КОЛЛЕКЦИИ ${target.name}")
        ioStream.writeln(target.getInfo())
        return Response(
            cmdName, ResponseType.TASK_COMPLETED,
            content = "$cmdName executed with argument: arg='$arg'"
        )
        // TODO get StorageManager info
    }
}

