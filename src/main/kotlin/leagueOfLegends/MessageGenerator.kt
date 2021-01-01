package leagueOfLegends

object MessageGenerator {

    val LINE_BREAK = "%0A"

    fun generateMessage(performance: Performance): String {
        val potentialMessages = if (performance.win) generateWinMessage(performance) else generateDefeatMessage(performance)
        return "${generateGameSummary(performance)}$LINE_BREAK" +
               potentialMessages.random()
    }

    private fun generateGameSummary(performance: Performance): String {
        val championName = performance.champion.getString("name")
        return "You went ${performance.kills}/${performance.deaths}/${performance.assists} on $championName."
    }

    private fun generateDefeatMessage(performance: Performance): List<String> {
        val champion = performance.champion.getString("name")
        val individualScore = performance.individualScore
        val phrase = "No wonder you lost."
        return if (individualScore < 0.5) {
            listOf(
                "$phrase Are you intentionally losing games to start smurfing again?",
                "Here is a link to uninstall League of Legends: https://gamerone.gg/how-to-uninstall-league-of-legends." +
                        " Please follow the instructions.",
                "$phrase I don't think you should be playing $champion anymore.",
                "$phrase Please watch a tutorial on how to play $champion properly."
            )
        } else if (individualScore < 0.7) {
            listOf(
                "Solid game, but next time try to actually carry.",
                "If you want to climb the elo ladder, you'd better start carrying."
            )
        } else {
            listOf(
                "For once it's actually your teammates that are inting.",
                "Looks like you're smurfing.",
                "It's hard to carry a team to victory with $champion."
            )
        }
    }

    private fun generateWinMessage(performance: Performance): List<String> {
        val individualScore = performance.individualScore
        val phrase = "How did your team even win the game?"
        return if (individualScore < 0.5) {
            listOf(
                "$phrase Looks like you really know how to get carried.",
                "$phrase How can you play so badly when you are in such a good team?",
                "$phrase Boosted."
            )
        } else if (individualScore < 0.7) {
            listOf(
                "Solid game, looks like you don't belong in Bronze division after all."
            )
        } else {
            listOf(
                "GG EZ",
                "I think it's time to stop smurfing."
            )
        }
    }
}