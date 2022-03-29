package com.github.e1turin.lab5.common.containers


abstract class Message(
    val sender: String,
    val content: String
) : java.io.Serializable
