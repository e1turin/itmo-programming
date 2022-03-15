package com.company.things

import com.company.Sound
import com.company.Quality
import abstracts.Thing
import abstracts.Closable
import types.Color

class Door(sound: Sound, quality: Quality) :
    Thing("Дверь", sound, quality, Color.UNDEFINED), Closable {
    override fun close() {
        class Knock : Sound("Стук")

        val knock = Knock()
        print("Дверь " + color + "цвета закрылась. ")
        knock.sound()
    }
}