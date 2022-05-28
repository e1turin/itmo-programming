package com.github.e1turin.commands

import com.github.e1turin.protocol.api.LdpOptions
import com.github.e1turin.util.Manager

class ExitCmd(cmdName: String) :
    Command(cmdName) {
    override var description: String = "Завершить работу с приложением"
    override fun execute(arg: String, respondent: Manager): Int {
        val io = respondent.stdio

        io.writeln("ВЫХОД ИЗ МЕНЕДЖЕРА КОЛЛЕКЦИИ")
        io.write("Вы уверены, что хотите выйти? (Y[es]/N[o])")
        if (io.yesAnswer()) {
            io.writeln("Выход из консоли управления...")
            respondent.stop()
            return LdpOptions.StatusCode.OK
        }

        io.writeln("Отмена завершения работы")
        return LdpOptions.StatusCode.FAIL
    }
}
