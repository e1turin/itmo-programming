package com.github.e1turin.lab5.server

import com.github.e1turin.lab5.common.util.IOStream
import java.io.BufferedReader
import java.io.BufferedWriter

fun main(args: Array<String>) {
    val ioStream = IOStream(
        BufferedReader(System.`in`.reader()),//TODO: socket reader
        BufferedWriter(System.out.writer()),//TODO: socket writer
        interactive = true
    )
    val server = Server(ioStream)
    server.startServer(args)//TODO: args processing take out from class
}