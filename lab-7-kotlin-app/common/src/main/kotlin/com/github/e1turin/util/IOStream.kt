package com.github.e1turin.util

import com.github.e1turin.app.Coordinates
import com.github.e1turin.app.Label
import com.github.e1turin.app.MusicBand
import com.github.e1turin.app.MusicGenre
import com.github.e1turin.exceptions.InvalidCmdArgumentException
import kotlinx.datetime.LocalDate
//import com.github.e1turin.util.DateValidator
import java.io.IOException
import java.io.Reader
import java.io.Writer
import java.text.SimpleDateFormat
import java.util.*

class IOStream(
    reader: Reader,
    val writer: Writer,
    var interactive: Boolean
) {
    private val scanner: Scanner

    init {
        scanner = Scanner(reader)
    }

    fun write(message: String) {
        try {
            writer.write(message)
            writer.flush()
        } catch (e: IOException) {
            System.err.println("<<Ошибка вывода>>")
        }
    }

    fun writeln(message: String = "") = write(message + "\n")

    fun writetermsep(sep: String = ">>>") = write("$sep ")

    fun <T> termInput(
        sep: String = ">>>",
        message: String = "",
        query: () -> T?
    ): T? {
        if (message.isNotEmpty()) writeln(message)
        writetermsep(sep)
//        while (true) {
//            if (canRead()) {
        return query()
//            }
//        }
    }

    fun <T> termInputUntil(
        sep: String = "",
        message: String = "",
        hint: String = "",
        condition: (T?) -> Boolean,
        query: () -> T?
    ): T? {
        var response: T? = termInput(sep, message, query)
        while (interactive && !condition(response)) {
            if (hint.isNotEmpty()) writeln(hint)
            response = termInput(sep, message, query)
        }
        return response
    }


    fun canRead(): Boolean = scanner.hasNext();
    fun canReadLine(): Boolean = scanner.hasNextLine();
    fun yesAnswer(): Boolean {
        return arrayOf("yes", "y", "Yes", "Y").contains(readLineWord())
    }


    /**
     * Bad to use, as leaves trailing '\n', should be used wish readLine()
     */
    fun read(): String = scanner.next()
    fun readLine(): String = scanner.nextLine()
    fun readLineWord(): String = readLine().trim()

    fun readNotBlankLineOrNull(): String? {
        val input = readLine()
        return input.ifBlank {
            null
        }
    }


    fun readLineIntOrNull(): Int? {
        return readLine().trim().toIntOrNull()
//        return if (scanner.hasNextInt()) {
//            scanner.nextInt()
//        } else {
//            scanner.next()
////            readLine()
//            null
//        }
    }

    fun readLineLongOrNull(): Long? {
        return readLine().trim().toLongOrNull()
//        return if (scanner.hasNextLong()) {
//            scanner.nextLong()
//        } else {
//            scanner.next()
//            null
//        }
    }

    fun readLineDoubleOrNull(): Double? {
        return readLine().trim().toDoubleOrNull()
//        return if (scanner.hasNextDouble()) {
//            scanner.nextDouble()
//        } else {
//            scanner.next()
//            null
//        }
    }

    private fun readCoordinates(message: String): Coordinates { //TODO: rename to readPair
        writeln(message)
        val x: Double = termInputUntil(
            sep = "x=",
            hint = "Не верное значение! Должна быть десятичная дробь или целое число (Double)",
            condition = { it != null },
            query = { readLineDoubleOrNull() }
        ) ?: throw InvalidCmdArgumentException("reading Coordinates: x")
        val y: Double = termInputUntil(
            sep = "y=",
            hint = "Не верное значение! Должна быть десятичная дробь или целое число (Double)",
            condition = { it != null },
            query = { readLineDoubleOrNull() }
        ) ?: throw InvalidCmdArgumentException("reading Coordinates: y")
        return Coordinates(x, y)
    }

    private fun readMusicGenre(message: String): MusicGenre { //TODO: rename to readEnum
        writeln(message)
        val musicGenreName: String = termInputUntil(
            sep = "genre =",
            hint = "Не верное значение! Должно быть одно из следующих: " +
                    MusicGenre.values().map { it.name },
            condition = {
                it != null && it.uppercase() in MusicGenre.values().map { value -> value.name }
            },
            query = { readNotBlankLineOrNull() }
        )?.uppercase() ?: throw InvalidCmdArgumentException("reading Music Genre: blank value")
        if (musicGenreName !in MusicGenre.values()
                .map { value -> value.name }
        ) throw InvalidCmdArgumentException("reading Music Genre: wrong value")
        return MusicGenre.valueOf(musicGenreName)
    }

    private fun readLabel(message: String = ""): Label {
        writeln(message)
        return Label(termInputUntil(
            sep = "number =",
            hint = "Неверное значение! Должно быть число (long)!",
            condition = { it != null },
            query = { readLineLongOrNull() }
        ) ?: throw InvalidCmdArgumentException("reading Label")
        )
    }

    private fun parseDate(format: String, date: String): Date =
        SimpleDateFormat(format).parse(date)

    /*
    private fun readEstablishmentDate(message: String = "", format: String): Date? {
        writeln("$message (формат: $format или пусто)")
        readLine() // F*CKING HACK: next scanner.nextLine() doesn't work correctly without it!!!
        val dateString: String = termInputUntil(
            sep = "date =",
            hint = "Не верный формат даты! Должен быть следующий: $format",
            condition = {it=="" || DateValidator(format).isValid(it) },
            query = { readLine().trim()}
//            query = { if(canRead()) readLine() else "non-$format-non" }
        )!!
//        writeln("kek")
//        writeln(dateString)
//        writeln("kek")
        if (!interactive && !DateValidator(format).isValid(dateString))
            throw InvalidCmdArgumentException(
                "reading EstablishmentDate: wrong format for date '$dateString'"
            )
        return if(dateString=="") null else parseDate(format, dateString)
    }

     */

    fun readMusicBand(message: String = ""): MusicBand {
        writeln(message)
        val name: String = termInputUntil(
            sep = "name =",
            message = "Введите имя группы (не более 50 символов)",
            hint = "Имя группы не может быть пустым!",
            condition = { it != null && it.isNotBlank() && it.length<=50},
            query = { readNotBlankLineOrNull() }
        ) ?: throw InvalidCmdArgumentException("reading Music Band: name")
        val coordinates: Coordinates = readCoordinates("Введите координаты, значение coordinates")
        val numberOfParticipants: Int = termInputUntil(
            sep = "number =",
            message = "Введите количество членов группы",
            hint = "Не верное значение! Число должно быть от 1, значение numberOfParticipants",
            condition = { it != null && it > 0 },
            query = { readLineIntOrNull() }
        ) ?: throw InvalidCmdArgumentException("reading Music Band: numberOfParticipants")
        val albumsCount: Long = termInputUntil(
            sep = "albumsCount =",
            message = "Введите количество альбомов группы, значение albumsCount",
            hint = "Не верное значение! Число должно быть от 1",
            condition = { it != null && it > 0 },
            query = { readLineLongOrNull() }
        ) ?: throw InvalidCmdArgumentException("reading Music Band: albumsCount")


        val establishmentDate: LocalDate? = null
        /*
        val establishmentDate: Date? = readEstablishmentDate(
            message = "Введите дату образования группы",
            format = "dd.MM.yyyy"
        )

         */
        val genre: MusicGenre = readMusicGenre("Введите жанр музыки, значение genre ${
            MusicGenre
                .values().map { it.name }
        }"
        )
        val label: Label = readLabel("Введите значение 'Label'")

        return MusicBand(
            name, coordinates, numberOfParticipants, albumsCount,
            establishmentDate, genre, label
        )
    }


    fun readFileName(message: String): String = TODO()

    fun readCmdWithArg(): Pair<String, String> {
        val cmdAndArg: List<String> = readLine().trim().split(" ", limit = 2)
        val cmd = if (cmdAndArg.isNotEmpty()) cmdAndArg[0] else ""
        val arg = if (cmdAndArg.size > 1) cmdAndArg[1] else ""
        return Pair(cmd, arg)
    }
}

