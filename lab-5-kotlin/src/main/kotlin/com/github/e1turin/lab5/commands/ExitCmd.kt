package com.github.e1turin.lab5.commands

import com.github.e1turin.lab5.collection.MusicBandStorage
import com.github.e1turin.lab5.collection.StorageManager
import com.github.e1turin.lab5.containers.*
import com.github.e1turin.lab5.util.IOStream

class ExitCmd(target: MusicBandStorage, cmdName: String, ioStream: IOStream) :
    Command(cmdName, "Завершить программму (без сохранения в файл)") {
    override fun exec(arg: String, target: MusicBandStorage, ioStream: IOStream): Message {
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
