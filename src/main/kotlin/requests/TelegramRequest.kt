package requests

import java.net.HttpURLConnection

abstract class TelegramRequest(apiKey: String) : Request(apiKey){

    val urlDomain = "https://api.telegram.org"

    // A Telegram request does not need to set any header properties.
    override fun setHeader(con: HttpURLConnection) { ; }
}