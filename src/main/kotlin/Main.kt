import leagueOfLegends.Poller
import org.json.JSONObject
import requests.TelegramSendMessageRequest
import telegram.TelegramBot
import java.util.Calendar


fun main() {
    val properties = FileHandler.readConfigFromFile()
    val controller = Controller(properties)
    controller.poll()

//    val poller = Poller(properties.getProperty("leagueOfLegendsApiKey"), properties.getProperty("region"), properties.getProperty("username"))
//    val poorPerformances = poller.poorPerformances
//    println("poorPerformances: $poorPerformances")
//
//    val telegramBot = TelegramBot(properties.getProperty("botName"), properties.getProperty("telegramApiKey"))
//    val request = TelegramSendMessageRequest(properties.getProperty("telegramApiKey"), properties.getProperty("chatId").toInt(), poorPerformances.first.toString())
//    val response = request.sendRequest()
//    println(response)
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