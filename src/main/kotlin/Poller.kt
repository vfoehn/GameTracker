import org.json.JSONObject
import requests.SummonerAccountRequest
import kotlin.system.exitProcess

class Poller(val apiKey: String, val region: String, val username: String) {

    val MAX_FAILED_REQUEST = 5
    val BACKOFF_TIME: Long = 2000
    lateinit var account: JSONObject

    fun fetchAccountInformation() {
        // Fetch the account information.
        val accountRequest = SummonerAccountRequest(apiKey, region, username)
        var accountResponse = accountRequest.sendRequest()
        var numFailedRequests = 0
        while (accountResponse == null) {
            numFailedRequests++
            if (numFailedRequests >= MAX_FAILED_REQUEST) {
                exitProcess(0)
            }
            Thread.sleep(BACKOFF_TIME)
            accountResponse = accountRequest.sendRequest()
        }

        account = accountResponse // Smart-cast JSONObject? to JSONObect
    }
}