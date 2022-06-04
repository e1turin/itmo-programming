package com.github.e1turin.util

import java.security.MessageDigest

internal fun String.md5(): String {
    return hashString(this, "MD5")
}

internal fun String.sha256(): String {
    return hashString(this, "SHA-256")
}

private fun hashString(input: String, algorithm: String): String {
    return MessageDigest
        .getInstance(algorithm)
        .digest(input.toByteArray())
        .fold("") { str, it -> str + "%02x".format(it) }
}