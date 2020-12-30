package requests

import requests.Request

class SummonerAccountRequest(apiKey: String, region: String, val username: String) : Request(apiKey, region) {

    override fun getUrl(): String {
        // https://euw1.api.riotgames.com/lol/summoner/v4/summoners/by-name/FuzzleyJojo // TODO: Delete line
        return "https://$region.$urlDomain/lol/summoner/$apiVersion/summoners/by-name/$username"
    }

}