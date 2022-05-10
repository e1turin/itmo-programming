package com.github.e1turin.lab5.common.commands

import com.github.e1turin.lab5.common.util.IOStream
import com.github.e1turin.lab5.protocol.LdpRequest
import com.github.e1turin.lab5.protocol.LdpResponse

abstract class Command(
    val cmdName: String
) {
    protected var description: String = ""
    abstract fun execute(arg: String, ioStream: IOStream): LdpRequest
    abstract fun handleResponse(response: LdpResponse, ioStream: IOStream): LdpResponse

    @JvmName("getDescription1")
    fun getDescription() = description
}
