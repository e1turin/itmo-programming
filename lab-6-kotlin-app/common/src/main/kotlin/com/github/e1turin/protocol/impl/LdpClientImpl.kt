package com.github.e1turin.protocol.impl

import com.github.e1turin.protocol.api.*
import com.github.e1turin.protocol.exceptions.LdpConnectionException
import java.io.IOException
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.ServerSocket
import java.net.SocketAddress
import java.net.SocketTimeoutException
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

    //    override var localHost: InetAddress = InetAddress.getLocalHost()
    override var localHost: InetAddress = InetAddress.getByName("0.0.0.0")
        private set
    override var localPort = -1
        private set
    override var timeout: Int = builder.timeout
        private set
    private var address: SocketAddress = InetSocketAddress(this.host, this.port)
    private lateinit var datagramChannel: DatagramChannel
    private lateinit var datagramSocket: DatagramSocket
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
//            datagramSocket = datagramChannel.socket().apply { soTimeout = timeout }
            this.localPort = freePort
            try {
                return send(
                    LdpRequest.newBuilder().method(LdpRequest.METHOD.GET)
                        .header(LdpHeaders.Headers.CMD_NAME, LdpHeaders.Values.Cmd.connect).build()
                ).status
            } catch (e: Exception) {
                close()
                throw e
            }
//            LdpOptions.StatusCode.OK
            //TODO:connect // request
        } else {
            throw LdpConnectionException("Connection already established")
        }
    }

    override fun disconnect(): Int {
        if (this::datagramChannel.isInitialized && this.datagramChannel.isOpen) {
            val responseStatus = sendRequest(
                LdpRequest.newBuilder().method(LdpRequest.METHOD.GET)
                    .header(LdpHeaders.Headers.CMD_NAME, LdpHeaders.Values.Cmd.disconnect).build()
            )
            close()
//            if (response == LdpOptions.StatusCode.OK) {
//            }
            return responseStatus
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
        datagramChannel.configureBlocking(true)
        return datagramChannel
    }


    override fun send(request: LdpRequest): LdpResponse {
        if (!this::datagramChannel.isInitialized) {
            throw LdpConnectionException("Connection not established")
        }
        val req = LdpRequest.newBuilder(request).uri(URI("udp://${host.hostName}:${port}")).build()
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
            if (result == LdpOptions.StatusCode.FAIL) {
                break
            }
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
        return try {
            datagramChannel.send(bufferedData, address)
            receive()
//        } catch (e: SocketTimeoutException) {
//            LdpResponse(LdpOptions.StatusCode.FAIL, body = "err: ${e.message}")
        } catch (e: Exception) {
            println("datagramChannel problem (todo: delete this msg)")
            throw e
//            LdpResponse(LdpOptions.StatusCode.FAIL, body = "err: ${e.message}")
        }
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
        /*
        val selector: Selector = Selector.open()
        datagramChannel.register(selector, SelectionKey.OP_READ)
        if (selector.select(timeout) == 0) {
            return null
        }
            for (key in selector.selectedKeys()) {
                if (key.isReadable) {
                    val buffer = ByteBuffer.allocate(512) //used only 512
                    val responseAddress: SocketAddress? = datagramChannel.receive(buffer)
//        val responseAddress: SocketAddress? = datagramSocket.receive(buffer)
                    responseAddress?.let {
                        return extractData(buffer)
                    }

                }

            }
         */


//        val buffer = ByteBuffer.allocate(512) //used only 512
        var buf = ByteArray(512)
        val datagramPacket = DatagramPacket(buf, 512).apply {
            socketAddress = this@LdpClientImpl.address
        }
        val socket = datagramChannel.socket().apply {
            soTimeout = timeout
        }
        socket.receive(datagramPacket)
//        val responseAddress: SocketAddress? = datagramPacket.socketAddress
//        val responseAddress: SocketAddress? = datagramSocket.receive(buffer)
//        responseAddress?.let {
//            return extractData(buffer)
        val byteBuf = ByteBuffer.wrap(buf, 0, datagramPacket.length)
        return extractData(byteBuf, false)
//        }
//        return null
    }

    private fun extractData(buffer: ByteBuffer, flip: Boolean = true): Triple<Int, Int, String> {
//        (buffer as Buffer).flip()// crutch for java 11+ -> java 8
        if (flip) {
            buffer.flip()
        }
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
        if (isOpen) {
            this.datagramChannel.close()
        }
    }
}