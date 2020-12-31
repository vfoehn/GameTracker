import org.json.JSONArray
import org.json.JSONObject
import requests.MatchHistoryRequest
import requests.MatchInfoRequest
import requests.SummonerAccountRequest
import java.io.File
import java.lang.Exception
import java.util.*

class Poller(val apiKey: String, val region: String, val username: String) {

    val dataDirectory = "league_of_legends${File.separator}player_data${File.separator}$username"
    lateinit var mostRecentMatchTimestamp: Calendar
    lateinit var account: JSONObject
    lateinit var matchHistory: JSONArray

    init {
        fetchAccountInformation()
        initializeMatchHistory()
    }

    private fun initializeMatchHistory() {
        if (FileHandler.fileExists(dataDirectory)) {
            // Load previous match history from file.
            val jsonTokener = FileHandler.readJsonFromFile("$dataDirectory${File.separator}match_history_debug.json")
            val storedMatchHistory = JSONArray(jsonTokener)
            mostRecentMatchTimestamp = timestampToCalendar((storedMatchHistory[0] as JSONObject).getLong("timestamp"))
            println("mostRecentMatchTimestamp: $mostRecentMatchTimestamp")

            // If there are more recent matches we need to fetch and analyze them.
            matchHistory = fetchMatchHistory()
            analyzeMatches()

            // Only once the match analysis is done, can we add the stored match history.
            matchHistory.putAll(storedMatchHistory)
        } else {
            mostRecentMatchTimestamp = Calendar.getInstance()
            mostRecentMatchTimestamp.timeInMillis = 0
            matchHistory = fetchMatchHistory()
            analyzeMatches()
        }

        FileHandler.writeToFile("$dataDirectory${File.separator}match_history.json", matchHistory)
        mostRecentMatchTimestamp = timestampToCalendar(getMatchFromHistory(0).getLong("timestamp"))
        println("mostRecentMatchTimestamp: $mostRecentMatchTimestamp")
    }

    private fun fetchAccountInformation() {
        val request = SummonerAccountRequest(apiKey, region, username)
        account = request.sendRequest()
        if (account.isEmpty) {
            throw Exception("The account with username \"$username\" may not exist.")
        }
    }

    private fun fetchMatchHistory(): JSONArray {
        val accountId = account["accountId"].toString()
        val request = MatchHistoryRequest(apiKey, region, accountId, (mostRecentMatchTimestamp.timeInMillis+1).toString())
        val response = request.sendRequest()
        return if (response.isEmpty) JSONArray() else response["matches"] as JSONArray
    }

    fun fetchMatchInfo(gameId: Long): JSONObject {
        val request = MatchInfoRequest(apiKey, region, gameId.toString())
        return request.sendRequest()
    }

    private fun getMatchFromHistory(index: Int): JSONObject {
        return matchHistory[index] as JSONObject
    }

    private fun analyzeMatches() {
        println("Analyzing matches")
        for (element in matchHistory) {
            val elementJson = element as JSONObject
            val match = fetchMatchInfo(element.getLong("gameId"))
            val matchAnalyzer = MatchAnalyzer(match, username)
            println(matchAnalyzer.goodPerformance())
        }
    }
}