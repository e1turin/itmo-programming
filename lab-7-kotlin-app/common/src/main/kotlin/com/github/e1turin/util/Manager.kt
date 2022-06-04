package com.github.e1turin.util

import com.github.e1turin.protocol.api.LdpResponse

abstract class Manager {
    enum class Method { GET, POST, CONNECTION, AUTH }
    object Task {
        object Get { //TODO: refactor
//            const val count = "count"
//            const val get = "get"
        }
        object Post { //TODO: refactor
//            const val add = "add"
//            const val clear = "remove"
//            const val update = "update"
        }
        object Connect { //TODO: refactor
//            const val connect = "connect"
//            const val disconnect = "disconnect"
        }
        object Auth {
            const val login = "login"
            const val logout = "logout"
            const val signup = "sign-up"
            const val signout = "sign-out"
        }
        const val add = "add"
        const val clear = "remove"
        const val count = "count"
        const val update = "update"
        const val get = "get"
        const val connect = "connect"
        const val disconnect = "disconnect"
    }

    object Opt {
        const val `do` = "do"
        const val first_arg = "first-arg"
        const val second_arg = "second-arg"
        const val condition = "condition"
        const val all = "all"
        const val greater = ">"
        const val smaller = "<"
        const val equal = "="
        const val amount = "#"
    }

    object Value{
        object Get {
            const val help = "help"
            const val info = "info"
        }
        object Condition{//TODO: refactor
//            const val all = "all"
//            const val greater = ">"
//            const val smaller = "<"
//            const val equal = "="
//            const val amount = "#"
        }

    }
    abstract fun request(method: Method, args: Map<String, Any>): LdpResponse
    abstract fun stop()
    abstract val stdio: IOStream
    abstract val WORK: Boolean

}