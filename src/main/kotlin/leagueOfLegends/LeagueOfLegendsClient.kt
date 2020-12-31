package leagueOfLegends

import FileHandler
import org.json.JSONArray
import org.json.JSONObject
import requests.DDragonRequest
import requests.LoLMatchHistoryRequest
import requests.LoLMatchInfoRequest
import requests.LoLSummonerAccountRequest
import timestampToCalendar
import java.io.File
import java.lang.Exception
import java.util.*

// The LeagueOfLegendsClient is the central entity for the "League of Legends" part of the program.
// It contains the logic that decides when to
//   - read/write the match history from/to file.
//   - fetch new information from the League of Legends API servers.
// Additionally, it checks if the given user had a poor performance in any of the new matches.
class LeagueOfLegendsClient(val apiKey: String, val region: String, val username: String) {

    val dataDirectory = "league_of_legends${File.separator}player_data${File.separator}$username"
    lateinit var mostRecentMatchTimestamp: Calendar
    lateinit var champions: JSONObject
    lateinit var account: JSONObject
    lateinit var matchHistory: JSONArray
    lateinit var poorPerformances : LinkedList<Performance>


    init {
        fetchChampionsInformation()
        fetchAccountInformation()
    }

    fun updateMatchHistory() {
        if (FileHandler.fileExists(dataDirectory)) {
            // Load previous match history from file.
            val jsonTokener = FileHandler.readJsonFromFile("$dataDirectory${File.separator}match_history_debug.json")
            val storedMatchHistory = JSONArray(jsonTokener)
            mostRecentMatchTimestamp = timestampToCalendar((storedMatchHistory[0] as JSONObject).getLong("timestamp"))

            // If there are more recent matches we need to fetch and analyze them.
            matchHistory = fetchMatchHistory()
            findPoorPerformances()

            // Only once the match analysis is done, can we add the stored match history.
            matchHistory.putAll(storedMatchHistory)
        } else {
            mostRecentMatchTimestamp = Calendar.getInstance()
            mostRecentMatchTimestamp.timeInMillis = 0
            matchHistory = fetchMatchHistory()
            findPoorPerformances()
        }

        FileHandler.writeToFile("$dataDirectory${File.separator}match_history.json", matchHistory)
        mostRecentMatchTimestamp = timestampToCalendar(getMatchFromHistory(0).getLong("timestamp"))
    }

    private fun fetchChampionsInformation() {
        // Get current patch version.
        var request = DDragonRequest(apiKey, region, "api/versions.json")
        val response: JSONObject = request.sendRequest(encapsulateJSONArray = true)
        val latestVersion = (response["versions"] as JSONArray)[0]

        request = DDragonRequest(apiKey, region, "cdn/$latestVersion/data/en_US/champion.json")
        champions = request.sendRequest()
    }

    private fun fetchAccountInformation() {
        val request = LoLSummonerAccountRequest(apiKey, region, username)
        account = request.sendRequest()
        if (account.isEmpty) {
            throw Exception("The account with username \"$username\" may not exist.")
        }
    }

    private fun fetchMatchHistory(): JSONArray {
        val accountId = account["accountId"].toString()
        val request = LoLMatchHistoryRequest(apiKey, region, accountId, (mostRecentMatchTimestamp.timeInMillis+1).toString())
        val response = request.sendRequest()
        return if (response.isEmpty) JSONArray() else response["matches"] as JSONArray
    }

    fun fetchMatchInfo(gameId: Long): JSONObject {
        val request = LoLMatchInfoRequest(apiKey, region, gameId.toString())
        return request.sendRequest()
    }

    private fun getMatchFromHistory(index: Int): JSONObject {
        return matchHistory[index] as JSONObject
    }

    private fun findPoorPerformances() {
        poorPerformances = LinkedList<Performance>() // Reset list of poor performances
        for (element in matchHistory) {
            val match = fetchMatchInfo((element as JSONObject).getLong("gameId"))
            val matchAnalyzer = MatchDataOrganizer(match, username)
            val performance: Performance = matchAnalyzer.getPerformance()
            if (performance.isPoor) {
                poorPerformances.add(performance)
            }
        }
    }
}