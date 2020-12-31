package requests

import java.net.HttpURLConnection

// A LolRequest represents an HTTP GET request to the League of Legends API server.
abstract class LoLRequest(apiKey: String, val region: String) : Request(apiKey) {

    val urlDomain = "api.riotgames.com"
    val apiVersion = "v4"

    override fun setHeader(con: HttpURLConnection) {
        con.setRequestProperty("X-Riot-Token", apiKey)
    }
}