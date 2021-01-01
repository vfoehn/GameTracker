package leagueOfLegends

import getField
import org.json.JSONArray
import org.json.JSONObject

// A MatchDataOrganizer organizes the data for a given user (i.e., protagonist) in a given match.
// The match data is provided in the shape of JSONObjects. It is the MatchDataOrganizer's job to extract
// the relevant data and to combine different JSONs.
class MatchDataOrganizer(val match: JSONObject, val champions: JSONObject, val protagonistUsername: String) {

    fun getPerformance(): Performance {
        val protagonist = getProtagonist()
        val protagonistStats = protagonist.getField("stats")
        val (protagonistTeam, antagonistTeam) = getTeams(protagonist)

        // The antagonist is the player on the opposing team that has the same position as the protagonist.
        // The notion of "positions" only exists in some game modes. Therefore, we can only define a clear
        // antagonist some game modes.
        val antagonistStats = getAntagonist(protagonist, antagonistTeam)

        val win = protagonistTeam.get("win") == "Win"
        val champion = getChampion(protagonist.getInt("championId"))
        val performance = Performance(match, protagonistStats, protagonistTeam, antagonistStats, champion, win)
        return performance
    }

    private fun getProtagonist(): JSONObject {
        val participants = match["participantIdentities"] as JSONArray
        var protagonist = JSONObject()
        for (participant in participants) {
            val participantJson = participant as JSONObject
            if (participantJson.getField("player").getString("summonerName") == protagonistUsername) {
                val participantId = participantJson.getInt("participantId")
                protagonist = match.getJSONArray("participants")[participantId-1] as JSONObject
                break
            }
        }
        return protagonist
    }

    // Returns a pair of teams. The first component is the team of the protagonist. The second component
    // is the the opposing team.
    private fun getTeams(protagonist: JSONObject): Pair<JSONObject, JSONObject> {
        val protagonistTeamId = protagonist.getInt("teamId")
        val teams = match["teams"] as JSONArray
        val team1 = teams[0] as JSONObject
        val team2 = teams[1] as JSONObject
        if (team1.getInt("teamId") == protagonistTeamId) {
            return team1 to team2
        } else {
            return team2 to team1
        }
    }

    private fun getAntagonist(protagonist: JSONObject, antagonistTeam: JSONObject): JSONObject? {
        val antagonistTeamId = antagonistTeam.getInt("teamId")
        var antagonistStats = JSONObject()
        val participants = match["participants"] as JSONArray
        for (participant in participants) {
            val participantJson = participant as JSONObject
            if (samePosition(protagonist.getField("timeline"), participantJson.getField("timeline")) &&
                participantJson.getInt("teamId") == antagonistTeamId) {
                println(protagonist)
                println(participantJson)
                println()
                antagonistStats = participantJson.getField("stats")
                break
            }
        }
        return antagonistStats
    }

    private fun samePosition(player1: JSONObject, player2: JSONObject): Boolean {
        return player1.getString("role") == player2.getString("role") &&
               player1.getString("lane") == player2.getString("lane")
    }

    private fun getChampion(championId: Int): JSONObject {
        var result = JSONObject()
        for (champion in champions.keySet()) {
            val championJson = champions.getField(champion)
            if (championJson.getString("key").toInt() == championId) {
                result = championJson
                break
            }
        }
        return result
    }

}