import com.github.e1turin.util.IOStream
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.File
import java.io.IOException
import java.net.BindException

fun main(args: Array<String>){
    val localPort: Int = if(args.isNotEmpty()){args[0].toInt()}else{35047}
    val ioStream = IOStream(
        BufferedReader(System.`in`.reader()),
        BufferedWriter(System.out.writer()),
        interactive = true
    )

    val storageName: String = if (args.size>1) {
        if (args[1].endsWith(".json")) {
            args[1]
        } else {
            args[1] + ".json"
        }
    } else {
        "storage-${Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date}.json"
    }

//    val storageFile = File(storageName)
//    try {
//        val resultOfCreation = storageFile.createNewFile()
//        if (resultOfCreation) {
//            ioStream.writeln("Файл хранилища коллекции успешно создан")
//        }
//    } catch (e: IOException) {
//        ioStream.writeln("Произошла ошибка при создании файла. Завершение работы менеджера.")
//        return
//    }

    val service = ServerService(ioStream,localPort, storageName)
    try {
        service.start(args)
    }catch (e: BindException){
        println("Socket 35047 is not free")
    } catch (e: Exception){
        println("error: ${e.message}")
    }
}
