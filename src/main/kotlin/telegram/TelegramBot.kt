package telegram

import org.json.JSONObject
import requests.TelegramGetUpdatesRequest

// The TelegramBot contains information about the Telegram bot that is being used in this program.
class TelegramBot(val botName: String, val apiKey: String, val chatId: Int) {

    // Fetching updates can help finding the chatId of all the chats that the bot is part of. Alternatively,
    // you can use the already existing bot "@get_id_bot" to find the chatId (https://stackoverflow.com/a/41779623).
    fun getUpdates(): JSONObject {
        val request = TelegramGetUpdatesRequest(apiKey)
        return request.sendRequest()
    }
}