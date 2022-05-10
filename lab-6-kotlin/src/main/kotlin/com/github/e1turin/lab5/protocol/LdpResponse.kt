package com.github.e1turin.lab5.protocol

import com.google.gson.Gson

class LdpResponse(
    val status: String,
): LdpMessage() { //todo interface

    fun header(header: String, value: String): LdpResponse {
        this.setHeader(header, value)
        return this
    }

    fun uri(uri: String): LdpResponse {
        header(HEADERS.URI_NAME, uri)
        return this
    }

    fun getURI(): String {
        return if (this.hasHeader(HEADERS.URI_NAME)) {
            this.getHeader(HEADERS.URI_NAME)
        } else "" //todo
    }

    fun command(cmdName: String): LdpResponse {
        header(HEADERS.CMD_NAME, cmdName)
        return this
    }

    fun commandArg(argName: String, value: String): LdpResponse {
        val cmdName = if (this.hasHeader(HEADERS.CMD_NAME)) {
            this.getHeader(HEADERS.CMD_NAME)
        } else {
            HEADERS.UNDEFINED
        }
        this.setHeader("$cmdName-$argName", value)
        return this
    }

    object Status {
        const val OK = "OKAY"
        const val FAIL = "FAIL"
        const val NONE = "NONE"
    }

    companion object{
        fun deserialize(data: String): LdpResponse{
            val gson = Gson()
            TODO("deserialise")
        }
    }

}
