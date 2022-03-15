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
import com.company.Person.Face
import com.company.Decision
import abstracts.Paintable
import java.util.*

object Main {
    @Throws(IncorrectFileNameException::class)
    @JvmStatic
    fun main(args: Array<String>) {
        val theFucsiaName: String
        try {
            theFucsiaName = Inputter.inoutFileName("C:/names.txt")
        } catch (e: IncorrectFileNameException) {
            println("Не верное имя файла, файл не обнаружен")
        }
        val theShortyes: Shorty
        theShortyes = try {
            Shorty(Inputter.inputName(args))
        } catch (e: NogivenNameException) {
            println("*Имя коротышек установлено по-умолчанию*")
            Shorty("Коротышки")
        }
        val theFucsia = Person("Фуксия")
        val theSeledochks = Person("Селедочка")
        val rocket = Rocket(Sound(""), Quality(""))
        val shots = Shots(Sound("Выстрелы"), Quality("Страшные"))
        val bullets = Bullets(
            Sound("Свист"), Quality(
                "Опасные " +
                        "вокруг"
            )
        )
        val theKlepka = Person("Клёпка")
        val something = Something(
            Sound(""), Quality(
                "Обжигающее"
            )
        )
        val theSmarty = Person("Знайка")
        val shirtSleev = ShirtSleev(Sound(""), Quality(""))
        val bloodstain = Bloodstain(Sound(""), Quality(""))
        val door = Door(Sound("Закрываение"), Quality(""))
        val theRigle = Policeman("Ригль")
        val theDocPilulckin = Person("Доктор Пилюлькин")
        val healpack = Healpack(Sound(""), Quality(""))
        val bullet = Bullets(Sound("свист пули"), Quality(""))
        val thePolicemen = Policeman("Полицейские")
        theSmarty.toSee(theRigle.toShout("Полицейские, ружья на изготовку"))
        theSmarty.toCommandTo(theShortyes, Command("забираться в ракету"))

        //
        //
        theSmarty.toDecide("сесть в ракету")
        theShortyes.toLet(theFucsia)
        theShortyes.toLet(theSeledochks)
        theShortyes.toClimbinto(rocket)
        shots.toSound()
        bullets.toSound()
        val act = something.toBurn(theKlepka, "руку")
        theKlepka.toFeel(act!!)
        theSmarty.toSee(theKlepka.toReshapeFace(Feeling.PAIN))
        theSmarty.toSee(bloodstain.toAppearOn(shirtSleev))
        theSmarty.toDragIn(theKlepka)
        print("не теряя ни секунды ")
        theSmarty.toClose {
            door.close()
            println("Закрыли дверь")
        } //door);
        //
        //
        theDocPilulckin.toSee(act)
        theDocPilulckin.toThrowsToWith(theKlepka, healpack)
        theDocPilulckin.toSee(
            Action(
                "Пуля прошла навылет",
                Quality("Не смертельно")
            )
        )
        theDocPilulckin.toHeal(theKlepka)
        theKlepka.toFeel(Feeling.PAIN)
        theSmarty.toHear(
            Action(
                bullets.name + " барабанят по оболочке " + rocket.name,
                Quality("Опасный звук")
            )
        )
        thePolicemen.toShoot()
    }
}

internal object Inputter {
    @Throws(NogivenNameException::class)
    fun inputName(args: Array<String>): String {
        return try {
            args[0]
        } catch (e: ArrayIndexOutOfBoundsException) {
            throw NogivenNameException(
                "не передано имя", "введите имя в " +
                        "качестве аргумента командной строки", e
            )
        }
    }

    fun isCorrectFileName(path: String): Boolean {
        return path == "C:/meme.txt"
    }

    @Throws(IncorrectFileNameException::class)
    fun inoutFileName(path: String): String {
        try {
            Scanner(File(path)).use { file -> if (file.hasNextLine()) return file.nextLine() }
        } catch (e: FileNotFoundException) {
            if (!isCorrectFileName("C:/names.txt")) {
                throw IncorrectFileNameException(
                    "Incorrect filename : " + "C" +
                            ":/names.txt"
                )
            }
            //...
        }
        return path
    }
}