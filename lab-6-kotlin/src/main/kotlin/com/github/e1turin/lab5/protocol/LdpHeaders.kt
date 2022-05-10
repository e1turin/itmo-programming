package com.github.e1turin.lab5.protocol

class LdpHeaders {
    private val headers = mutableMapOf<String, String>()

    fun set(header: String, value: String){
        headers[header] = value
    }

    fun containsKey(header: String): Boolean {
        return headers.containsKey(header)
    }

    @Throws(IllegalArgumentException::class)
    fun get(header: String): String {
        return headers[header] ?: throw IllegalArgumentException("No such header")
    }

}