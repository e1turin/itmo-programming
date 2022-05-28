package com.github.e1turin.protocol.api

import com.github.e1turin.protocol.impl.LdpServerBuilderImpl
import java.net.SocketAddress

abstract class LdpServer protected constructor(): AutoCloseable {
    abstract val localPort: Int
    abstract val timeout: Int
    companion object{
        fun newBuilder(): Builder {
            return LdpServerBuilderImpl()
        }
    }

    abstract fun send(response: LdpResponse, address: SocketAddress): LdpResponse?
    abstract fun receiveRequest(): Pair<LdpRequest, SocketAddress>?
    abstract fun start()

    interface Builder {
        fun build(): LdpServer
//        fun uri(uri: String): Builder
//        val uri: URI
        val localPort: Int
        fun localPort(port: Int): Builder
        val timeout: Int
        fun timeout(ms: Int): Builder
//        val host: InetAddress
//        fun host(host: InetAddress): Builder
        fun authorise(login: String, password: String): Builder
    }

    abstract fun receiveResponse(): LdpResponse
}