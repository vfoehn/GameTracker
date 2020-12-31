import leagueOfLegends.Poller
import requests.TelegramSendMessageRequest
import telegram.TelegramBot
import java.util.Properties;

class Controller(val properties: Properties) {

    private val poller = Poller(properties.getProperty("leagueOfLegendsApiKey"),
        properties.getProperty("region"),
        properties.getProperty("username"))
    private val telegramBot = TelegramBot(properties.getProperty("botName"),
        properties.getProperty("telegramApiKey"),
        properties.getProperty("chatId").toInt())

    fun poll(sleepDuration: Long = 10000) {
        while (true) {
            poller.updateMatchHistory()
            val poorPerformances = poller.poorPerformances

            if (poorPerformances.isEmpty()) {
                println("There are no new poor performances.")
            } else {
                // Send a message to the chat identified by chatId.
                println("There are new poor performances. A message is being set.")
                val request = TelegramSendMessageRequest(
                    telegramBot.apiKey,
                    telegramBot.chatId,
                    poorPerformances.first.toString()
                )
                val response = request.sendRequest()
            }

            println("Sleeping for $sleepDuration ms.")
            Thread.sleep(sleepDuration)
        }
    }
}
