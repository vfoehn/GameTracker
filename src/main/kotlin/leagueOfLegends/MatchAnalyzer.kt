package leagueOfLegends

import getField
import org.json.JSONArray
import org.json.JSONObject

class MatchAnalyzer(val match: JSONObject, val protagonistUsername: String) {

    lateinit var protagonist: JSONObject
    lateinit var protagonistTeam: JSONObject

    init {
        getProtagonistStats()
        getProtagonistTeam()
//        println(protagonist)
//        println(protagonistTeam)
    }

    private fun getProtagonistStats() {
        val participants = match["participantIdentities"] as JSONArray
        for (participant in participants) {
            val participantJson = participant as JSONObject
            if (participantJson.getField("player").getString("summonerName") == protagonistUsername) {
                val participantId = participantJson.getInt("participantId")
                protagonist = match.getJSONArray("participants")[participantId-1] as JSONObject
            }
        }
    }

    private fun getProtagonistTeam() {
        val protagonistTeamId = protagonist.getInt("teamId")
        val teams = match["teams"] as JSONArray
        for (team in teams) {
            val teamJson = team as JSONObject
            if (teamJson.getInt("teamId") == protagonistTeamId) {
                protagonistTeam = teamJson
            }
        }
    }

    fun getPerformance(): Performance {
        val protagonistStats = protagonist.getField("stats")
        val win = if (protagonistTeam.get("win") == "Win") true else false
        val champion = protagonist.getInt("championId")
        val performance = Performance(match, protagonistStats, protagonistTeam, win, champion)
        return performance
    }
}