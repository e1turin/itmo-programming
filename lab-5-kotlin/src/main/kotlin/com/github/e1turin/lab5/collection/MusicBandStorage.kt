package com.github.e1turin.lab5.collection

import java.time.Clock
import java.time.LocalDate
import java.util.*
import kotlin.collections.LinkedHashSet

class MusicBandStorage(val name: String) {
    private val data: LinkedHashSet<MusicBand> = java.util.LinkedHashSet()
    var creationDate = LocalDate.now(Clock.systemUTC())
    var size: Int
        get() = data.size
        private set(n: Int) {}

    var lastElementId: Int = 0
        private set

    fun nextElementId(): Int {
        return lastElementId++
    }


    fun appendData(newData: Collection<MusicBand>) {
        data.addAll(newData)
    }
    fun makeIndices(){
        var id = 1
        for(it in data){
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
        if (id > lastElementId) return false
        for (i in data) {
            if (i.id == id) {
                return true
            }
        }
        return false
    }

    fun count(predicate: (MusicBand) -> Boolean): Int = data.count(predicate)
    fun filter(predicate: (MusicBand) -> Boolean) {
        for (it in data) {
            if (!predicate(it)) {
                data.remove(it)
            }
        }
    }


    fun isEmpty() = data.isEmpty()

    fun deleteWithID(id: Int): Boolean = data.removeIf { it.id == id }

    fun getMax(): MusicBand = Collections.max(data)
    fun getInfo(): String {
        return """
        Тип коллекции: ${data.javaClass}
        Дата инициализации: $creationDate
        Количество элементов: ${data.size}
        """.trimIndent()
    }
}
