package leagueOfLegends

import org.json.JSONObject

// A Performance contains all the information about a given user in a given match. This information includes
// the number of kills, deaths etc.
// This class can be used to set rules that are used for determining whether a performance is considered good
// or not.
class Performance(val match: JSONObject, val protagonistStats: JSONObject, val protagonistTeam: JSONObject,
                  val antagonistStats: JSONObject?, val champion: JSONObject, val win: Boolean) {

    val kills = protagonistStats.getInt("kills")
    val deaths = protagonistStats.getInt("deaths")
    val kda = kills.toDouble() / Math.max(1, deaths)
    var performanceScore: Double = computePerformance()
    var isPoor: Boolean = isPoorPerformance()

    private fun isPoorPerformance(): Boolean {
        // If the protagonistStats lost the game and had a negative kda, it is considered a bad performance.
        return true //TODO: Remove
//        return !win && kda < 1
    }

    /*
     * The performance is computed based on three criteria:
     *  1. Did the user win the game?
     *  2. The protagonist's KDA (kill death ration)
     *  3. The difference between the protagonist's and the antagonist's performance
     *  Each aspect is scored on a scale of 0 to 1. The weighted average of all aspects is the final performance.
     *
     * Note: The notion of "positions" only exists in some game modes. Therefore, we can only define a clear
     * antagonist some game modes. If there is no antagonist, the third criteria of the performance evaluation
     * is ignored.
     */
    private fun computePerformance(): Double {
        val winScore = if (win) 1 else 0
        val kdaScore = computeKdaScore()
        if (antagonistStats != null) {
            val directDuelScore =
        }


        return score
    }

    // To plot the function run "Plot[Piecewise[{{x/2,  0 <= x < 1}, {1/(1+exp(1-x)), x > 0}}], {x, 0, 10}]"
    // on https://www.wolframalpha.com/.
    private fun computeKdaScore() =
        if (kda <= 1)  kda/2 else 1/(1+Math.exp(1-kda))

    private fun computeDirectDuelScore() {


    }

    override fun toString(): String {
        return "[gameId: ${match.getLong("gameId")}, win: $win, kills: $kills, deaths: $deaths]"
    }
}