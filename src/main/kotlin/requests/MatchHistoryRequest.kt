package requests

import requests.Request

class MatchHistoryRequest(apiKey: String, region: String, val encryptedAccountId: String, val beginTime: String) : Request(apiKey, region) {

    override fun getUrl(): String {
        return "https://$region.$urlDomain/lol/match/$apiVersion/matchlists/by-account/$encryptedAccountId?beginTime=$beginTime"
    }

}