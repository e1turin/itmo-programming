package com.github.e1turin.lab5.collection

import com.github.e1turin.lab5.commands.Command
import com.github.e1turin.lab5.containers.*
import com.github.e1turin.lab5.exceptions.*
import com.github.e1turin.lab5.util.IOStream
import com.github.e1turin.lab5.util.LocalDateKotlinAdapter
import com.google.gson.GsonBuilder
import java.io.*
import java.nio.file.Files
import java.time.LocalDate
import java.util.*


class StorageManager(
    private var storage: MusicBandStorage,
    private val history: LinkedList<String>,
    private val stdIOStream: IOStream,
    vararg commands: Command
) {
    private val commands: MutableMap<String, Command> = HashMap<String, Command>()
    private val scriptCallstack: Stack<File> = Stack()
    private val scriptCallstackLimit: Int = 10


    enum class TaskType {
        NONE, EXIT, GIVE_HELP, LOAD_DATA, CLEAR_COLLECTION, GIVE_HISTORY, EXEC_SCRIPT, SAVE_DATA
    }

    init {
        for (command in commands) {
            this.commands[command.cmdName] = command
//            stdIOStream.write(command.cmdName + "; ")
        }
    }

    fun loadData(storageFile: File) { // Method used only on first setup
        if (storageFile.canRead()) {
            if (storageFile.length() != 0L) { // file is empty is no reason to load data from it
                try {
                    val jsonReader = BufferedReader(FileReader(storageFile))
                    val gson = GsonBuilder()
                        .serializeNulls()
                        .registerTypeAdapter(
                            LocalDate::class.java,
                            LocalDateKotlinAdapter().nullSafe()
                        )
                        .create()

                    val storageData = gson.fromJson(jsonReader, MusicBandStorage::class.java)

                    storage.apply {
                        creationDate = storageData.creationDate
                        appendData(storageData.toList()).also {
                            makeIndices()
                        }
                    }
                } catch (ex: Exception) {
                    stdIOStream.writeln("Возникли проблемы с загрузкой данных. Данные не загружены.")
//                    throw ex
                    //TODO: Logger.log(ex.printStackTrace())
                }
            }
        } else {
            stdIOStream.writeln("Не возможно считать из файла. Данные не загружены.")
        }
    }

    private fun saveDataToFile(fileToSaveData: File) {
//        val mapper = ObjectMapper()
////        mapper.writeValue(System.out, storage)
//        mapper.writeValue(fileToSaveData, storage)
//        val gson = Gson()
        val gson = GsonBuilder()
            .serializeNulls()
            .registerTypeAdapter(LocalDate::class.java, LocalDateKotlinAdapter().nullSafe())
            .create()
        val jsonString: String = gson.toJson(storage)
//        stdIOStream.writeln(jsonString)
        val writer = BufferedWriter(FileWriter(fileToSaveData))
        writer.write(jsonString)
        writer.close()
    }

    fun validateCmd(cmd: String): Boolean = commands.containsKey(cmd)

    private fun executeCmd(cmdName: String, arg: String, ioStream: IOStream): Message {
        return commands[cmdName]!!.exec(arg, storage, ioStream)
    }

    private fun giveResponseToCmd(
        cmdName: String, response: Response, ioStream: IOStream
    ): Message {
        val cmd = commands[cmdName]!!
        return if (cmd is Requestable) {
            cmd.exec(response, ioStream)
        } else {
            throw NotRequestableCommandException()
        }
    }

    private fun addCmdToHistory(cmdName: String) {
        history.addLast(cmdName)
        if (history.size > 6) history.removeFirst()
    }

    private fun callstackHasScript(callStack: Stack<File>, fileToExecScript: File): Boolean {
        for (it in callStack) {
            if (it == fileToExecScript || Files.mismatch(
                    it.toPath(),
                    fileToExecScript.toPath()
                ) == -1L
            ) return true
        }
        return false
    }

    private fun clearStorage() = storage.clear()

    fun setCreationDate(date: LocalDate) {
//        storage.creationDate = date
    }

    fun loop(ioStream: IOStream = stdIOStream) {
        while (ioStream.interactive || ioStream.canRead()) {
            val cmdAndArg = loopStepGetCmdWithArg(ioStream)
            val cmdName = cmdAndArg.first
            val arg = cmdAndArg.second

            try {
                if (validateCmd(cmdName)) {
                    loopStepExecuteValidatedCmdWithArg(cmdName, arg, ioStream)
                } else {
                    if (!ioStream.interactive && cmdName.isNotBlank())
                        throw NonexistentCommandException(cmdName)
                    ioStream.writeln("Неверное имя команды! Попробуйте снова")
                }
            } catch (e: ExitScripException) {
                if (ioStream.interactive) {
                    ioStream.writeln("Завершение работы менеджера...")
                }
                return

            } catch (e: Throwable) {
                processLoopExceptions(e, ioStream)
            }
        }
    }

    private fun processLoopExceptions(e: Throwable, ioStream: IOStream) {
        when (e) {
            is InterruptedException -> {
                return
            }
            is RecursiveScriptException -> {
                if (ioStream.interactive) {
                    ioStream.writeln("Обнаружена рекурсия в скрипте. Выполнение прекращено.")
                } else {
                    throw e
                }
            }
            is CallstackOverflowException -> {
                if (ioStream.interactive) {
                    ioStream.writeln(
                        "Произошло слишком много дополнительных вызовов выполнения " + "скрипта (максимум = $scriptCallstackLimit). Выполнение " + "остановлено."
                    )
                } else {
                    throw e
                }
            }
            is NonexistentCommandException -> {
                if (ioStream.interactive) {
                    ioStream.writeln("Попытка выполнить несуществующую команду ${e.cmd}")
                } else {
                    throw e
                }
            }
            is InvalidCmdArgumentException -> {
                if (ioStream.interactive) {
                    ioStream.writeln("Не верные значения параметров (${e.arg})")
                } else {
                    throw e
                }
            }

            else -> {
                throw e
            }
        }
    }

    private fun loopStepGetCmdWithArg(ioStream: IOStream): Pair<String, String> {
        var cmdName: String = ""
        var arg: String = ""
        try {
            val cmdAndArg = ioStream.termInputUntil(sep = ">>>",
                hint = "Ошибка! Такой команды нет, попробуйте снова",
                condition = { it != null && validateCmd(it.first) },
                query = { ioStream.readCmdWithArg() })
                ?: throw InvalidCmdArgumentException("cmdAndArg")
            cmdName = cmdAndArg.first
            arg = cmdAndArg.second
        } catch (e: InterruptedException) {
            ioStream.writeln("<<Прерывание работы>>")
            ioStream.writeln("Для выхода из менеджера используйте команду exit")
            e.message?.let { ioStream.writeln(it) }
        }
        return Pair(cmdName, arg)
    }

    private fun loopStepExecuteValidatedCmdWithArg(
        cmdName: String, arg: String, ioStream: IOStream
    ) {
        try {
            var message: Message = executeCmd(cmdName, arg, ioStream)
            //TODO Logger.log(message)
            if (message is Request) {
                message = doTask(message, ioStream)
            }
            //TODO Logger.log(message)
            addCmdToHistory(cmdName)
        } catch (e: InterruptedException) {
            //TODO Logger.log(Interrupted)
            ioStream.writeln("<<Прервано выполнение команды $cmdName с аргументом $arg>>")
        }
    }


    /**
     *  @throws RecursiveScriptException
     *  @throws CallstackOverflowException
     */
    private fun doTask(request: Request, ioStream: IOStream): Message {
        return when (request.taskType) {
            TaskType.CLEAR_COLLECTION -> clearCollectionTask(request, ioStream)
            TaskType.EXIT -> exitTask(request, ioStream)
            TaskType.GIVE_HELP -> giveHelpTask(request, ioStream)
            TaskType.SAVE_DATA -> saveDataTask(request, ioStream)
            TaskType.LOAD_DATA -> TODO()
            TaskType.GIVE_HISTORY -> giveHistoryTask(request, ioStream)
            TaskType.EXEC_SCRIPT -> execScriptTask(request, ioStream)
            TaskType.NONE -> TODO()
            else -> TODO("Other task is not yet implemented")
        }
    }

    private fun clearCollectionTask(request: Request, ioStream: IOStream): Message {
        clearStorage()
        return giveResponseToCmd(
            cmdName = request.sender, response = Response(
                request.sender,
                ResponseType.TASK_COMPLETED,
                content = "Task from ${request.sender} finished, collection cleared"
            ), ioStream = ioStream
        )
    }

    @kotlin.jvm.Throws(ExitScripException::class)
    private fun exitTask(request: Request, ioStream: IOStream): Message {
        //TODO: Logger.log(exiting...)
        throw ExitScripException()
    }

    private fun giveHelpTask(request: Request, ioStream: IOStream): Message {
        var helpString: String = "Команда\t| Описание команды"
        for (it in commands) {
            helpString += "\n${it.key}\t: ${it.value.description}"
        }
        return giveResponseToCmd(
            cmdName = request.sender, response = Response(
                request.sender, ResponseType.TASK_COMPLETED, arg = helpString,
                content = "Task from ${request.sender} finished, collection cleared"
            ), ioStream = ioStream
        )
    }

    private fun saveDataTask(request: Request, ioStream: IOStream): Message {
        val pathToSaveData = request.arg as String
        val fileToSaveData = File(pathToSaveData)
        if (!fileToSaveData.canWrite()) {
            //TODO Logger.log(can't write)
            return giveResponseToCmd(
                cmdName = request.sender, response = Response(
                    request.sender,
                    ResponseType.TASK_FAILED,
                    content = "Произошла ошибка (Нет права на запись в файл), данные не сохранены"
                ), ioStream = ioStream
            )
        }
        try {
            saveDataToFile(fileToSaveData)
            return giveResponseToCmd(
                cmdName = request.sender, response = Response(
                    request.sender,
                    ResponseType.TASK_COMPLETED,
                    content = "Данные успешно сохранены по пути: '$pathToSaveData'"
                ), ioStream = ioStream
            )
        } catch (ex: java.lang.Exception) {
            //TODO Logger.log(ex)
            return giveResponseToCmd(
                cmdName = request.sender, response = Response(
                    request.sender,
                    ResponseType.TASK_FAILED,
                    content = "Произошла ошибка (${ex.message}), данные не сохранены"
                ), ioStream = ioStream
            )
        }
    }

    private fun giveHistoryTask(request: Request, ioStream: IOStream): Message {
        var historyString: String = "История команд:\n<...>"
        for (it in 1 until history.size) {
            historyString += "\n$it\t:${history[it]}"
        }
        return giveResponseToCmd(
            cmdName = request.sender, response = Response(
                request.sender, ResponseType.TASK_COMPLETED, content = historyString
            ), ioStream = ioStream
        )
    }

    private fun execScriptTask(request: Request, ioStream: IOStream): Message {
        val pathToExecScript = request.arg as String
        val fileToExecScript = File(pathToExecScript)
        if (!fileToExecScript.exists()) {
            return giveResponseToCmd(
                cmdName = request.sender, response = Response(
                    request.sender,
                    ResponseType.TASK_FAILED,
                    content = "Произошла ошибка. Файла не существует"
                ), ioStream = ioStream
            )
        }
        if (!fileToExecScript.canRead()) {
            return giveResponseToCmd(
                cmdName = request.sender, response = Response(
                    request.sender,
                    ResponseType.TASK_FAILED,
                    content = "Произошла ошибка. Файл нельзя продолжить считывание или не " +
                            "существует"
                ), ioStream = ioStream
            )
        }
        if (callstackHasScript(scriptCallstack, fileToExecScript)) throw RecursiveScriptException()
        if (scriptCallstack.size + 1 > scriptCallstackLimit) throw CallstackOverflowException()

        scriptCallstack.push(fileToExecScript)
        try {
            loop(IOStream(BufferedReader(FileReader(fileToExecScript)), Writer.nullWriter(), false))
        } catch (e: Throwable) {
            scriptCallstack.pop()
            return giveResponseToCmd(
                cmdName = request.sender, response = Response(
                    request.sender,
                    ResponseType.TASK_FAILED,
                    content = "Скрипт $pathToExecScript не выполнился, возникла ошибка.",
                    arg = e
                ), ioStream = ioStream
            )
        }
        return giveResponseToCmd(
            cmdName = request.sender, response = Response(
                request.sender,
                ResponseType.TASK_COMPLETED,
                content = "Скрипт $pathToExecScript успешно выполнен"
            ), ioStream = ioStream
        )
    }


}


