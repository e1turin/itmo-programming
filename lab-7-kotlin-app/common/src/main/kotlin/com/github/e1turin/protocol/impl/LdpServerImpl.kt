package com.github.e1turin.protocol.impl

import com.github.e1turin.protocol.api.LdpOptions
import com.github.e1turin.protocol.api.LdpRequest
import com.github.e1turin.protocol.api.LdpResponse
import com.github.e1turin.protocol.api.LdpServer
import com.github.e1turin.protocol.exceptions.LdpConnectionException
import java.net.BindException
import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.ServerSocket
import java.net.SocketAddress
import java.nio.ByteBuffer
import java.nio.channels.DatagramChannel

/**
 * Implementation of LdpServer class
 */
internal class LdpServerImpl(builder: Builder) : LdpServer() {
    private var localHost = InetAddress.getByName("0.0.0.0")
//    private var localHost = InetAddress.getByName("localhost")
//    private var localHost = InetAddress.getLocalHost()

    override var localPort: Int = if (builder.localPort < 0) {
        throw UninitializedPropertyAccessException(
            "property localPort has not been initialized correctly"
        )
    } else {
        builder.localPort
    }
        private set
    override var timeout: Int = builder.timeout
        private set
    private lateinit var datagramChannel: DatagramChannel
    private val connections = mutableMapOf<SocketAddress, MutableMap<Int, String>>()

    init {
        this.localPort = builder.localPort
        this.timeout = builder.timeout
        println("hostname: $localHost")
    }

    override fun start() {
        try {
            this.datagramChannel = bindChannel(localPort)
            println("Using port 35047")
        } catch (e: BindException) {
            localPort = ServerSocket(0).localPort
            this.datagramChannel = bindChannel(localPort)
            println("Port 35047 is not free, using port: $localPort")
        }
    }

    private fun bindChannel(port: Int): DatagramChannel {
        return openChannel().bind(InetSocketAddress(port))
//        return openChannel().bind(InetSocketAddress(localHost, port))
    }

    private fun openChannel(): DatagramChannel {
        datagramChannel = DatagramChannel.open()
        datagramChannel.configureBlocking(false)
        return datagramChannel
    }

    override fun receiveRequest(): Pair<LdpRequest, SocketAddress>? {
        var data = ""
        var num: Int = 0
        var amount: Int = 1
        var dataPart: String = ""
        var address: SocketAddress? = null
        while (true) {
            val res = receivePieceOfRequest() ?: continue
            val numAmountData = res.first
            num = numAmountData.first
            amount = numAmountData.second
            dataPart += numAmountData.third
            address = res.second
            if (connections[address] == null) {
                connections[address] = mutableMapOf()
            }
            connections[address]!![num] = dataPart
            if ((connections[address]?.size ?: -1) == amount) {
                break //todo: place to concurrent processing,
            }
        }
        //todo: extract outside of loop to method `processRequest()`
        val dataParts = connections[address]?.values
        if (address == null || dataParts == null) {
            return null
        }
        for (dt in dataParts) {
            data += dt
        }
//        connections.remove(address)
        connections[address]?.clear() //TODO: timeout to clear connections
        return LdpRequest.deserialize(data.trim()) to address
    }

    private fun receivePieceOfRequest(): Pair<Triple<Int, Int, String>, SocketAddress>? {
        val buffer = ByteBuffer.allocate(512) //used only 512
        val responseAddress: SocketAddress? = datagramChannel.receive(buffer)

        responseAddress?.let { address ->
            println("received from $address")
            sendResponse(
                LdpResponse(LdpOptions.StatusCode.OK, body = "Request got successfully"),
                address
            )
            return extractData(buffer) to address
        }
        return null
    }

    override fun send(response: LdpResponse, address: SocketAddress): LdpResponse? {
        if (!this::datagramChannel.isInitialized) {
            throw LdpConnectionException("Connection not established")
        }
        //TODO:verifying response
//        val response = LdpResponse(LdpOptions.StatusCode.OK)
        sendResponse(response, address)
//        return receiveResponse()
        return null
    }

    private fun sendResponse(response: LdpResponse, address: SocketAddress) {
        val packets = subdivideToPackets(response.serialize())
        for (packet in packets) {
            val result: Boolean = sendPacket(packet, address)
        }
    }

    override fun receiveResponse(): LdpResponse {
        //TODO: ######## TIMEOUT!!! #########
        var data = ""
        var num: Int = 0
        var amount: Int = 1
        var dataPart: String
        var pieceOfResp: Triple<Int, Int, String>?
        do {
            pieceOfResp = receivePieceOfResponse()
            pieceOfResp?.let {
                num = it.first
                amount = it.second
                dataPart = it.third
                data += dataPart
            }
        } while (num < amount)
        println("deserialized resp: $data")
        return LdpResponse.deserialize(data.trim())
    }

    private fun receivePieceOfResponse(): Triple<Int, Int, String>? {
        val buffer = ByteBuffer.allocate(512) //used only 512
        val responseAddress: SocketAddress? = datagramChannel.receive(buffer)
        responseAddress?.let {
            //TODO: SocketAddress return?
            return extractData(buffer)
        }
        return null
    }


    private fun subdivideToPackets(data: String): Array<ByteArray> {
        var buffers = arrayOf<ByteArray>()
        val byteData = data.toByteArray().asList().chunked(510)
//        assert(byteData.size < 1L shl 16 - 1) todo: more bytes for amount
        for ((i, buf) in byteData.withIndex()) {
            buffers = buffers.plus(
                byteArrayOf(
                    (i + 1).toByte(), (byteData.size).toByte(), *buf.toByteArray()
                )
            )
        }
        return buffers
    }

    private fun sendPacket(packet: ByteArray, address: SocketAddress): Boolean {
        val bufferedData = ByteBuffer.wrap(packet)
        try {
            datagramChannel.send(bufferedData, address)
        } catch (e: Exception) {
            println("datagramChannel problem")
            throw e
        }
//        val res = receiveResponse() //TODO: receive different amount of packets
        //TODO: ################## wait and send again ##################
        return LdpOptions.StatusCode.OK == LdpOptions.StatusCode.OK
    }

    private fun extractData(buffer: ByteBuffer): Triple<Int, Int, String> {
//        (buffer as Buffer).flip()// crutch for java 11+ -> java 8
        buffer.flip()
        val bytes = buffer.array().take(buffer.remaining())
        val packetNumber = bytes[0].toInt()
        val packetsAmount = bytes[1].toInt()
        val data = String(bytes.slice(2 until bytes.size).toByteArray())
        return Triple(packetNumber, packetsAmount, data)
    }


    override fun close() {
        datagramChannel.close()
    }

}