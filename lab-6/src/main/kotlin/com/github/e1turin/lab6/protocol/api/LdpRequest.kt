package com.github.e1turin.lab6.protocol.api

import com.github.e1turin.lab6.protocol.impl.LdpRequestBuilderImpl
import java.net.URI

abstract class LdpRequest protected constructor() {


    object METHOD {
        const val POST = "POST"
        const val PUT = "PUT"
        const val GET = "GET"
    }

    interface Builder {
        val headers: LdpHeaders
        fun header(name: String, value: String): Builder
        val uri: URI
        fun uri(uri: URI): Builder
//        fun checkURI(): Boolean
        val method: String
        fun method(method: String)
        fun PUT()
        fun POST()
        fun GET()
        fun build(): LdpRequest
    }

    abstract fun uri(): URI
    abstract fun method(): String
    abstract fun headers(): LdpHeaders

    companion object {
        fun newBuilder(): Builder {
            return LdpRequestBuilderImpl()
        }
        fun newBuilder(request: LdpRequest): Builder {
            return LdpRequestBuilderImpl(request)
        }
    }

    abstract fun serialize(): String

}