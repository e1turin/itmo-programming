package com.github.e1turin.lab5.common.commands

import com.github.e1turin.lab5.common.containers.Message
import com.github.e1turin.lab5.common.containers.Response
import com.github.e1turin.lab5.common.containers.ResponseType
import com.github.e1turin.lab5.common.util.IOStream

class InfoCmd(cmdName: String) :
    Command(
        cmdName, "вывести в стандартный поток вывода информацию о " +
                "коллекции (тип, дата инициализации, количество элементов и т.д."
    ) {
    override fun execute(arg: String, ioStream: IOStream): Message {
        ioStream.writeln("ИНФОРМАЦИЯ О КОЛЛЕКЦИИ ${target.name}")
        ioStream.writeln(target.getInfo())
        return Response(
            cmdName, ResponseType.TASK_COMPLETED,
            content = "$cmdName executed with argument: arg='$arg'"
        )
        // TODO get StorageManager info
    }
}

