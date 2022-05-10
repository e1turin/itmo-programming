package com.github.e1turin.lab5.client

import com.github.e1turin.lab5.common.application.CommandManager
import com.github.e1turin.lab5.common.util.IOStream
import java.io.BufferedReader
import java.io.BufferedWriter

fun main(args: Array<String>){
    val ioStream = IOStream(
        BufferedReader(System.`in`.reader()),
        BufferedWriter(System.out.writer()),
        interactive = true
    )
    val commandManager = CommandManager(
        //TODO commands
    )
    val client = Client(ioStream, commandManager)
    client.start(args)//TODO: args processing
}