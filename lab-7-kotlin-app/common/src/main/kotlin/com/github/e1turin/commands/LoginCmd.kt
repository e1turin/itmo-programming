package com.github.e1turin.commands

import com.github.e1turin.protocol.api.LdpOptions
import com.github.e1turin.util.Manager

class LoginCmd(cmdName: String) : Command(cmdName) {
    override var description: String = "Войти в систему"
    override fun execute(arg: String, respondent: Manager): Int {
        val io = respondent.stdio
        io.writeln("ВХОД В СИСТЕМУ")
        val login = io.termInput(
            sep = "login=",
            message = "Логин должен быть уникальный и не менее 4 символов",
            query = { io.readLineWord() }
        )

        if (login?.isBlank() != false || login.length < 4) {
            io.writeln("Логин не удовлетворяет условию")
            return LdpOptions.StatusCode.FAIL
        }

        val password = io.termInput(
            sep = "password=",
            message = "пароль не может быть пустым и меньше 6 символов",
            query = { io.readLineWord() }
        )

        if (password?.isBlank() != false || password.length < 6) {
            io.writeln("Пароль не удовлетворяет условию")
            return LdpOptions.StatusCode.FAIL
        }

        val response = respondent.request(
            Manager.Method.AUTH,
            mapOf(
                Manager.Opt.`do` to Manager.Task.Auth.login,
                Manager.Opt.Auth.login to login,
                Manager.Opt.Auth.password to password
            )
        )

        if (response.status == LdpOptions.StatusCode.OK){
            io.writeln("Вы успешно вошли в систему")
        } else {
            io.writeln("Возникли проблемы, вы не вошли в систему")
            io.writeln(response.body)
        }

        return response.status

    }
}
