package com.github.e1turin.util

import com.github.e1turin.protocol.api.LdpResponse

abstract class Manager {
    enum class Method { GET, POST, CONNECTION }
    object Task {
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
        const val single_arg = "single-arg"
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
        object Condition{//TODO:
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