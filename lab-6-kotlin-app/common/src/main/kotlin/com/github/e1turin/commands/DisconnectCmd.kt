package com.github.e1turin.commands

import com.github.e1turin.protocol.api.LdpOptions
import com.github.e1turin.util.Manager

class DisconnectCmd(cmdName: String) :
    Command(cmdName) {
    override var description: String = "Отключиться от сервера"
    override fun execute(arg: String, respondent: Manager): Int {
        val io = respondent.stdio

        io.writeln("ОТКЛЮЧЕНИЕ ОТ СЕРВЕРА")
        //TODO: process arguments (host, port)

        val response = respondent.request(
            Manager.Method.CONNECTION,
            mapOf(
                Manager.Opt.`do` to Manager.Task.disconnect,
            )
        )
        io.write("Соединение разорвано ")
        if (response.status == LdpOptions.StatusCode.OK){
            io.writeln("(успешно)")
        } else {
//            io.writeln("Соединение не разорвано")
            io.writeln("(не успешно)")
            io.writeln(response.body)
        }

        return response.status

    }
}
