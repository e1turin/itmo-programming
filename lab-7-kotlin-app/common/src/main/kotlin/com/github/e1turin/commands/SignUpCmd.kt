package com.github.e1turin.commands

import com.github.e1turin.protocol.api.LdpOptions
import com.github.e1turin.util.Manager

class SignUpCmd(cmdName: String) : Command(cmdName) {
    override var description: String = "Зарегистрировать нового пользователя"
    override fun execute(arg: String, respondent: Manager): Int {
        val io = respondent.stdio
        io.writeln("РЕГИСТРАЦИЯ ПОЛЬЗОВАТЕЛЯ")
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
                Manager.Opt.`do` to Manager.Task.Auth.signup,
                Manager.Opt.Auth.login to login,
                Manager.Opt.Auth.password to password
            )
        )

        if (response.status == LdpOptions.StatusCode.OK){
            io.writeln("Пользователь успешно авторизован, теперь войдите в систему")
        } else {
            io.writeln("Пользователь не авторизован")
            io.writeln(response.body)
        }

        return response.status

    }
}