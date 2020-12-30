package requests

import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

abstract class Request(val apiKey: String, val region: String) {

    val urlDomain = "api.riotgames.com"
    val apiVersion = "v4"

    abstract fun getUrl(): String

    fun sendRequest(): JSONObject? {
        var response: JSONObject? = null
        try {
            val urlString = "${getUrl()}?api_key=$apiKey"
               // "https://euw1.api.riotgames.com/lol/match/v4/matches/5005226744?api_key=RGAPI-3275faa2-19c2-4533-97d5-645b2890cf83"
            print("urlString: $urlString")
            val url = URL(urlString)
            val con: HttpURLConnection = url.openConnection() as HttpURLConnection
            con.requestMethod = "GET"

            val responseCode = con.responseCode
            if (responseCode < 200 || responseCode >= 300) {
                throw Exception("HTTP request unsuccessful. Response code: $responseCode")
            }

            val responseReader = BufferedReader(InputStreamReader(con.inputStream))
            var inputLine: String? = ""
            val responseString = StringBuffer()
            while (responseReader.readLine().also { inputLine = it } != null) {
                responseString.append(inputLine)
            }
            responseReader.close()

            response = JSONObject(responseString.toString())
            println(response) // TODO: Delete line
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            return response
        }
    }
}