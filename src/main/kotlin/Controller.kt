import leagueOfLegends.LeagueOfLegendsClient
import org.apache.log4j.Logger
import requests.TelegramSendMessageRequest
import telegram.TelegramBot
import java.util.Properties;

// The Controller is the central unit of the program. Periodically, it uses the LeagueOfLegendsClient to fetch
// the latest match history for a given user. If the user had a poor performance in any of the matches, the
// Controller uses the TelegramBot to send a message based on that performance to the user.
class Controller(val properties: Properties) {

    var logger: Logger = Logger.getLogger(Controller::class.java)

    private val lolClient = LeagueOfLegendsClient(properties.getProperty("leagueOfLegendsApiKey"),
        properties.getProperty("region"),
        properties.getProperty("username"))
    private val telegramBot = TelegramBot(properties.getProperty("botName"),
        properties.getProperty("telegramApiKey"),
        properties.getProperty("chatId").toInt())

    init {
        logger.info("Controller initialized.")
    }

    fun poll(sleepDuration: Long = 10000) {
        while (true) {
            lolClient.updateMatchHistory()
            val poorPerformances = lolClient.poorPerformances

            if (poorPerformances.isEmpty()) {
                logger.info("There are no new poor performances.")
            } else {
                // Send a message to the chat identified by chatId.
                logger.info("There are new poor performances. A message is being set.")
                val request = TelegramSendMessageRequest(
                    telegramBot.apiKey,
                    telegramBot.chatId,
                    poorPerformances.first.toString()
                )
                val response = request.sendRequest()
            }

            logger.info("Sleeping for $sleepDuration ms.")
            Thread.sleep(sleepDuration)
        }
    }
}
