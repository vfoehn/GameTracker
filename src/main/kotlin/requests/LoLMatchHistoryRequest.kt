package requests

class LoLMatchHistoryRequest(apiKey: String, region: String, val encryptedAccountId: String, val beginTime: String) :
    LoLRequest(apiKey, region) {

    override fun getUrl(): String {
        return "https://$region.$urlDomain/lol/match/$apiVersion/matchlists/by-account/$encryptedAccountId?beginTime=$beginTime"
    }

}