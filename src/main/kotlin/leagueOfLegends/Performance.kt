package leagueOfLegends

import org.json.JSONObject
import kotlin.math.exp

// A Performance contains all the information about a given user in a given match. This information includes
// the number of kills, deaths etc.
// This class can be used to set rules that are used for determining whether a performance is considered good
// or not.
class Performance(val match: JSONObject, val protagonistStats: JSONObject, val protagonistTeam: JSONObject,
                  val antagonistStats: JSONObject?, val champion: JSONObject, val win: Boolean) {

    val kills = protagonistStats.getInt("kills")
    val deaths = protagonistStats.getInt("deaths")
    val assists = protagonistStats.getInt("assists")
    val kda = computeSafeRatio(kills.toDouble(), deaths.toDouble())
    val individualScore = computeIndividualScore(kda)
    var performanceScore: Double = computePerformance()
    var isPoor: Boolean = isPoorPerformance()

    private fun isPoorPerformance(): Boolean {
        return performanceScore < 0.3
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
        val teamScore = if (win) 1 else 0
        return (teamScore + 2 * individualScore) / 3
    }

    private fun computeIndividualScore(localKda: Double): Double {
        val kdaScore = computeNormalizedScore(localKda)
        return if (antagonistStats != null) {
            val duelScore = computeDuelScore()
            (kdaScore + duelScore) / 2
        } else {
            kdaScore
        }
    }

    private fun computeDuelScore(): Double {
        // The following branch is required to smart-cast "antagonistStats" to a non-null type.
        if (antagonistStats == null) {
            return -1.0
        }
        val kda1 = kda
        val kda2 = computeSafeRatio(antagonistStats.getDouble("kills"), antagonistStats.getDouble("deaths"))
        val kdaDuelScore = computeNormalizedScore(computeSafeRatio(kda1, kda2))

        val goldEarned1 = protagonistStats.getDouble("goldEarned")
        val goldEarned2 = antagonistStats.getDouble("goldEarned")
        val goldEarnedDuelScore = computeNormalizedScore(computeSafeRatio(goldEarned1, goldEarned2))

        return (kdaDuelScore + goldEarnedDuelScore) / 2
    }

    private fun computeSafeRatio(x: Double, y: Double): Double {
        return if (y == 0.0) {
            if (x == 0.0) 1.0 else x
        } else {
            x / y
        }
    }

    /*
     * To plot the function run "Plot[Piecewise[{{x/2,  0 <= x < 1}, {1/(1+exp(1-x)), x > 0}}], {x, 0, 10}]"
     * on https://www.wolframalpha.com/.
     * Intuition:
     *  - As the KDA goes towards 0, the score goes towards 0
     *  - If the KDA is 1, the score is 0.5
     *  - As the KDA goes towards infinity, the score goes towards 1.
     */
    private fun computeNormalizedScore(value: Double) =
        if (value <= 1)  value/2 else 1/(1+ exp(1-value))

    override fun toString(): String {
        "You went $kills/$deaths/$assists on $champion"
        return "win: $win, kills: $kills, deaths: $deaths]"
    }
}