package com.github.e1turin.commands

import com.github.e1turin.protocol.api.LdpHeaders
import com.github.e1turin.protocol.api.LdpOptions
import com.github.e1turin.util.Manager

class HelpCmd(cmdName: String) :
    Command(cmdName) {
    override var description: String = "Показать элементы коллекции"
    override fun execute(arg: String, respondent: Manager): Int {
        val io = respondent.stdio

        io.writeln("ПРОСМОТР ЭЛЕМЕНТОВ КОЛЛЕКЦИИ")

        val response = respondent.request(
            Manager.Method.GET,
            mapOf(
                Manager.Opt.`do` to Manager.Task.get,
                Manager.Opt.single_arg to Manager.Value.Get.help
            )
        )
        if (response.status == LdpOptions.StatusCode.OK) {
            val helpString: String? = response.headers[LdpHeaders.Headers.DATA]
            if (helpString!=null) {
                io.writeln(helpString)
            } else {
                io.writeln("Получен некорректный ответ")
            }
        } else {
            io.writeln("Операция не выполнена")
            io.writeln("Ответ клиента: ${response.body}")
        }
        return response.status

    }
}
