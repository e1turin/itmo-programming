package com.github.e1turin.lab5.common.commands

import com.github.e1turin.lab5.common.containers.Message
import com.github.e1turin.lab5.common.containers.Response
import com.github.e1turin.lab5.common.util.IOStream

abstract class Command(
    //    protected val target: MusicBandStorage,
    val cmdName: String,
    //    protected var ioStream: IOStream,
    val description: String
) {
    abstract fun execute(arg: String, ioStream: IOStream): Message
    abstract fun getResponse(taskResponse: Response, ioStream: IOStream): Message

}
