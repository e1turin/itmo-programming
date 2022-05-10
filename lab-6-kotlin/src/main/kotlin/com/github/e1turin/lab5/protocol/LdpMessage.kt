package com.github.e1turin.lab5.protocol


abstract class LdpMessage() : java.io.Serializable {
    val headers: LdpHeaders = LdpHeaders()

    object HEADERS {
        const val UNDEFINED = "Undefined"
        const val CMD_NAME = "CMD"
        const val URI_NAME = "URI"
        const val FROM_ADDRESS = "From"
        const val REQUEST = "Request"
        const val RESPONSE = "Response"
    }


    fun setHeader(header: String, value: String) {
        this.headers.set(header, value)
    }

    fun hasHeader(header: String): Boolean {
        return this.headers.containsKey(header)
    }

    @Throws(IllegalArgumentException::class)
    fun getHeader(header: String): String {
        return this.headers.get(header)
    }

//    fun headers() = this.headers
//    fun owner() = this.owner

    override fun toString(): String {
        return "${this.javaClass.simpleName}(data=${headers})"
    }


}



