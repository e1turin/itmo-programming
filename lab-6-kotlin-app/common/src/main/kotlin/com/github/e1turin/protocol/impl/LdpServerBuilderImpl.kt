package com.github.e1turin.protocol.impl

import com.github.e1turin.protocol.api.LdpServer
import java.io.IOException

internal class LdpServerBuilderImpl() : LdpServer.Builder {
//    override lateinit var host: InetAddress
//        private set
    override var localPort = -1
        private set
    override var timeout = 10000
        private set

//    override fun host(host: InetAddress): LdpServer.Builder {
//        this.host = host
//        return this
//    }

    override fun authorise(login: String, password: String): LdpServer.Builder {
        TODO("Not yet implemented")
    }

    override fun localPort(port: Int): LdpServer.Builder {
        if(port<0){
            throw IOException("Wrong port definition (<0)")
        }
        this.localPort = port
        return this
    }

    override fun timeout(ms: Int): LdpServer.Builder {
        this.timeout = ms
        return this
    }

    override fun build(): LdpServer {
        return LdpServerImpl(this)
    }

}