package com.github.e1turin.lab6.protocol.api

import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.SocketAddress
import java.net.UnknownHostException
import java.nio.channels.DatagramChannel

class LdpServer(val uri: String, val port: Int) : AutoCloseable {
    private val host: InetAddress
    private var address: SocketAddress
    private lateinit var datagramChannel: DatagramChannel

    init {
        try {
            host = InetAddress.getByName(uri)
        } catch (e: UnknownHostException) {
            throw e //Todo
        }
        address = InetSocketAddress(host, port)
    }

    fun open() {
        datagramChannel = DatagramChannel.open() //todo: move to method
        datagramChannel.configureBlocking(false)
    }

    override fun close() {
        TODO("Not yet implemented")
    }

}