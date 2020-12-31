import org.json.JSONObject
import java.io.File
import java.util.*
import java.util.Calendar




fun main() {
    println(FileHandler.readConfigFromFile())
    val properties = FileHandler.readConfigFromFile()
    val poller = Poller(properties.getProperty("apiKey"), properties.getProperty("region"), properties.getProperty("username"),)
//    val poller = Poller("RGAPI-3275faa2-19c2-4533-97d5-645b2890cf83", "euw1", "FuzzleyJojo")
}

// -------------------------------------------- Utility Functions -------------------------------------------------

fun timestampToCalendar(timestamp: Long): Calendar {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = timestamp
    return calendar
}

// Access a non-nested field in a JSONObject. The function takes care of the necessary casts. (Extension function)
fun JSONObject.getField(fieldName: String): JSONObject {
    return this[fieldName] as JSONObject
}

// Access a nested field in a JSONObject. The function takes care of the necessary casts. (Extension function)
fun JSONObject.getField(fieldPath: Array<String>): JSONObject {
    var current = this
    for (field in fieldPath) {
        current = current[field] as JSONObject
    }

    return current
}