package com.github.e1turin.application

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.IOException

class MusicBandStorageManager(musicBandStorage: MusicBandStorage) {
    private val store = musicBandStorage
    val dataAsList: List<MusicBand>
        get() {
            return store.toList()
        }

    val storeSize: Int get() = store.size

    val storageName: String get() = store.name

    fun info(): String {
        return store.info
    }

    fun saveDataTo(file: File) {
        val json: String = Json.encodeToString(store)
//        stdIOStream.writeln(jsonString)
        BufferedWriter(FileWriter(file)).use {
            it.write(json)
        }
    }

    //    @OptIn(ExperimentalSerializationApi::class)
    fun loadDataFrom(file: File) {
        if (!file.canRead()) {
            throw IOException("Can not read storage input file")
        }
        if (file.length() == 0L) {
            throw IOException("Storage input file is empty")
        }
        val jsonStore: MusicBandStorage
        try {
            jsonStore = Json.decodeFromStream(file.inputStream())
            store.apply {
                creationDate = jsonStore.creationDate
                appendData(jsonStore.toList())
                makeIndices()
            }
        } catch (e: Exception) {
//            io.writeln("Невозможно считать из файла. Данные не загружены.")
            throw e
        }
    }

    fun clearStore() {
        store.clear()
        saveDataTo(File(store.name))
    }

    fun addElement(obj: MusicBand) {
        store.add(obj)
        saveDataTo(File(store.name))
    }

    fun removeIf(predicate: (MusicBand) -> Boolean) {
        store.removeIf(predicate)
        saveDataTo(File(store.name))
    }

    fun count(predicate: (MusicBand) -> Boolean) = store.count(predicate)

    fun filter(predicate: (MusicBand) -> Boolean) {
        store.filter(predicate)
        saveDataTo(File(store.name))
    }


}