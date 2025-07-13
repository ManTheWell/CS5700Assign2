import kotlinx.coroutines.*
import java.io.BufferedReader
import java.io.File

class UpdateSimulator(private val tracker: TrackingShipmentSimulator, private val filepath: String) {
    init {
        readFile()
    }

    private fun readFile(): Job {
        return CoroutineScope(Dispatchers.IO).launch {
            val reader: BufferedReader = File(filepath).bufferedReader()

            var line: String? = withContext(Dispatchers.IO) {
                reader.readLine()
            }

            var lineCount = 0

            while (line != null) {
                tracker.processUpdate(line) // Process the line
                line = withContext(Dispatchers.IO) {
                    reader.readLine()
                }    // Read the next line
                lineCount++
                if (lineCount % 10 == 0)
                    delay(5000L)
            }

            withContext(Dispatchers.IO) {
                reader.close()
            }
        }
    }
}

