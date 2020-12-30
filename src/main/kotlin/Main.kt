import org.json.JSONObject
import requests.MatchHistoryRequest

fun main() {
    println("Hello World")
    // val request = MatchesRequest("RGAPI-3275faa2-19c2-4533-97d5-645b2890cf83", "euw1", "5005226744")
//    val request = SummonerAccountRequest("RGAPI-3275faa2-19c2-4533-97d5-645b2890cf83", "euw1", "FuzzleyJojo")
//    request.sendRequest()
    val poller = Poller("RGAPI-3275faa2-19c2-4533-97d5-645b2890cf83", "euw1", "FuzzleyJojo")
    poller.fetchAccountInformation()
    poller.fetchMatchHistory()
    poller.fetchMatchInfo(1)
}