package requests

// A LoLMatchInfoRequest represents an HTTP GET request to get information about a given match.
// This information includes statistics about the teams as well as the individual players.
class LoLMatchInfoRequest(apiKey: String, region: String, val gameId: String) : LoLRequest(apiKey, region) {

    override fun getUrl(): String {
        return "https://$region.$urlDomain/lol/match/$apiVersion/matches/$gameId"
    }

}