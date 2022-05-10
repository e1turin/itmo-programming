package com.github.e1turin.lab6.protocol.impl

import com.github.e1turin.lab6.protocol.api.LdpClient
import java.net.InetAddress

class LdpClientBuilderImpl: LdpClient.Builder {
    override lateinit var host: InetAddress
        private set
    override var port = -1
        private set
    override var timeout: Int = 10000
        private set
//    override lateinit var uri: URI
//        private set


    override fun build(): LdpClient {
        return LdpClientImmutableImpl(this)
    }

//    fun uri(uri: String): LdpClientBuilderImpl {
//        checkURI(uri)
//        this.uri = URI("udp://$uri${if (this.port!=-1){":${port}"}}")
//        return this
//    }

    override fun port(port: Int): LdpClientBuilderImpl {
        this.port = port
        return this
    }

    override fun host(host: InetAddress): LdpClientBuilderImpl {
        //checkHost()
        this.host = host
        return this
    }


    override fun timeout(ms: Int): LdpClientBuilderImpl {
        TODO("Not yet implemented")
        return this
    }

    override fun authorise(login: String, password: String): LdpClientBuilderImpl {
        TODO("Not yet implemented")
        return this
    }
}
