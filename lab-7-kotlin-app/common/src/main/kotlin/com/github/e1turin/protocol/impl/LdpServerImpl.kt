package com.github.e1turin.protocol.impl

import com.github.e1turin.protocol.api.LdpOptions
import com.github.e1turin.protocol.api.LdpRequest
import com.github.e1turin.protocol.api.LdpResponse
import com.github.e1turin.protocol.api.LdpServer
import com.github.e1turin.protocol.exceptions.LdpConnectionException
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import java.net.*
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
    private val requestChannel = Channel<Pair<LdpRequest, SocketAddress>>()

    init {
        this.localPort = builder.localPort
        this.timeout = builder.timeout
        println("hostname: $localHost")
    }

    override suspend fun start(): Unit = coroutineScope {
        try {
            datagramChannel = bindChannel(localPort)
            println("Using port 35047")
        } catch (e: BindException) {
            localPort = withContext(Dispatchers.IO) {
                ServerSocket(0).localPort
            }
            datagramChannel = bindChannel(localPort)
            println("Port 35047 is not free, using port: $localPort")
        }

        launch(Dispatchers.IO) {
            receiveRequestLoop()
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

    override suspend fun receiveRequest(): Pair<LdpRequest, SocketAddress> {
        return requestChannel.receive()
    }

    private suspend fun receiveRequestLoop() = coroutineScope {
        var num: Int = 0
        var amount: Int = 1
        var dataPart: String = ""
        var address: SocketAddress? = null
        while (isActive) {
            val res = receivePieceOfRequest() ?: continue
            val numAmountData = res.first
            num = numAmountData.first
            amount = numAmountData.second
//            dataPart += numAmountData.third
            address = res.second
            if (connections[address] == null) {
                connections[address] = mutableMapOf()
            }
            connections[address]!![num] = numAmountData.third
            if ((connections[address]?.size ?: -1) == amount) {
                launch(Dispatchers.Default) {
                    collectRequest(address)
                }
            }
        }
    }

    private suspend fun collectRequest(address: SocketAddress) {
        val dataParts = connections[address]?.values?.toList() ?: return
        connections[address]?.clear() //TODO: timeout to clear connections
        var data = ""
        for (dt in dataParts) {
            data += dt
        }
//        connections.remove(address)
        try {
            requestChannel.send(LdpRequest.deserialize(data.trim()) to address)
        } catch (e: Exception){
            e.printStackTrace()
        }
    }

    private suspend fun receivePieceOfRequest(): Pair<Triple<Int, Int, String>, SocketAddress>? {
        val buffer = ByteBuffer.allocate(512)
        val responseAddress: SocketAddress? =
            withContext(Dispatchers.IO) {
                datagramChannel.receive(buffer)
            }

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

    override suspend fun send(response: LdpResponse, address: SocketAddress): LdpResponse? =
        coroutineScope {
            if (!this@LdpServerImpl::datagramChannel.isInitialized) {
                throw LdpConnectionException("Connection not established")
            }
            //TODO:verifying response
//        val response = LdpResponse(LdpOptions.StatusCode.OK)
            sendResponse(response, address)
//        return receiveResponse()
            return@coroutineScope null
        }

    private suspend fun sendResponse(response: LdpResponse, address: SocketAddress) =
        coroutineScope {
            val packets = subdivideToPackets(response.serialize())
            for (packet in packets) {
                val result: Boolean = sendPacket(packet, address)
            }
        }

    override suspend fun receiveResponse(): LdpResponse = coroutineScope{
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
        return@coroutineScope LdpResponse.deserialize(data.trim())
    }

    private suspend fun receivePieceOfResponse(): Triple<Int, Int, String>? {
        val buffer = ByteBuffer.allocate(512) //used only 512
        val responseAddress: SocketAddress? =
            withContext(Dispatchers.IO) {
                datagramChannel.receive(buffer)
            }
        responseAddress?.let {
            //TODO: SocketAddress return?
            return extractData(buffer)
        }
        return null
    }


    private suspend fun subdivideToPackets(data: String): Array<ByteArray> {
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

    private suspend fun sendPacket(packet: ByteArray, address: SocketAddress): Boolean {
        val bufferedData = ByteBuffer.wrap(packet)
        try {
            withContext(Dispatchers.IO) {
                datagramChannel.send(bufferedData, address)
            }
        } catch (e: Exception) {
            println("datagramChannel problem")
            throw e
        }
//        val res = receiveResponse() //TODO: receive different amount of packets
        //TODO: ################## wait and send again ##################
        return LdpOptions.StatusCode.OK == LdpOptions.StatusCode.OK
    }

    private suspend fun extractData(buffer: ByteBuffer): Triple<Int, Int, String> {
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