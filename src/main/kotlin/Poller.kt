import org.json.JSONArray
import org.json.JSONObject
import requests.MatchHistoryRequest
import requests.MatchInfoRequest
import requests.SummonerAccountRequest
import java.io.File

class Poller(val apiKey: String, val region: String, val username: String) {

    val dataDirectory = "league_of_legends${File.separator}player_data${File.separator}$username"
    lateinit var account: JSONObject
    lateinit var matchHistory: JSONArray

    init {
        println("userFilesExist: ${userFilesExist()}")
        fetchAccountInformation()
        fetchMatchHistory()
    }

    private fun userFilesExist(): Boolean {
        return FileHandler.fileExists(dataDirectory)
    }

    private fun fetchAccountInformation() {
        val request = SummonerAccountRequest(apiKey, region, username)
        account = request.sendRequest()
    }

    private fun fetchMatchHistory() {
        val accountId = account["accountId"].toString()
        val request = MatchHistoryRequest(apiKey, region, accountId)
        val response = request.sendRequest()
        matchHistory = response["matches"] as JSONArray
    }

    fun fetchMatchInfo(gameId: Long): JSONObject {
        val request = MatchInfoRequest(apiKey, region, gameId.toString())
        return request.sendRequest()
    }

    fun getMatchFromHistory(index: Int): JSONObject {
        return matchHistory[index] as JSONObject
    }
}