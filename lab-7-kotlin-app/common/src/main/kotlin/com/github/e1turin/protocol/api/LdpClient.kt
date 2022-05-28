package com.github.e1turin.protocol.api

import com.github.e1turin.protocol.impl.LdpClientBuilderImpl
import java.net.*


abstract class LdpClient protected constructor() : AutoCloseable {
//    object StatusCode{
//        const val OK = 0
//        const val FAIL = 1
//    }
    abstract val timeout: Int
    abstract val port: Int
    abstract val host: InetAddress
    abstract val localHost: InetAddress
    abstract val localPort: Int
    abstract val isOpen: Boolean

    companion object{
        fun newBuilder(): Builder {
            return LdpClientBuilderImpl()
        }
    }

    abstract fun connect(): Int
    abstract fun disconnect(): Int
    abstract fun send(request: LdpRequest): LdpResponse
    abstract fun receive(): LdpResponse


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


}







