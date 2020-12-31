import org.json.JSONObject
import java.io.File

fun main() {
    println("Hello World")
    // val request = MatchesRequest("RGAPI-3275faa2-19c2-4533-97d5-645b2890cf83", "euw1", "5005226744")
//    val request = SummonerAccountRequest("RGAPI-3275faa2-19c2-4533-97d5-645b2890cf83", "euw1", "FuzzleyJojo")
//    request.sendRequest()
    val poller = Poller("RGAPI-3275faa2-19c2-4533-97d5-645b2890cf83", "euw1", "FuzzleyJojo")
    val matchInfo: JSONObject = poller.fetchMatchInfo(poller.getMatchFromHistory(0)["gameId"] as Long)

    val matchAnalyzer = MatchAnalyzer(matchInfo, "FuzzleyJojo")
    println("goodPerformance: ${matchAnalyzer.goodPerformance()}")

    val fileHandler = FileHandler
    fileHandler.writeToFile("dir${File.separator}matchHistory.json", poller.matchHistory)
}

// Access a non-nested field in a JSONObject. The function takes care of the necessary casts. (Extension function)
fun JSONObject.getField(fieldName: String): JSONObject {
    return this[fieldName] as JSONObject
}

// Access a nested field in a JSONObject. The function takes care of the necessary casts. (Extension function)
fun JSONObject.getField(fieldPath: Array<String>): JSONObject {
    var current = this
    for (field in fieldPath) {
        current = current[field] as JSONObject
    }

    return current
}