package com.github.e1turin.application

@kotlinx.serialization.Serializable
class Label (private var bands: Long){
    override fun toString(): String{
        return "Label(bands=$bands)"
    }
}