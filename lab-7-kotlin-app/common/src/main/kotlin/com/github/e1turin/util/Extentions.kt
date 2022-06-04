package com.github.e1turin.util

import com.github.e1turin.app.MusicBand
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

fun MusicBand.serialize(): String {
    return Json.encodeToString(this)
}