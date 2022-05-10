package com.github.e1turin.lab5.protocol

import com.google.gson.Gson

class LdpRequest() : LdpMessage() { //TODO: builder !!!


    fun header(header: String, value: String): LdpRequest {
        this.setHeader(header, value)
        return this
    }

    fun uri(uri: String): LdpRequest {
        header(HEADERS.URI_NAME, uri)
        return this
    }

    fun getURI(): String {
        return if (this.hasHeader(HEADERS.URI_NAME)) {
            this.getHeader(HEADERS.URI_NAME)
        } else "" //todo
    }

    fun command(cmdName: String): LdpRequest {
        header(HEADERS.CMD_NAME, cmdName)
        return this
    }

    fun commandArg(argName: String, value: String): LdpRequest {
        val cmdName = if (this.hasHeader(HEADERS.CMD_NAME)) {
            this.getHeader(HEADERS.CMD_NAME)
        } else {
            HEADERS.UNDEFINED
        }
        this.setHeader("$cmdName-$argName", value)
        return this
    }

    fun getCommandArg(key: String): String {
        return if (hasHeader("${getHeader(HEADERS.CMD_NAME)}-$key")) {
            getHeader("${getHeader(HEADERS.CMD_NAME)}-$key")
        } else {
            HEADERS.UNDEFINED
        }
    }

    fun serialise(): String {
        val gson = Gson()
        TODO()
    }
}
