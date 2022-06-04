package com.github.e1turin.protocol.api

import kotlinx.serialization.Serializable

@Serializable
class LdpHeaders {
    private val headers = mutableMapOf<String, String>()

    object Headers {
        const val UNDEFINED = "Undefined"
        const val CMD_NAME = "CMD"
        const val CONDITION = "Condition"
        const val SECOND_CONDITION = "Condition-2"
        const val URI_NAME = "URI"
        const val FROM_ADDRESS = "From"
        const val REQUEST = "Request"
        const val RESPONSE = "Response"
        const val DATA = "Data"
        const val USER = "uname"
        const val PASSWD = "passwd"
        object Args {
            const val FIRST_ARG = "Arg-1"
            const val SECOND_ARG = "Arg-2"

        }
        object Condition {
            const val begin = "begin"
            const val end = "end"
            const val none = "cond:none"
            const val property = "cond:prop"

        }
    }

    object Values {
        object Cmd {
            const val add = "add"
            const val remove = "remove"
            const val count = "count"
            const val update = "update"
            const val get = "get"
            const val connect = "connect"
            const val disconnect = "disconnect"
            const val auth = "auth"
        }
        object Condition {
            const val greater = ">" //todo: move
            const val less = "<" //todo: move
            const val equals = "==" //todo: move
            const val none = "none"
            const val amount = "num" //todo: move
            const val range = "range" //todo: move
            const val property = "prop"


            object Args {
                const val all = "all"
                const val obj = "obj"
//                const val greater = ">"
//                const val less = "<"
//                const val equals = "=="
            }

        }

    }

    fun add(header: String, value: String): LdpHeaders{
        headers[header] = value
        return this
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