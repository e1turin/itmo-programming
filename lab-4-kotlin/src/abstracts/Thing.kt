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
import types.Color

abstract class Thing(
    var name: String,
    var sound: Sound,
    var quality: Quality,
    var color: Color
) : Paintable {

    override fun paint(color: Color) {
        println("$name теперь цвета $color")
        this.color = color
    }

    fun toSound() {
        print("$name: ")
        sound.sound()
    }

    fun toAppearOn(thing: Thing): Action {
        return Action(
            name + " цвета " + color + " появилось на " + thing.name +
                    " " + "цвета " + thing.color,
            Quality("Появление")
        )
    }

    fun toAct(name: String): Action {
        return Action(name, quality)
    }

    override fun toString(): String {
        return name
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val thing = o as Thing
        return name == thing.name && sound == thing.sound && quality == thing.quality && color == thing.color
    }

    override fun hashCode(): Int {
        return Objects.hash(name, sound, quality, color)
    }
}