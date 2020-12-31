package requests

import java.net.HttpURLConnection

// A TelegramRequest represents an HTTP GET request to the Telegram API server.
abstract class TelegramRequest(apiKey: String) : Request(apiKey){

    val urlDomain = "https://api.telegram.org"

    // A Telegram request does not need to set any header properties.
    override fun setHeader(con: HttpURLConnection) { ; }
}