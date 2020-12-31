import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.PrintWriter


object FileHandler {

    fun fileExists(filePath: String): Boolean {
        return File(filePath).exists()
    }

    fun createDirectory(filePath: String) {
        val directoryName = filePath.substringBeforeLast(File.separator)
        val directory = File(directoryName)
        if (!directory.exists()) {
            directory.mkdirs()
        }
    }

    fun writeToFile(filePath: String, content: Any) {
        createDirectory(filePath)
        val myFile = PrintWriter(filePath, "UTF-8")
        when (content) {
            // Treat JSONArray and Object separately to enable smart-cast
            is JSONArray -> myFile.println(content.toString(4))
            is JSONObject -> myFile.println(content.toString(4))
            else -> myFile.println(content.toString())
        }
        myFile.close()
    }
}