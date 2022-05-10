package com.github.e1turin.lab6.protocol.api

import java.net.URI

data class LdpResponse(
    val status: String,
    val uri: URI,
    val headers: LdpHeaders,
    val body: String = ""
) {
    object Status {
        const val OK = "OK"
        const val FAIL = "FAIL"
    }

    fun setHeader(header: String, value: String){
        headers.add(header, value)
    }
    fun getHeader(header: String): String {
        return headers[header]
    }
    private fun hasHeader(header: String): Boolean {
        return headers.hasHeader(header)
    }

    companion object{
        fun deserialize(data: String): LdpResponse {
            TODO("Not yet implemented")
        }
    }

    /*
    fun command(cmdName: String): LdpResponse {
        setHeader(LdpHeaders.Headers.CMD_NAME, cmdName)
        return this
    }

    fun commandArg(argName: String, value: String): LdpResponse {
        val cmdName = if (this.hasHeader(LdpHeaders.Headers.CMD_NAME)) {
            this.getHeader(LdpHeaders.Headers.CMD_NAME)
        } else {
            LdpHeaders.Headers.UNDEFINED
        }
        this.setHeader("$cmdName-$argName", value)
        return this
    }
     */


}