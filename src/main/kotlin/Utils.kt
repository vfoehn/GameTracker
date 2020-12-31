import org.json.JSONObject
import java.util.*

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