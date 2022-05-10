package com.github.e1turin.lab5.server

import com.github.e1turin.lab5.common.application.MusicBandStorage
import com.github.e1turin.lab5.common.application.StorageManager
import com.github.e1turin.lab5.common.application.TaskManager
import com.github.e1turin.lab5.common.util.IOStream
import java.io.File
import java.io.IOException
import java.time.LocalDate

class Server(val stdIOStream: IOStream) {

    fun startServer(args: Array<String>) {
        val storageName: String = if (args.isNotEmpty()) { //TODO: console flags -P <PATH>
            if (args[0].endsWith(".json")) {
                args[0]
            } else {
                args[0] + ".json"
            }
        } else {
            "storage-${LocalDate.now()}.json"
        }

        val storageFile = File(storageName)
        try {
            val resultOfCreation = storageFile.createNewFile()
            if (resultOfCreation) {
                stdIOStream.writeln("Файл хранилища коллекции успешно создан")
            }
        } catch (e: IOException) {
            stdIOStream.writeln("Произошла ошибка при создании файла. Завершение работы менеджера.")
            return
        }


        //TODO: TaskManager{...}

        val storage = MusicBandStorage(storageName)
        val storageManager = StorageManager(
            storage,
            stdIOStream
        ).apply { loadData(storageFile) }
        val taskManager = TaskManager(storageManager)

        loop()

        stdIOStream.writeln("Работа менеджера завершена.")
        return
    }

    private fun loop() {
        stdIOStream.writeln("Менеджер запущен.")
        while(true) {

        }
    }
}