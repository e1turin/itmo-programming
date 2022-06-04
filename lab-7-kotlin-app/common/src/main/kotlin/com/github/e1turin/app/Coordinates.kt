package com.github.e1turin.app

@kotlinx.serialization.Serializable
data class Coordinates(val x: Double, val y: Double){
    override fun toString(): String {
        return "Coordinates(x=$x, y=$y)"
    }
}