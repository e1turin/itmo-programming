package com.github.e1turin.lab5.common.application

import com.github.e1turin.lab5.common.exceptions.ExitScripException
import com.github.e1turin.lab5.common.util.IOStream

class TaskManager {
    //    val commandManager: CommandManager
    val storageManager: StorageManager

    /*
    constructor(commandManager: CommandManager, storageManager: StorageManager):this() {
        this.commandManager = commandManager
        this.storageManager = storageManager
    }
     */

    constructor(storageManager: StorageManager) {
        this.storageManager = storageManager
//        this.commandManager = CommandManager()//TODO("Mock of commandManager")
    }

    /*
    constructor(commandManager: CommandManager):this() {
        this.commandManager = commandManager
        this.storageManager = StorageManager(MusicBandStorage((), IOStream())//TODO("Mock of
    // storageManager")
    }
     */
    /*
    fun loop(ioStream: IOStream = stdIOStream) {
        while (ioStream.interactive || ioStream.canRead()) {
            val cmdAndArg = commandManager.getCmdWithArg(ioStream)
            val cmdName = cmdAndArg.first
            val arg = cmdAndArg.second

            try {
//                if (commandManager.validateCmd(cmdName)) {
                loopStepExecuteValidatedCmdWithArg(cmdName, arg, ioStream)
//                } else {
//                    if (!ioStream.interactive && cmdName.isNotBlank()) throw NonexistentCommandException(
//                        cmdName
//                    )
//                    ioStream.writeln("Неверное имя команды! Попробуйте снова")
//                }
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
     */


    fun loop(ioStream: IOStream){

    }
}