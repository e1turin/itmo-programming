package com.github.e1turin.commands

import com.github.e1turin.app.MusicBand
import com.github.e1turin.protocol.api.LdpOptions
import com.github.e1turin.util.Manager

class AddCmd(cmdName: String) :
    Command(cmdName) {
    override var description: String = "Добавить новый элемент в коллекцию"
    override fun execute(arg: String, respondent: Manager): Int {
        val io = respondent.stdio

        io.writeln("ДОБАВЛЕНИЕ НОВОГО ЭЛЕМЕНТА В КОЛЛЕКЦИЮ")
        val musicBand: MusicBand = io.readMusicBand(
            "Необходимо заполнить значения следующих свойств элемента: "
        )

        val response = respondent.request(
            Manager.Method.POST,
            mapOf(
                Manager.Opt.`do` to Manager.Task.add,
                Manager.Opt.first_arg to musicBand
            )
        )
        if (response.status == LdpOptions.StatusCode.OK){
            io.writeln("Элемент успешно добавлен")
        } else {
            io.writeln("Элемент не добавлен")
            io.writeln(response.body)
        }

        return response.status

    }
    /*
    override fun execute(arg: String, ioStream: IOStream): Message {
        ioStream.writeln("ДОБАВЛЕНИЕ НОВОГО ЭЛЕМЕНТА В КОЛЛЕКЦИЮ")
        val musicBand: MusicBand = ioStream.readMusicBand(
            "Необходимо заполнить значения следующих свойств: "
        )
        return Request(
            cmdName, RequestType.DO_TASK, "ADD_ELEMENT",
            content = "$cmdName executed with arg=$arg, sent Request, waits Response with content" +
                    " as String",
            arg = musicBand
        )
     */

    /*
        musicBand.setId(target.nextElementId())
        target.add(musicBand)
        return Response(
            cmdName, ResponseType.TASK_COMPLETED,
            content = "$cmdName executed with argument: arg='$arg'"
        )
    }
    */

    /*
    override fun handleResponse(taskResponse: Response, ioStream: IOStream): Message {
//        ioStream.writeln("Новый элемент добавлен")
        ioStream.writeln(taskResponse.content)

        return Response(
            cmdName,
            taskResponse.status,
            content = "$cmdName got Response, after ${taskResponse.sender}"
        )
    }
     */
}
