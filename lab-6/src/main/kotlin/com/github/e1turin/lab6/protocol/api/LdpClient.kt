package com.github.e1turin.lab6.protocol.api

import com.github.e1turin.lab6.protocol.impl.LdpClientBuilderImpl
import java.net.*


abstract class LdpClient protected constructor() : AutoCloseable {

    companion object{
        fun newBuilder(): Builder {
            return LdpClientBuilderImpl()
        }
    }

    interface Builder {
        fun build(): LdpClient
//        fun uri(uri: String): Builder
//        val uri: URI
        val port: Int
        fun port(port: Int): Builder
        val timeout: Int
        fun timeout(ms: Int): Builder
        val host: InetAddress
        fun host(host: InetAddress): Builder
        fun authorise(login: String, password: String): Builder
    }

    abstract fun start()

    abstract fun send(request: LdpRequest): LdpResponse

    abstract fun receive(): LdpResponse

}







