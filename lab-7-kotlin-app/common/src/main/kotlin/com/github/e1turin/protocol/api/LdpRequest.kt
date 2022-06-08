package com.github.e1turin.protocol.api

import com.github.e1turin.protocol.impl.LdpRequestBuilderImpl
import com.github.e1turin.protocol.impl.LdpRequestImmutableImpl
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.net.URI

@kotlinx.serialization.Serializable
abstract class LdpRequest protected constructor() {

    object METHOD {
        const val POST = "POST"
//        const val PUT = "PUT"
        const val GET = "GET"
    }

    interface Builder {
        val headers: LdpHeaders
        fun header(name: String, value: String): Builder
        val uri: URI
        fun uri(uri: URI): Builder
//        fun checkURI(): Boolean
        val method: String
        fun method(method: String): Builder
//        fun PUT()
        fun POST()
        fun GET()
        fun build(): LdpRequest
    }

//    abstract val uri: URI
    abstract val method: String
    abstract val headers: LdpHeaders
    abstract fun serialize(): String

    companion object {
        fun newBuilder(): Builder {
            return LdpRequestBuilderImpl()
        }
        fun newBuilder(request: LdpRequest): Builder {
            return LdpRequestBuilderImpl(request)
        }

        fun deserialize(data: String): LdpRequest {
//            println("deserialize req: '$data'")
            return Json.decodeFromString<LdpRequestImmutableImpl>(data)
            /*
            return LdpRequest.newBuilder()
                .uri(URI(""))
                .header(LdpHeaders.Headers.CMD_NAME, "TODO: cmd req deserialize")
                .header(LdpHeaders.Headers.Args.FIRST_ARG, "TODO: req deserialize")
                .build()

             */
        }
    }


}