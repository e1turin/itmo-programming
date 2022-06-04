package com.github.e1turin.app

@kotlinx.serialization.Serializable
class Label (val bands: Long){
    override fun toString(): String{
        return "Label(bands=$bands)"
    }
}