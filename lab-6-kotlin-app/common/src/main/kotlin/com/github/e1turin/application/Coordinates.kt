package com.github.e1turin.application

@kotlinx.serialization.Serializable
class Coordinates(private var x: Double, private var y: Double){
    override fun toString(): String {
        return "Coordinates(x=$x, y=$y)"
    }
}