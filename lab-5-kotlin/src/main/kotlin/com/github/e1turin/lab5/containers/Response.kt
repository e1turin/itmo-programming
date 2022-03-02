package com.github.e1turin.lab5.containers


class Response(
    sender: String,
    val type: ResponseType,
    val arg: Any = "",
    content: String,
) : Message(sender, content) {

}