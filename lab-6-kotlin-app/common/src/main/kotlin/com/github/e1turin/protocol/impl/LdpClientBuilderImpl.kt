package com.github.e1turin.protocol.impl

import com.github.e1turin.protocol.api.LdpClient
import java.io.IOException
import java.net.InetAddress

internal class LdpClientBuilderImpl: LdpClient.Builder {
    override lateinit var host: InetAddress
        private set
    override var port = -1
        private set
    override var timeout: Int = 5000
        private set
//    override lateinit var uri: URI
//        private set


    override fun build(): LdpClient {
        return LdpClientImpl(this)
    }

//    fun uri(uri: String): LdpClientBuilderImpl {
//        checkURI(uri)
//        this.uri = URI("udp://$uri${if (this.port!=-1){":${port}"}}")
//        return this
//    }

    override fun port(port: Int): LdpClientBuilderImpl {
        if(port<0){
            throw IOException("Wrong port definition (<0)")
        }
        this.port = port
        return this
    }

    override fun host(host: InetAddress): LdpClientBuilderImpl {
        //checkHost()
        this.host = host
        return this
    }


    override fun timeout(ms: Int): LdpClientBuilderImpl {
        if(ms<1){
            throw IOException("Wrong timeout definition (<1)")
        }
        this.timeout = ms
        return this
    }

    override fun authorise(login: String, password: String): LdpClientBuilderImpl {
        TODO("Not yet implemented")
        return this
    }
}
