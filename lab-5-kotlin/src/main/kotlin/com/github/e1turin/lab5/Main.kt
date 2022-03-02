package com.github.e1turin.lab5

import com.github.e1turin.lab5.collection.MusicBandStorage
import com.github.e1turin.lab5.collection.StorageManager
import com.github.e1turin.lab5.commands.*
import com.github.e1turin.lab5.util.IOStream
import java.io.*
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.time.LocalDate
import java.util.LinkedList
import java.util.Queue

fun main(args: Array<String>) {
    val stdIOStream = IOStream(
        InputStreamReader(System.`in`, StandardCharsets.UTF_8),
        PrintWriter(System.out),
        true
    )
    val storageName: String = if (args.isNotEmpty()) {
        if (args[0].substring(args[0].length - 4..args[0].length) == ".json") {
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

    /*
    if (!storageFile.exists()) {
        stdIOStream.writeln(
            "Указан не верный путь до файла, или файла не существует. " +
                    "Создать файл? - Y[es]/N[o]:"
        )
        if (stdIOStream.yesAnswer()) {
            storageName = stdIOStream.readFileName()
        } else {
            stdIOStream.writeln("Выход из консоли управления...")
            return
        }
    }
     */

    val fileIOStream: IOStream = IOStream(
        FileReader(storageFile),
        FileWriter(storageFile),
        true
    )

    val storage = MusicBandStorage(storageName)
    val history = LinkedList<String>()
    val storageManager = StorageManager(
        storage,
        history,
        stdIOStream,
        LoadCmd(storage, "load", stdIOStream, fileIOStream),
        HelpCmd(storage, "help", stdIOStream),
        InfoCmd(storage, "info", stdIOStream),
        ShowCmd(storage, "show", stdIOStream),
        AddCmd(storage, "add", stdIOStream),
        UpdateWithIDCmd(storage, "update", stdIOStream),
        RemoveWithIDCmd(storage, "remove_by_id", stdIOStream),
        ClearCmd(storage, "clear", stdIOStream),
        SaveCmd(storage, "save", stdIOStream, fileIOStream),
        ExecuteScriptCmd(storage, "execute_script", stdIOStream, fileIOStream),
        ExitCmd(storage, "exit", stdIOStream),
        AddIfMaxCmd(storage, "add_if_max", stdIOStream),
        RemoveGreaterCmd(storage, "remove_greater", stdIOStream),
        HistoryCmd(storage, "history", stdIOStream, history),
        AverageOfNumberOfParticipantsCmd(storage, "average_of_number_of_participants", stdIOStream),
        CountLessThanAlbumsCountCmd(storage, "count_less_than_albums_count", stdIOStream),
        PrintAscendingCmd(storage, "print_ascending", stdIOStream)
    ).apply { loadData(storageFile) }


    storageManager.loop()
    stdIOStream.writeln("Работа менеджера завершена.")
    return
}