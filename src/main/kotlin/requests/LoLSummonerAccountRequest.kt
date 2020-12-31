package requests

class LoLSummonerAccountRequest(apiKey: String, region: String, val username: String) : LoLRequest(apiKey, region) {

    override fun getUrl(): String {
        return "https://$region.$urlDomain/lol/summoner/$apiVersion/summoners/by-name/$username"
    }

}