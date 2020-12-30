package requests

import requests.Request

class MatchesRequest(apiKey: String, region: String, val matchId: String) : Request(apiKey, region) {

    override fun getUrl(): String {
        return "https://$region.$urlDomain/lol/match/$apiVersion/timelines/by-match/$matchId"
    }

}