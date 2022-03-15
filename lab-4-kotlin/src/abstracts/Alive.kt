package abstracts

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

abstract class Alive(var name: String) {
    var decision: Decision
        protected set

    init {
        decision = Decision("")
    }

    fun toLet(entity: Alive): Action {
        println(name + " пропустил(а) " + entity.name)
        return Action(
            name + " пропустил(а) кого-то по имени " + entity.name,
            Quality(
                "Пропустил(а)"
            )
        )
    }

    fun toClimbinto(thing: Thing): Action {
        return Action(name + "полез в" + thing.getName(), Quality("Полез"))
    }

    fun toFeel(feeling: Feeling) {
        println("$name почувствовал(а) $feeling")
    }

    fun toFeel(action: Action) {
        println(name + " почувствовал(а) как " + action.description)
    }

    fun toDecide(decision: String) {
        this.decision = Decision(decision)
        println("$name решил(а) $decision")
    }

    fun toSee(action: Action): String {
        println(name + " увидел(а) как " + action.description)
        return name + "увидел(а)" + action.description
    }

    fun toShout(cmd: String): Action {
        println("$name скомандавал(а) $cmd")
        return Action(
            "$name скомандавал(а) $cmd", Quality(
                "Команда $cmd"
            )
        )
    }

    fun toTake(thing: Thing): Action {
        println(name + " взял(а) " + thing.getName())
        return Action(
            name + " взял(а) " + thing.getName(),
            Quality("Взяли " + thing.getName())
        )
    }

    fun toHear(action: Action): Action {
        println(name + " услышал(а), как " + action.description)
        return Action(
            name + " услышал(а), как " + action.description,
            Quality("Услышали: " + action.description)
        )
    }

    override fun toString(): String {
        return name
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val alive = o as Alive
        return name == alive.name && decision == alive.decision
    }

    override fun hashCode(): Int {
        return Objects.hash(name, decision)
    }
}