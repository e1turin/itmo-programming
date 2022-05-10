package com.github.e1turin.lab6.protocol.impl

import com.github.e1turin.lab6.protocol.api.LdpHeaders
import com.github.e1turin.lab6.protocol.api.LdpRequest
import java.net.URI

class LdpRequestImmutableImpl(builder: Builder) : LdpRequest() {
    private val headers: LdpHeaders
    private val uri: URI
    private val method: String

    init {
        headers = builder.headers()
        uri = builder.uri()
        method = builder.method()
    }

    override fun uri() = uri
    override fun method() = method
    override fun headers() = headers
    override fun serialize(): String {
        TODO("Not yet implemented")
    }


}
