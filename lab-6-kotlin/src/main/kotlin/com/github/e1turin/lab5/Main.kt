package com.github.e1turin.lab5

import com.github.e1turin.lab5.common.collection.MusicBandStorage
import com.github.e1turin.lab5.common.collection.StorageManager
import com.github.e1turin.lab5.common.commands.*
import com.github.e1turin.lab5.common.util.IOStream
import java.io.*
import java.nio.charset.StandardCharsets
import java.time.LocalDate
import java.util.LinkedList

fun main(args: Array<String>) {
    val stdIOStream = IOStream(
        InputStreamReader(System.`in`, StandardCharsets.UTF_8),
        PrintWriter(System.out),
        true
    )
    val storageName: String = if (args.isNotEmpty()) {
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

//    val fileIOStream: IOStream = IOStream(
//        FileReader(storageFile),
//        FileWriter(storageFile),
//        true
//    )

    val storage = MusicBandStorage(storageName)
    val history = LinkedList<String>()
    val storageManager = StorageManager(
        storage,
        history,
        stdIOStream,
        LoadCmd("load"),
        HelpCmd("help"),
        InfoCmd("info"),
        ShowCmd("show"),
        AddCmd("add"),
        UpdateWithIDCmd("update"),
        RemoveWithIDCmd("remove_by_id"),
        ClearCmd("clear"),
        SaveCmd("save"),
        ExecuteScriptCmd("execute_script"),
        ExitCmd("exit"),
        AddIfMaxCmd("add_if_max"),
        RemoveGreaterCmd("remove_greater"),
        HistoryCmd("history"),
        AverageOfNumberOfParticipantsCmd("average_of_number_of_participants"),
        CountLessThanAlbumsCountCmd("count_less_than_albums_count"),
        PrintAscendingCmd("print_ascending")
    ).apply { loadData(storageFile) }


    stdIOStream.writeln("Менеджер запущен. Для справки вы можете вызвать команду help.")
    storageManager.loop()
    stdIOStream.writeln("Работа менеджера завершена.")
    return
}