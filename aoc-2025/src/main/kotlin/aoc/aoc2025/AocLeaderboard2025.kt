package aoc.aoc2025

import aoc.report.AocStats.printStats
import aoc.report.AocSite.resourceFile
import aoc.report.printLeaderboard

object AocLeaderboard2025 {
    @JvmStatic
    fun main(args: Array<String>) {
        printLeaderboard(2025)
        // read personal times from text
        val personalTimesText = resourceFile(2025, "site/stats/personaltimes.txt").readText()
            .substringAfter("Part 2").substringAfter("\n").trim()
        printStats(personalTimesText)
    }
}