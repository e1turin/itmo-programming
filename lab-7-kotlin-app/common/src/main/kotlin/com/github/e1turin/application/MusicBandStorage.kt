package com.github.e1turin.application

import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
//import java.time.LocalDate
import kotlin.collections.LinkedHashSet

@kotlinx.serialization.Serializable
open class MusicBandStorage(val name: String = "storage.json") { //TODO: generic

    private val data: LinkedHashSet<MusicBand> = java.util.LinkedHashSet()
    var creationDate: LocalDate =
        kotlinx.datetime.Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date

    val size: Int
        get() = data.size

    var lastElementId: Int = 0
        private set

    val info: String
        get() {
            return """
            Тип коллекции: ${data.javaClass}
            Количество элементов: ${data.size}
            Дата инициализации: $creationDate
            """.trimIndent()
        }

    val nextElementId: Int
        get() {
            return lastElementId++
        }


    fun appendData(newData: Collection<MusicBand>) {
        data.addAll(newData)
    }

    fun makeIndices() {
        var id = 1
        for (it in data) {
            it.setId(id++)
        }
        lastElementId = id
    }

    fun toArray(): Array<MusicBand> {
        var array: Array<MusicBand> = arrayOf()
        array = data.toArray(array)
        return array
    }

    fun toList(): List<MusicBand> {
        return data.toList()
    }

    fun add(musicBand: MusicBand) {
        data.add(musicBand)
    }

    fun clear() = data.clear()

    fun hasElementWithID(id: Int): Boolean {
        return (id > lastElementId) && data.any { it.id == id }
    }

    fun count(predicate: (MusicBand) -> Boolean): Int = data.count(predicate)

    fun removeIf(predicate: (MusicBand) -> Boolean) {
        data.removeIf(predicate)
    }

    fun filter(predicate: (MusicBand) -> Boolean) = data.filter(predicate)

    fun isEmpty() = data.isEmpty()

    fun deleteWithID(id: Int): Boolean = data.removeIf { it.id == id }

    fun maxOrNull(): MusicBand? = data.maxOrNull()
}
