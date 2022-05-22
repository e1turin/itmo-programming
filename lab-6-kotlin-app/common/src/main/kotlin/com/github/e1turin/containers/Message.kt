package com.github.e1turin.containers


abstract class Message(
    val sender: String,
    val content: String
) : java.io.Serializable
