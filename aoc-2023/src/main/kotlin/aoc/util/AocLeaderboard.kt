package aoc.util

import aoc.AocRunner
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import java.time.LocalDate
import java.time.Month
import java.time.ZoneId

/** Parses leaderboard.json */
class AocLeaderboard(
    val event: String,
    val owner_id: Int,
    val members: Map<String, AocMember>
)

class AocMember(
    val name: String?,
    val id: Int,
    val stars: Int,
    val local_score: Int,
    val global_score: Int,
    val last_star_ts: Long,

    val completion_day_level: Map<String, AocDay>
)

class AocDay(
    val puzzle1Index: Int,
    val puzzle1Ts: Long,
    val puzzle2Index: Int?,
    val puzzle2Ts: Long?
) {
    @JsonCreator
    constructor(`1`: Map<String, Long>, `2`: Map<String, Long> = mapOf()) : this(
        `1`["star_index"]!!.toInt(),
        `1`["get_star_ts"]!!,
        `2`["star_index"]?.toInt(),
        `2`["get_star_ts"]
    )
}

fun main() {
    val json = AocRunner::class.java.getResource("input/leaderboard.json").readText()
    val leaderboard = ObjectMapper()
        .registerModule(KotlinModule.Builder().build())
        .readValue<AocLeaderboard>(json)
    val days = leaderboard.members.values.flatMap { it.completion_day_level.entries }
        .groupBy { it.key }
        .mapValues { it.value.map { it.value } }
    days.keys.map { it.toInt() }.sorted().forEach {
        val userBin = leaderboard.members["2651623"]!!
            .completion_day_level[it.toString()]!!
        printStars(2023, it, days[it.toString()]!!, userBin)
    }
}

val BIN_COUNT = 48

private fun binStar(date: LocalDate, ts: Long): Int {
    val BIN_SECONDS = 24*60*60 / BIN_COUNT
    // get unix timestamp at start of day in eastern us time zone
    val startTs = date.atStartOfDay(ZoneId.of("America/New_York")).toEpochSecond()
    val seconds = ts - startTs
    var bin = (seconds / BIN_SECONDS).toInt()

    // if solved after 24h, bin by number of days instead for up to a week
    if (bin >= BIN_COUNT) {
        bin = BIN_COUNT + (seconds/(24*60*60)).toInt().coerceAtMost(7)
    }
    return bin
}

private fun List<Int>.eachCount() = groupBy { it }.mapValues { it.value.size }

private fun printStar(line: Int, p1: Int, tot: Int, user1: Boolean, user2: Boolean) {
    when {
        line == p1+1 && user2 ->
            print("$ANSI_BRIGHT_GREEN*$ANSI_RESET")
        line in (p1 + 1..tot) ->
            print("$ANSI_BRIGHT_YELLOW*$ANSI_RESET")
        line == 1 && user1 ->
            print("$ANSI_BRIGHT_GREEN•$ANSI_RESET")
        line in (1..p1) ->
            print("$ANSI_GRAY•$ANSI_RESET")
        else -> print(" ")
    }
}

private fun printStars(year: Int, day: Int, info: List<AocDay>, userBin: AocDay) {
    val date = LocalDate.of(year, Month.DECEMBER, day)
    val userBin1 = binStar(date, userBin.puzzle1Ts)
    val userBin2 = userBin.puzzle2Ts?.let { binStar(date, it) }
    val p1Stars = info.map { it.puzzle1Ts }.map { binStar(date, it) }
    val p2Stars = info.mapNotNull { it.puzzle2Ts }.map { binStar(date, it) }
    val p1Bins = p1Stars.eachCount()
    val p2Bins = p2Stars.eachCount()
    val totBins = (p1Stars + p2Stars).eachCount()
    val maxCount = totBins.values.maxOrNull()!!
    (1..maxCount).forEach {
        val line = maxCount - it + 1
        (0 until BIN_COUNT).forEach {
            printStar(line, p1Bins[it] ?: 0, totBins[it] ?: 0,
                userBin1 == it, userBin2 == it)
        }
        print("|")
        (BIN_COUNT+1..BIN_COUNT+7).forEach {
            printStar(line, p2Bins[it] ?: 0, totBins[it] ?: 0,
                userBin1 == it, userBin2 == it)
        }
        println()
    }
    println("-".repeat(BIN_COUNT + 6) + " Dec $day, $year")

    // print out every 6 hours with spaces in between
    val spaceBetweenTicks = BIN_COUNT / 4
    print("0h")
    print("%${spaceBetweenTicks - 2}s".format("6h"))
    print("%${spaceBetweenTicks}s".format("12h"))
    print("%${spaceBetweenTicks}s".format("18h"))
    print("%${spaceBetweenTicks}s".format("24h"))
    print("|  >24h")
    println()
}