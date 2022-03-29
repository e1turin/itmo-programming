package com.github.e1turin.lab5.common.commands

import com.github.e1turin.lab5.common.collection.StorageManager
import com.github.e1turin.lab5.common.containers.*
import com.github.e1turin.lab5.common.util.IOStream

class ExitCmd( cmdName: String) :
    Command(cmdName, "Завершить программму (без сохранения в файл)") {
    override fun execute(arg: String, ioStream: IOStream): Message {
        ioStream.writeln("ВЫХОД ИЗ МЕНЕДЖЕРА КОЛЛЕКЦИИ")
        ioStream.write("Вы уверены, что хотите выйти? (Y[es]/N[o])")
        if (ioStream.yesAnswer()) {
            ioStream.writeln("Выход из консоли управления...")
            return Request(
                cmdName, RequestType.DO_TASK, StorageManager.TaskType.EXIT,
                content = "$cmdName executed with argument: '$arg' and agreement, " +
                        "exiting from manager"
            )
        }

        ioStream.writeln("Отмена завершения работы")
        return Response(
            cmdName, ResponseType.TASK_FAILED,
            content = "Execution of $cmdName with argument: '$arg' was interrupted"
        )

    }
}
