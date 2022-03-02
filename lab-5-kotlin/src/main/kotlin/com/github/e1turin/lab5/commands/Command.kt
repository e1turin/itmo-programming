package com.github.e1turin.lab5.commands

import com.github.e1turin.lab5.collection.MusicBandStorage
import com.github.e1turin.lab5.containers.Message
import com.github.e1turin.lab5.util.IOStream

abstract class Command(
    //    protected val target: MusicBandStorage,
    val cmdName: String,
    //    protected var ioStream: IOStream,
    val description: String
) {

    abstract fun exec(arg: String, target: MusicBandStorage, ioStream: IOStream): Message// Message help(); -> ??? description

}
