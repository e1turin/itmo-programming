package com.github.e1turin

import com.github.e1turin.app.CommandManagerFactory
import com.github.e1turin.util.IOStream
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.IOException
import java.net.InetAddress

fun main(args: Array<String>) {
    //todo: process args

    var serverHost: String = if (args.isNotEmpty()) {
        args[0]
    } else {
        throw UninitializedPropertyAccessException("Host name mast be the 1st argument")
    }

    val serverPort: Int = if (args.size > 1) {
        try {
            args[1].toInt()
        } catch (e: java.lang.NumberFormatException) {
            throw IOException("Wrong port id")
        }
    } else {
        35047
    }


    val ioStream = IOStream(
        BufferedReader(System.`in`.reader()),
        BufferedWriter(System.out.writer()),
        interactive = true
    )
    val commandManager = CommandManagerFactory.getInstance().apply {
            setStdIO(ioStream)
        }

    val client = Client(ioStream, commandManager, serverPort, InetAddress.getByName(serverHost))
    client.start(args)
}


