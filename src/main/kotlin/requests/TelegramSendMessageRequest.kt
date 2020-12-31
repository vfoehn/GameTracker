package requests

// A TelegramSendMessageRequest represents an HTTP GET request to send a message to a given chat.
class TelegramSendMessageRequest(apiKey: String, val chatId: Int, val text: String) : TelegramRequest(apiKey){

    override fun getUrl(): String {
        return "$urlDomain/bot${apiKey}/sendMessage?chat_id=$chatId&text=$text"
    }
}