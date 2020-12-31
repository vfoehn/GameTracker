package requests

import java.net.HttpURLConnection

class TelegramSendMessageRequest(apiKey: String, val chatId: Int, val text: String) : TelegramRequest(apiKey){

    override fun getUrl(): String {
        return "$urlDomain/bot${apiKey}/sendMessage?chat_id=$chatId&text=$text"
    }
}