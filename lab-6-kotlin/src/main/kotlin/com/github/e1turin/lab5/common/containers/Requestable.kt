package com.github.e1turin.lab5.common.containers

import com.github.e1turin.lab5.common.util.IOStream

interface Requestable {
    fun getResponse(taskResponse: Response, ioStream: IOStream): Message
}
