package com.github.e1turin.protocol.impl

import com.github.e1turin.protocol.api.LdpHeaders
import com.github.e1turin.protocol.api.LdpRequest
import java.net.URI

internal class LdpRequestBuilderImpl() : LdpRequest.Builder {
    override var headers: LdpHeaders = LdpHeaders()
        private set
    override var uri: URI = URI("")
        private set
    override var method: String = LdpRequest.METHOD.GET
        private set

    constructor(request: LdpRequest) : this() {
        this.headers = request.headers
//        this.uri = request.uri
        this.method = request.method
    }

    override fun build(): LdpRequest {
        return LdpRequestImmutableImpl(this)
    }

    override fun header(name: String, value: String): LdpRequestBuilderImpl {
        headers.add(name, value)
        return this
    }

//    override fun uri(): URI = uri
    fun checkURI(): Boolean {
        TODO("Not yet implemented")
    }

    override fun uri(uri: URI): LdpRequestBuilderImpl {
        this.uri = uri
        return this
    }

//    override fun headers(): LdpHeaders {
//        return headers
//    }

    override fun method(method: String): LdpRequestBuilderImpl{
        this.method = method
        return this
    }

//    override fun method() = method

//    override fun PUT() {
//        method = LdpRequest.METHOD.PUT
//    }

    override fun POST() {
        method = LdpRequest.METHOD.POST
    }

    override fun GET() {
        method = LdpRequest.METHOD.GET
    }

}
