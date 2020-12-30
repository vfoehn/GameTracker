import exceptions.RecurrentFailedRequestsException
import org.json.JSONObject
import requests.SummonerAccountRequest
import kotlin.system.exitProcess

class Poller(val apiKey: String, val region: String, val username: String) {

    lateinit var account: JSONObject

    fun fetchAccountInformation() {
        val accountRequest = SummonerAccountRequest(apiKey, region, username)
        account = accountRequest.sendRequest()
    }

    fun fetchMatchHistory() {

    }
}