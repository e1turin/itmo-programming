package com.github.e1turin.lab5.common.containers


class Response(
    sender: String,
    val type: ResponseType,
    val arg: Any = "",
    content: String,
) : Message(sender, content) {

}