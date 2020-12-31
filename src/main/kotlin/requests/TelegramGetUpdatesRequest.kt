package requests

import java.net.HttpURLConnection

class TelegramGetUpdatesRequest(apiKey: String) : TelegramRequest(apiKey){

    override fun getUrl(): String {
        return "$urlDomain/bot${apiKey}/getUpdates"
    }
}