package com.github.e1turin.commands

import com.github.e1turin.app.MusicBand
import com.github.e1turin.protocol.api.LdpHeaders
import com.github.e1turin.protocol.api.LdpOptions
import com.github.e1turin.util.Manager
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json


class ShowCmd(cmdName: String) :
    Command(cmdName) {
    override var description: String = "Показать элементы коллекции"
    override fun execute(arg: String, respondent: Manager): Int {
        val io = respondent.stdio

        io.writeln("ПРОСМОТР ЭЛЕМЕНТОВ КОЛЛЕКЦИИ")

        val response = respondent.request(
            Manager.Method.GET,
            mapOf(
                Manager.Opt.`do` to Manager.Task.get,
                Manager.Opt.condition to Manager.Opt.amount,
                Manager.Opt.first_arg to Manager.Opt.all
            )
        )
        if (response.status == LdpOptions.StatusCode.OK) {
            val dataListJson: String? = response.headers[LdpHeaders.Headers.DATA]
            if (dataListJson!=null) {
                try {
                    val dataList: List<MusicBand> = Json.decodeFromString(dataListJson)
                    io.writeln(dataList.toString())
                }catch (e: Exception) {
                    io.writeln("Не удалось извлечь данные из ответа")
                }
            } else {
                io.writeln("Получен некорректный ответ")
            }
        } else {
            io.writeln("Операция не выполнена")
            io.writeln("Ответ сервера: ${response.body}")
        }
        return response.status

    }
}