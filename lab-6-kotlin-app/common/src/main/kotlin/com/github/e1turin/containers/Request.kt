package com.github.e1turin.containers

class Request(
    sender: String,
    val type: RequestType,
    val task: String,
    content: String,
    val arg: Any = ""
) : Message(sender, content)
