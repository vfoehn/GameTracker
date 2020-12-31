package leagueOfLegends

import org.json.JSONObject

// A Performance contains all the information about a given user in a given match. This information includes
// the number of kills, deaths etc.
// This class can be used to set rules that are used for determining whether a performance is considered good
// or not.
class Performance(val match: JSONObject, val protagonistStats: JSONObject, val protagonistTeam: JSONObject,
                  val win: Boolean, val championId: Int) {

    val kills = protagonistStats.getInt("kills")
    val deaths = protagonistStats.getInt("deaths")
    val kda = kills.toDouble() / Math.max(1, deaths)
    var isPoor: Boolean = isPoorPerformance()

    private fun isPoorPerformance(): Boolean {
        // If the protagonistStats lost the game and had a negative kda, it is considered a bad performance.
        return !win && kda < 1
    }

    override fun toString(): String {
        return "[gameId: ${match.getLong("gameId")}, win: $win, kills: $kills, deaths: $deaths]"
    }
}