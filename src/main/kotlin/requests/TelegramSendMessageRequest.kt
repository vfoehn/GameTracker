package requests

import java.net.HttpURLConnection

class TelegramSendMessageRequest(apiKey: String, val chatId: Int, val text: String) : Request(apiKey){

    val urlDomain = "https://api.telegram.org"

    override fun getUrl(): String {
        return "$urlDomain/bot${apiKey}/sendMessage?chat_id=$chatId&text=$text"
    }

    override fun setHeader(con: HttpURLConnection) { ; }
}