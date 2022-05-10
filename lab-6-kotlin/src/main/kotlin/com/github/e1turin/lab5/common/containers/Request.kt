package com.github.e1turin.lab5.common.containers

class Request(
    sender: String,
    val type: RequestType,
    val task: String,
    content: String,
    val arg: Any = ""
) : Message(sender, content)
