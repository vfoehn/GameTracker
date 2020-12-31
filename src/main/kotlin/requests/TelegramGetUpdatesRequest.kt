package requests

// A TelegramGetUpdatesRequest represents an HTTP GET request to get all updates for a given Telegram bot.
// An update consists of all the messages visible to the bot that have been created since the last time
// the bot queried for updates.
class TelegramGetUpdatesRequest(apiKey: String) : TelegramRequest(apiKey){

    override fun getUrl(): String {
        return "$urlDomain/bot${apiKey}/getUpdates"
    }
}