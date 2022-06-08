package com.github.e1turin.protocol.api

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.net.URI

@kotlinx.serialization.Serializable
data class LdpResponse(
    val status: Int, //LdpOptions.StatusCode
//    val uri: URI, //for safety
    val headers: LdpHeaders = LdpHeaders(),
    val body: String = ""
) {

    fun setHeader(header: String, value: String){
        headers.add(header, value)
    }
    fun getHeader(header: String): String? {
        return headers[header]
    }
    private fun hasHeader(header: String): Boolean {
        return headers.hasHeader(header)
    }

    fun serialize(): String {
        val str = Json.encodeToString(this)
//        println("serialize resp: '$str'")
        return str
    }

    companion object{
        fun deserialize(data: String): LdpResponse {
//            println("deserialize resp: '$data'")
            return Json.decodeFromString(data)

//            return LdpResponse(LdpOptions.StatusCode.OK, body = "TODO deserialize Response")
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