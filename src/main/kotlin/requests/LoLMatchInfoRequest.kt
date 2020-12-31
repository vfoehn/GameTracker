package requests

class LoLMatchInfoRequest(apiKey: String, region: String, val gameId: String) : LoLRequest(apiKey, region) {

    override fun getUrl(): String {
        return "https://$region.$urlDomain/lol/match/$apiVersion/matches/$gameId"
    }

}