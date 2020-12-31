package requests

import requests.Request

class MatchInfoRequest(apiKey: String, region: String, val gameId: String) : LeagueOfLegendsRequest(apiKey, region) {

    override fun getUrl(): String {
        return "https://$region.$urlDomain/lol/match/$apiVersion/matches/$gameId"
    }

}