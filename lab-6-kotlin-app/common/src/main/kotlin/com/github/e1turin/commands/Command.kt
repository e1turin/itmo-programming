package com.github.e1turin.commands

import com.github.e1turin.util.Manager

abstract class Command(
    val cmdName: String
) {
    abstract var description: String
//    abstract fun execute(arg: String, ioStream: IOStream): LdpRequest
    abstract fun execute(arg: String, respondent: Manager): Int
//    abstract fun handleResponse(response: LdpResponse, ioStream: IOStream): LdpResponse

    @JvmName("getDescription1")
    fun getDescription() = description
}
