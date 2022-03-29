package com.github.e1turin.lab5.client

import com.github.e1turin.lab5.common.collection.MusicBandStorage
import com.github.e1turin.lab5.common.collection.StorageManager
import com.github.e1turin.lab5.common.commands.*
import com.github.e1turin.lab5.common.util.IOStream
import java.util.*

class Client(val stdIOStream: IOStream) {

    fun start(args: Array<String>){


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
//            SaveCmd("save"),
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
}