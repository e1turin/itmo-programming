package com.github.e1turin.lab6.protocol.impl

import com.github.e1turin.lab6.protocol.api.LdpClient
import com.github.e1turin.lab6.protocol.api.LdpRequest
import com.github.e1turin.lab6.protocol.api.LdpResponse
import java.io.IOException
import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.SocketAddress
import java.net.URI
import java.nio.ByteBuffer
import java.nio.channels.DatagramChannel

class LdpClientImmutableImpl(builder: LdpClient.Builder): LdpClient() {
    private val host: InetAddress
    private val port: Int
    private var address: SocketAddress
    private lateinit var datagramChannel: DatagramChannel

    init {
        this.host = builder.host
        this.port = builder.port
        address = InetSocketAddress(this.host, this.port) //TODO nullable?
    }

    override fun start() {
        datagramChannel = bindChannel()
    }

    private fun bindChannel(): DatagramChannel {
        return openChannel().bind(address)
    }

    private fun openChannel(): DatagramChannel {
        datagramChannel = DatagramChannel.open()
        datagramChannel.configureBlocking(false)
        return datagramChannel
    }


    override fun send(request: LdpRequest): LdpResponse {
        if (!this::datagramChannel.isInitialized){
            throw IOException("Client must be started to open channel")
        }
        val req = LdpRequest.newBuilder(request)
            .uri(URI("udp://${host.hostName}:${port}"))
            .build()
        sendRequest(req)
        return receive()
    }


    private fun sendRequest(request: LdpRequest) {
        val packets = subdivideToPackets(request.serialize())
        for (packet in packets) {
            //TODO: check delivered result
            val result: Boolean = sendPacket(packet)
        }
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

    private fun sendPacket(packet: ByteArray): Boolean {
        val bufferedData = ByteBuffer.wrap(packet)
        try {
            datagramChannel.send(bufferedData, address)
        } catch (e: Exception) {
            println("datagramChannel problem")
            throw e
        }
        val (number, amount, res) = receiveResponse() //TODO: receive different amount of packets
        //TODO: ################## wait and send again ##################
        return res.status == LdpResponse.Status.OK
    }

    private fun receiveResponse(): Triple<Int, Int, LdpResponse> {
        val buffer = ByteBuffer.allocate(640) //used only 512
        val responseAddress: SocketAddress = datagramChannel.receive(buffer)
        return extractData(buffer)
    }

    private fun extractData(buffer: ByteBuffer): Triple<Int, Int, LdpResponse> {
        buffer.flip()
        val bytes = buffer.array()
        val packetNumber = bytes[0].toInt()
        val packetsAmount = bytes[1].toInt()
        val data = String(bytes.slice(2 until bytes.size).toByteArray())
        return Triple(packetNumber, packetsAmount, LdpResponse.deserialize(data))
    }

    //TODO: private?
    override fun receive(): LdpResponse {
        var data = ""
        do {
            val (num, amount, dataPart) = receiveResponse()
            data += dataPart
        } while (num < amount)
        return LdpResponse.deserialize(data)
    }

    override fun close() {
        if (!this::datagramChannel.isInitialized){
            throw IOException("Client must be started to open channel")
        }
        this.datagramChannel.close()
    }
}