package com.github.e1turin.lab5.common.commands

import com.github.e1turin.lab5.common.collection.StorageManager
import com.github.e1turin.lab5.common.containers.*
import com.github.e1turin.lab5.common.util.IOStream
import java.io.File

class ExecuteScriptCmd(cmdName: String) : Command(
    cmdName, "Считать и исполнить скрипт из указанного файла. В " +
            "скрипте содержатся команды в таком же виде, в котором их вводит пользователь"
), Requestable {
    override fun execute(arg: String, ioStream: IOStream): Message {
        ioStream.writeln("ВЫПОЛЕНЕНИЕ СКРИПТА")

        var pathToScriptFile: String = ioStream.termInput(
            sep = "path =",
            message = "Введите путь до файла скрипта",
//            condition = { it != null && File(it).exists() },
//            hint = "Не верный путь до файла",
            query = { ioStream.read() }
        )!!
        if (!File(pathToScriptFile).exists()) {
            ioStream.writeln("Не верный путь до файла")
            return Response(
                cmdName, ResponseType.TASK_FAILED,
                content = "wrong path to execution script"
            )
        }

//        var scriptFile = File(pathToScriptFile)

        return Request(
            cmdName, RequestType.DO_TASK, StorageManager.TaskType.EXEC_SCRIPT,
            arg = pathToScriptFile,
            content = "$cmdName executed with arg=$arg, sent Request, waits Response"
        )

    }

    override fun getResponse(taskResponse: Response, ioStream: IOStream): Message {
        ioStream.writeln(taskResponse.content)
        if (taskResponse.type == ResponseType.TASK_FAILED) throw taskResponse.arg as Exception
        return Response(
            cmdName, taskResponse.type,
            content = "$cmdName got Response, after ${taskResponse.sender}"
        )
    }
}
