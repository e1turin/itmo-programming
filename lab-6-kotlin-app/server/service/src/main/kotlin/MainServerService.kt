import com.github.e1turin.util.IOStream
import java.io.BufferedReader
import java.io.BufferedWriter

fun main(args: Array<String>){
    val localPort: Int = if(args.isNotEmpty()){args[0].toInt()}else{35047}
    val ioStream = IOStream(
        BufferedReader(System.`in`.reader()),
        BufferedWriter(System.out.writer()),
        interactive = true
    )

    val service = ServerService(ioStream,localPort)
    service.start(args)
}
