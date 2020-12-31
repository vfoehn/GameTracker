package requests

import java.net.HttpURLConnection

abstract class LeagueOfLegendsRequest(apiKey: String, val region: String) : Request(apiKey) {

    val urlDomain = "api.riotgames.com"
    val apiVersion = "v4"

    override fun setHeader(con: HttpURLConnection) {
        con.setRequestProperty("X-Riot-Token", apiKey)
    }
}