package com.github.e1turin.lab5.common.application

import com.github.e1turin.lab5.common.commands.*

class CommandManagerFactory {
    companion object {
        fun getInstance(): CommandManager {
            return CommandManager(
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
                RemoveAllGreaterCmd("remove_greater"),
                HistoryCmd("history"),
                AverageOfNumberOfParticipantsCmd("average_of_number_of_participants"),
                CountLessThanAlbumsCountCmd("count_less_than_albums_count"),
                PrintAscendingCmd("print_ascending")
            )
        }
    }
}