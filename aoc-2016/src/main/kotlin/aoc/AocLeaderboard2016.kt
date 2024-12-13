package aoc

import AocStats.printStats
import aoc.input.rootDir
import aoc.report.printLeaderboard

object AocLeaderboard2016 {
    @JvmStatic
    fun main(args: Array<String>) {
        printLeaderboard(2016)
        // read personal times from text
        val personalTimesText = rootDir(2016).resolve("src/main/resources/aoc/personal_times.txt").readText()
        printStats(personalTimesText)
    }
}