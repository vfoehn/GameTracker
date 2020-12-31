package requests

// A LoLMatchHistoryRequest represents an HTTP GET request to get the match history since a given time.
// Each entry in the match history contains the match ID (which can be used to find a more detailed description
// of the match).
class LoLMatchHistoryRequest(apiKey: String, region: String, val encryptedAccountId: String, val beginTime: String) :
    LoLRequest(apiKey, region) {

    override fun getUrl(): String {
        return "https://$region.$urlDomain/lol/match/$apiVersion/matchlists/by-account/$encryptedAccountId?beginTime=$beginTime"
    }

}