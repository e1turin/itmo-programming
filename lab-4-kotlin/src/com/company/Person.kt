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

class Person(name: String) : Alive(name) {
    inner class Face {
        private val feelings: MutableSet<Feeling>

        init {
            feelings = HashSet()
        }

        fun toReshape(feeling: Feeling): Action {
            feelings.add(feeling)
            return Action(
                "$name исказил лицо от чувства $feeling",
                Quality(
                    "Лицо исказилось"
                )
            )
        }
    }

    var face = Face()
    fun toDragIn(person: Person) {
        println(name + " втащил(а) " + person.name)
    }

    fun toClose(thing: Closable) {
        thing.close()
        println("$name закрыл(a)")
    }

    fun toReshapeFace(feeling: Feeling): Action {
        println("$name исказил(а) лицо от чувства $feeling")
        return face.toReshape(feeling)
    }

    fun toCommandTo(whom: Alive, cmd: Command): Action {
        println(name + " скомандовал(а) " + whom.name + ": " + cmd.description)
        return Action(
            name + " скомандовал(а) " + whom.name + ": " + cmd.description,
            Quality("Скомандовали: " + cmd.description)
        )
    }

    fun toHeal(whom: Alive): Action {
        println(name + " подлечил " + whom.name)
        return Action(
            name + " подлечил " + whom.name,
            Quality("Подлечили " + whom.name)
        )
    }

    fun toLookAt(thing: Thing): Action {
        println(name + " взглянул(а) на " + thing.name)
        return Action(
            name + " взглянул(а) на " + thing.name,
            Quality("Взглянули на " + thing.name)
        )
    }

    fun toThrowsToWith(whom: Alive, thing: Thing): Action {
        println(name + " бросился(лась) к " + whom.name + " с " + thing.name)
        return Action(
            name + " бросился(лась) к " + whom.name + " с " + thing.name,
            Quality("Идет помощь для " + whom.name)
        )
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val person = o as Person
        return name == person.name && decision == person.decision
    }

    override fun hashCode(): Int {
        return Objects.hash(name, decision)
    }
}