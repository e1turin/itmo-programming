package com.github.e1turin.lab6.protocol.api

class LdpHeaders {
    private val headers = mutableMapOf<String, String>()

    object Headers {
        const val UNDEFINED = "Undefined"
        const val CMD_NAME = "CMD"
        const val URI_NAME = "URI"
        const val FROM_ADDRESS = "From"
        const val REQUEST = "Request"
        const val RESPONSE = "Response"
    }

    fun add(header: String, value: String){
        headers[header] = value
    }

    fun hasHeader(header: String): Boolean {
        return headers.containsKey(header)
    }

    @Throws(IllegalArgumentException::class)
    operator fun get(header: String): String {
        return headers[header] ?: throw IllegalArgumentException("No such header")
    }


}