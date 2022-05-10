package com.github.e1turin.lab5.protocol

import java.net.*
import java.nio.ByteBuffer
import java.nio.channels.DatagramChannel

class LdpClient(val uri: String, val port: Int) {
    private val host: InetAddress
    private var address: SocketAddress
    private var datagramChannel: DatagramChannel

    init {
        try {
            host = InetAddress.getByName(uri)
        } catch (e: UnknownHostException) {
            throw e //Todo
        }
        address = InetSocketAddress(host, port)
        datagramChannel = DatagramChannel.open() //todo move to method
        datagramChannel.configureBlocking(false)
    }


    fun send(request: LdpRequest): LdpResponse {
        request.uri("$uri:$port")
        sendRequest(request)
        return receiveResponse()
    }

    private fun sendRequest(request: LdpRequest) {
        val requestDataByteArrays = subdividePackets(request.serialise())
        //todo: timeout, thread to send, thread to check answer
        /*
        if (result.status != LdpResponse.GOT ||
            result.getHeader("CMD-Name") != request.getHeader("CMD-Name")
        //|| middleRes.getHeader("Pkt-Order") != request.getHeader("Pkt-Order")
        ) { }
         */

        for (packet in requestDataByteArrays) {
//            val result: LdpResponse =
            sendPacket(packet)
        }
    }

    private fun sendPacket(packet: ByteArray) {
        val bufferedData = ByteBuffer.wrap(packet)
        datagramChannel.send(bufferedData, address)
    }

    private fun subdividePackets(data: String): Array<ByteArray> {
        //TODO subdivision
        return arrayOf(data.toByteArray())
    }


    private fun receiveResponse(): LdpResponse {

        val buffer = ByteBuffer.allocate(1024)
        val responseAddress: SocketAddress = datagramChannel.receive(buffer)
        //todo multi pocket response
        val data = String(buffer.array())
        val response = LdpResponse.deserialize(data)
        response.header(LdpMessage.HEADERS.FROM_ADDRESS, responseAddress.toString())
        return response

    }

    fun stop() {
        this.datagramChannel.close()
    }

}







