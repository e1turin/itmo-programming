package com.company

import com.company.Sound
import com.company.Quality
import abstracts.Thing
import abstracts.Closable
import abstracts.Alive
import kotlin.Throws
import exc.IncorrectFileNameException
import kotlin.jvm.JvmStatic
import com.company.Inputter
import com.company.Shorty
import exc.NogivenNameException
import com.company.Person
import com.company.things.Rocket
import com.company.things.Shots
import com.company.things.Bullets
import com.company.things.Something
import com.company.things.ShirtSleev
import com.company.things.Bloodstain
import com.company.things.Door
import com.company.Policeman
import com.company.things.Healpack
import types.Feeling
import java.lang.ArrayIndexOutOfBoundsException
import java.io.File
import java.io.FileNotFoundException
import java.util.HashSet
import com.company.Person.Face
import java.util.Objects
import com.company.Decision
import abstracts.Paintable

class Policeman(name: String) : Alive(name) {
    private override val name: String? = null
    private override val decision: Decision? = null
    fun toShoot(): Action {
        return Action("$name стреляет(ют) ", Quality("Стрельба"))
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val shorty = o as Policeman
        return name == shorty.name && decision == shorty.decision
    }

    override fun hashCode(): Int {
        return Objects.hash(name, decision)
    }
}