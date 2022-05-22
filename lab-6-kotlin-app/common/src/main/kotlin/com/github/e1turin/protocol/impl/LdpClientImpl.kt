package com.github.e1turin.protocol.impl

import com.github.e1turin.protocol.api.*
import com.github.e1turin.protocol.exceptions.LdpConnectionException
import java.io.IOException
import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.ServerSocket
import java.net.SocketAddress
import java.net.URI
import java.nio.ByteBuffer
import java.nio.channels.DatagramChannel

internal class LdpClientImpl(builder: Builder) : LdpClient() {
    override var host: InetAddress = builder.host
        private set
    override var port: Int = if (builder.port < 0) {
        throw UninitializedPropertyAccessException(
            "property port has not been initialized correctly"
        )
    } else {
        builder.port
    }
        private set

    override var localHost: InetAddress = InetAddress.getLocalHost()
        //    override var localHost: InetAddress = InetAddress.getByName("0.0.0.0")
        private set
    override var localPort = -1
        private set
    override var timeout = builder.timeout
        private set
    private var address: SocketAddress = InetSocketAddress(this.host, this.port)
    private lateinit var datagramChannel: DatagramChannel
    override val isOpen: Boolean
        get() = if (this::datagramChannel.isInitialized) {
            datagramChannel.isOpen
        } else {
            throw LdpConnectionException("Connection not established")
        }

    override fun connect(): Int {
        if (!this::datagramChannel.isInitialized || !this.datagramChannel.isOpen) {
            val freePort: Int
            try {
                freePort = ServerSocket(0).localPort
            } catch (e: IOException) {
                System.err.println("No available ports for client")
                System.err.println(e.message)
                throw IOException("Could not find an available port")
            }
            datagramChannel = bindChannel(freePort)
            this.localPort = freePort
            return sendRequest(
                LdpRequest.newBuilder()
                    .method(LdpRequest.METHOD.GET)
                    .header(LdpHeaders.Headers.CMD_NAME, LdpHeaders.Values.Cmd.connect)
                    .build()
            )
//            LdpOptions.StatusCode.OK
            //TODO:connect // request
        } else {
            throw LdpConnectionException("Connection already established")
        }
    }

    override fun disconnect(): Int {
        if (this::datagramChannel.isInitialized && this.datagramChannel.isOpen) {
            val response = sendRequest(
                LdpRequest.newBuilder()
                    .method(LdpRequest.METHOD.GET)
                    .header(LdpHeaders.Headers.CMD_NAME, LdpHeaders.Values.Cmd.disconnect)
                    .build()
            )
            if (response == LdpOptions.StatusCode.OK) {
                close()
            }
            return response
//            LdpOptions.StatusCode.OK
            //TODO:connect // request
        } else {
            throw LdpConnectionException("Connection not established")
        }
//        return LdpOptions.StatusCode.OK
    }


    private fun bindChannel(port: Int): DatagramChannel {
        return openChannel().bind(InetSocketAddress(localHost, port))
    }

    private fun openChannel(): DatagramChannel {
        datagramChannel = DatagramChannel.open()
        datagramChannel.configureBlocking(false)
        return datagramChannel
    }


    override fun send(request: LdpRequest): LdpResponse {
        if (!this::datagramChannel.isInitialized) {
            throw LdpConnectionException("Connection not established")
        }
        val req = LdpRequest.newBuilder(request)
            .uri(URI("udp://${host.hostName}:${port}"))
            .build()
        sendRequest(req)
        return receive()
    }


    private fun sendRequest(request: LdpRequest): Int {
        val packets = subdivideToPackets(request.serialize())
        var response: LdpResponse
        var result = LdpOptions.StatusCode.FAIL
        for (packet in packets) {
            //TODO: check delivered result
            response = sendPacket(packet)
            result = response.status
        }
        return result
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

    private fun sendPacket(packet: ByteArray): LdpResponse {
        val bufferedData = ByteBuffer.wrap(packet)
        try {
            datagramChannel.send(bufferedData, address)
        } catch (e: Exception) {
            println("datagramChannel problem")
            throw e
        }
        return receive()
//        val res = receive() //TODO: receive different amount of packets
//        //TODO: ################## wait and send again ##################
//        return res.status == LdpOptions.StatusCode.OK
    }

    //TODO: private?
    override fun receive(): LdpResponse {
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
        return LdpResponse.deserialize(data.trim())
    }


    private fun receivePieceOfResponse(): Triple<Int, Int, String>? {
        val buffer = ByteBuffer.allocate(512) //used only 512
        val responseAddress: SocketAddress? = datagramChannel.receive(buffer)
        responseAddress?.let {
            return extractData(buffer)
        }
        return null
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
        if (!this::datagramChannel.isInitialized) {//todo: is it necessary?
            throw LdpConnectionException("Connection not established")
        }
        this.datagramChannel.close()
    }
}