package requests

// A LoLSummonerAccountRequest represents an HTTP GET request to get account information for a given username.
// The information includes PUUID (Player Universally Unique IDentifiers), account ID etc.
class LoLSummonerAccountRequest(apiKey: String, region: String, val username: String) : LoLRequest(apiKey, region) {

    override fun getUrl(): String {
        return "https://$region.$urlDomain/lol/summoner/$apiVersion/summoners/by-name/$username"
    }

}