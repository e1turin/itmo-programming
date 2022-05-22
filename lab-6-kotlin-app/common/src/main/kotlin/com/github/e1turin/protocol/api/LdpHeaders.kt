package com.github.e1turin.protocol.api

import kotlinx.serialization.Serializable

@Serializable
class LdpHeaders {
    private val headers = mutableMapOf<String, String>()

    object Headers {
        const val UNDEFINED = "Undefined"
        const val CMD_NAME = "CMD"
        const val CONDITION = "Condition"
        const val URI_NAME = "URI"
        const val FROM_ADDRESS = "From"
        const val REQUEST = "Request"
        const val RESPONSE = "Response"
        object Args {
            const val FIRST_ARG = "Arg-1"
            const val SECOND_ARG = "Arg-2"

        }
    }

    object Values {
        object Cmd {
            const val add = "add"
            const val clear = "remove"
            const val count = "count"
            const val update = "update"
            const val get = "get"
            const val connect = "connect"
            const val disconnect = "disconnect"
        }
        object Condition {
            const val greater = ">"
            const val less = "<"
            const val equals = "="
            const val none = "none"
            const val amount = "num"


        }
    }

    fun add(header: String, value: String){
        headers[header] = value
    }

    fun hasHeader(header: String): Boolean {
        return headers.containsKey(header)
    }

//    @Throws(IllegalArgumentException::class)
    operator fun get(header: String): String? {
        return headers[header]
//            ?: throw IllegalArgumentException("No such header")
    }


}