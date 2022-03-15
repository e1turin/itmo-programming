package com.company.things

import abstracts.Thing
import abstracts.Closable
import abstracts.Alive
import kotlin.Throws
import exc.IncorrectFileNameException
import kotlin.jvm.JvmStatic
import exc.NogivenNameException
import com.company.things.Rocket
import com.company.things.Shots
import com.company.things.Bullets
import com.company.things.Something
import com.company.things.ShirtSleev
import com.company.things.Bloodstain
import com.company.things.Door
import com.company.things.Healpack
import types.Feeling
import java.lang.ArrayIndexOutOfBoundsException
import java.io.File
import java.io.FileNotFoundException
import java.util.HashSet
import com.company.Person.Face
import java.util.Objects
import abstracts.Paintable
import com.company.*
import types.Color

class Something(sound: Sound, quality: Quality) :
    Thing("Что-то", sound, quality, Color.UNDEFINED) {
    fun toBurn(whom: Alive, what: String): Action {
        println()
        return Action(
            name + " обожгло " + whom.name + " а именно: " + what,
            Quality("Больно")
        )
    }
}