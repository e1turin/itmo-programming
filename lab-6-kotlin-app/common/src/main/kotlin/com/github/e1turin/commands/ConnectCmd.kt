package com.github.e1turin.commands

import com.github.e1turin.protocol.api.LdpOptions
import com.github.e1turin.util.Manager

class ConnectCmd(cmdName: String) :
    Command(cmdName) {
    override var description: String = "Подключиться к серверу"
    override fun execute(arg: String, respondent: Manager): Int {
        val io = respondent.stdio

        io.writeln("ПОДКЛЮЧЕНИЕ К СЕРВЕРУ")
        //TODO: process arguments (host, port)

        val response = respondent.request(
            Manager.Method.CONNECTION,
            mapOf(
                Manager.Opt.`do` to Manager.Task.connect,
            )
        )
        if (response.status == LdpOptions.StatusCode.OK){
            io.writeln("Соединение установлено")
        } else {
            io.writeln("Соединение не установлено")
            io.writeln(response.body)
        }

        return response.status

    }
}
