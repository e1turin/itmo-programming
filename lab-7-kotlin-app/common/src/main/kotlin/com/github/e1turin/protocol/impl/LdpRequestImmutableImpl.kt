package com.github.e1turin.protocol.impl

import com.github.e1turin.protocol.api.LdpHeaders
import com.github.e1turin.protocol.api.LdpRequest
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.net.URI

@Serializable
internal class LdpRequestImmutableImpl private constructor(
    override val headers: LdpHeaders,
//    @Contextual override val uri: URI,
    override val method: String
) : LdpRequest() {

    constructor(builder: Builder): this(
        headers = builder.headers,
//        uri = builder.uri,
        method = builder.method
    )


//    override fun uri() = uri
//    override fun method() = method
//    override fun headers() = headers
    override fun serialize(): String {
        val str = Json.encodeToString(this)
//    println("serialized req: '$str'")
    return str
    }


}
