package requests

import java.net.HttpURLConnection

// A DDragonRequest represents an HTTP GET request to the Data Dragon API server.
class DDragonRequest(apiKey: String, val region: String, val urlPath: String) : Request(apiKey) {

    val urlDomain = "https://ddragon.leagueoflegends.com"

    override fun setHeader(con: HttpURLConnection) {
        con.setRequestProperty("X-Riot-Token", apiKey)
    }

    override fun getUrl(): String {
        return "$urlDomain/$urlPath"
    }
}