package com.github.e1turin.commands

import com.github.e1turin.protocol.api.LdpOptions
import com.github.e1turin.util.Manager

class LogoutCmd(cmdName: String) : Command(cmdName) {
    override var description: String = "Выйти из системы"
    override fun execute(arg: String, respondent: Manager): Int {
        val io = respondent.stdio
        io.writeln("ВЫХОД ИЗ СИСТЕМЫ")
        val response = respondent.request(
            Manager.Method.AUTH,
            mapOf(
                Manager.Opt.`do` to Manager.Task.Auth.logout,
            )
        )

        if (response.status == LdpOptions.StatusCode.OK){
            io.writeln("Вы успешно вышли из систему")
        } else {
            io.writeln("Возникли проблемы, вы не вышли из системы")
            io.writeln(response.body)
        }

        return response.status

    }
}