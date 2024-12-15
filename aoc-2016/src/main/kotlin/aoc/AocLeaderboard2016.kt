package aoc

import AocStats.printStats
import aoc.report.AocSite.resourceFile
import aoc.report.printLeaderboard

object AocLeaderboard2016 {
    @JvmStatic
    fun main(args: Array<String>) {
        printLeaderboard(2016)
        // read personal times from text
        val personalTimesText = resourceFile(2016, "personal_times.txt").readText()
            .substringAfter("--------Part 2--------").trim()
        printStats(personalTimesText)
    }
}