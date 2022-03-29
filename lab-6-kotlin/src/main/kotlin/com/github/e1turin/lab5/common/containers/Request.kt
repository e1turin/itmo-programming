package com.github.e1turin.lab5.common.containers

import com.github.e1turin.lab5.common.collection.StorageManager

class Request(
    sender: String,
    val type: RequestType,
    val task: String,
    content: String,
    val arg: Any = ""
) : Message(sender, content) {

}
