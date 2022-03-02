package com.github.e1turin.lab5.containers

import com.github.e1turin.lab5.util.IOStream

interface Requestable {
    fun exec(taskResponse: Response, ioStream: IOStream): Message
}
