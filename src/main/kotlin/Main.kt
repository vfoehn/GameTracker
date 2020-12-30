import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

import org.json.JSONObject

class Main {
    val debug = "DEBUG"
}

fun main() {
    println("Hello World")

    val urlString = "https://euw1.api.riotgames.com/lol/match/v4/matches/5005226744?api_key=RGAPI-3275faa2-19c2-4533-97d5-645b2890cf83"
    val url = URL(urlString)
    val con: HttpURLConnection =  url.openConnection() as HttpURLConnection
    con.requestMethod = "GET"

    val reponseCode = con.responseCode
    val responseReader = BufferedReader(InputStreamReader(con.inputStream))
    var inputLine: String? = ""
    val responseString = StringBuffer()
    while (responseReader.readLine().also { inputLine = it } != null) {
        responseString.append(inputLine)
    }
    responseReader.close()

    val response = JSONObject(responseString.toString())
    println(response)

}