package com.github.e1turin.containers


class Response(
    sender: String,
    val status: ResponseStatus,
    val arg: Any = "",
    content: String,
) : Message(sender, content) {

}