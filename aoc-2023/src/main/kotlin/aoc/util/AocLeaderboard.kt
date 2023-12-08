package aoc.util

import aoc.AocRunner
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import java.time.LocalDate
import java.time.Month
import java.time.ZoneId
import kotlin.math.ceil

fun main() {
    getLeaderboards()
    printLeaderboard("leaderboard.json", highlight = "2651623")
    printLeaderboard("leaderboard2.json", highlight = "2651623", reduceFactor = 2)
}

private fun printLeaderboard(jsonFile: String, highlight: String? = null, reduceFactor: Int = 1) {
    println("-".repeat(BIN_COUNT + 20) + " Dec 2023")
    val json = AocRunner::class.java.getResource(jsonFile).readText()
    val leaderboard = ObjectMapper()
        .registerModule(KotlinModule.Builder().build())
        .readValue<AocLeaderboard>(json)
    val days = leaderboard.members.values.flatMap { it.completion_day_level.entries }
        .groupBy { it.key }
        .mapValues { it.value.map { it.value } }
    days.keys.map { it.toInt() }.sorted().forEach {
        val userBin = leaderboard.members[highlight]?.completion_day_level?.get(it.toString())
        printStars(2023, it, days[it.toString()]!!, userBin, reduceFactor)
        printStarIntervals(2023, it, days[it.toString()]!!, userBin, reduceFactor)
    }
}

//region DATA OBJECTS

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

//endregion

/** Print each individual star (first and second) in a bar chart. */
private fun printStars(year: Int, day: Int, info: List<AocDay>, userBin: AocDay?, reduceFactor: Int) {
    val date = LocalDate.of(year, Month.DECEMBER, day)
    val userBin1 = userBin?.let { binStar(date, it.puzzle1Ts) }
    val userBin2 = userBin?.puzzle2Ts?.let { binStar(date, it) }
    val p0Stars = mutableListOf<Int>()
    val p1Stars = mutableListOf<Int>()
    val p2Stars = mutableListOf<Int>()
    info.forEach {
        val b1 = binStar(date, it.puzzle1Ts)
        val b2 = it.puzzle2Ts?.let { binStar(date, it) }
        if (b1 == b2)
            p0Stars += b1
        else {
            p1Stars += b1
            if (b2 != null) p2Stars += b2
        }
    }
    val p0Bins = p0Stars.eachCount().mapValues { ceil(it.value.toDouble() / reduceFactor).toInt() }
    val p1Bins = p1Stars.eachCount().mapValues { ceil(it.value.toDouble() / reduceFactor).toInt() }
    val p2Bins = p2Stars.eachCount().mapValues { ceil(it.value.toDouble() / reduceFactor).toInt() }
    val totBins = (p0Stars + p1Stars + p2Stars).eachCount().mapValues { ceil(it.value.toDouble() / reduceFactor).toInt() }
    val maxCount = totBins.values.maxOrNull()!!
    (1..maxCount).forEach {
        val line = maxCount - it + 1
        (0 until BIN_COUNT).forEach {
            printStar(
                line, p0Bins[it] ?: 0, p1Bins[it] ?: 0, p2Bins[it] ?: 0, totBins[it] ?: 0,
                userBin1 == it, userBin2 == it
            )
        }
        print("|")
        (BIN_COUNT + 1..BIN_COUNT + 7).forEach {
            printStar(
                line, p0Bins[it] ?: 0, p2Bins[it] ?: 0, p2Bins[it] ?: 0, totBins[it] ?: 0,
                userBin1 == it, userBin2 == it
            )
        }
        println()
    }
    printAxis(BIN_COUNT, day, year)
}

/** Print stars while showing intervals between first and second. */
private fun printStarIntervals(year: Int, day: Int, info: List<AocDay>, userBin: AocDay?, reduceFactor: Int) {
    val date = LocalDate.of(year, Month.DECEMBER, day)
    val userBin1 = userBin?.let { binStar(date, it.puzzle1Ts) }
    val userBin2 = userBin?.puzzle2Ts?.let { binStar(date, it) }
    val userStars = info.filter { it != userBin }.sortedBy { it.puzzle1Ts }.map {
        binStar(date, it.puzzle1Ts) to it.puzzle2Ts?.let { binStar(date, it) }
    }
    val grid = mutableListOf<ChartInterval>()
    if (userBin1 != null) {
        grid.add(ChartInterval(0, userBin1, userBin2, ANSI_BRIGHT_GREEN))
    }
    userStars.sortedByDescending { (it.second ?: it.first) - it.first }
        .forEach { (p1, p2) -> grid.addInterval(p1, p2, ANSI_BRIGHT_YELLOW) }
    grid.map { it.row }.toSet().forEach {
        if (!grid.intersecting(it, BIN_COUNT, null)) {
            grid.add(ChartInterval(it, BIN_COUNT, null, ANSI_RESET))
        }
    }
    grid.groupBy { it.row }.toSortedMap().entries.reversed().onEach {
        var str = ""
        val intervals = it.value.sortedBy { it.start }
        var pos = -1
        intervals.indices.forEach {
            with (intervals[it]) {
                str += " ".repeat(start - pos - 1)
                if (color == ANSI_RESET) {
                    str += "|"
                } else if (start == end) {
                    str += "$color⊗$ANSI_RESET"
                } else if (end == null) {
                    str += if (color == ANSI_BRIGHT_GREEN) {
                        "$color∘$ANSI_RESET"
                    } else {
                        "$ANSI_GRAY∘$ANSI_RESET"
                    }
                } else {
                    str += if (color == ANSI_BRIGHT_GREEN) {
                        "$color»"
                    } else {
                        "$ANSI_GRAY»"
                    }
                    str += "»".repeat(interval.last - start - 1) + color + "*" + ANSI_RESET
                }
                pos = interval.last
            }
        }
        println(str)
    }
    printAxis(BIN_COUNT, day, year)
}

private class ChartInterval(val row: Int, val start: Int, val end: Int?, val color: String) {
    val interval = start..(end ?: start)
    fun intersects(start: Int, end: Int?) =
        interval.intersect(start..(end ?: start)).isNotEmpty()
}

private fun MutableList<ChartInterval>.addInterval(start: Int, end: Int?, color: String) {
    var row = 0
    while (intersecting(row, start, end)) {
        row++
    }
    add(ChartInterval(row, start, end, color))
}

private fun List<ChartInterval>.intersecting(row: Int, start: Int, end: Int?) =
    filter { it.row == row }
        .any { it.intersects(start, end) }

/** Print axis. */
private fun printAxis(bins: Int, day: Int, year: Int) {
    // print out every 6 hours with spaces in between
    println("-".repeat(bins + 6) + " Dec $day, $year")
    val spaceBetweenTicks = bins / 4
    print("0h")
    print("%${spaceBetweenTicks - 2}s".format("6h"))
    print("%${spaceBetweenTicks}s".format("12h"))
    print("%${spaceBetweenTicks}s".format("18h"))
    print("%${spaceBetweenTicks}s".format("24h"))
    print("|  >24h")
    println()
}

private const val BIN_COUNT = 48

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

private fun printStar(line: Int, p0: Int, p1: Int, p2: Int, tot: Int, user1: Boolean, user2: Boolean) {
    when {
        user1 && user2 && line == 1 ->
            print("$ANSI_BRIGHT_GREEN⊗$ANSI_RESET")
        user2 && !user1 && line == p0+1 ->
            print("$ANSI_BRIGHT_GREEN*$ANSI_RESET")
        user1 && !user2 && line == p2+1 ->
            print("$ANSI_BRIGHT_GREEN•$ANSI_RESET")
        line in (1..p0) ->
            print("$ANSI_BRIGHT_YELLOW⊗$ANSI_RESET")
        line in (p0+1..p0+p2) ->
            print("$ANSI_BRIGHT_YELLOW*$ANSI_RESET")
        line in (p2+1..tot) ->
            print("$ANSI_GRAY•$ANSI_RESET")
        else -> print(" ")
    }
}

private fun List<Int>.eachCount() = groupBy { it }.mapValues { it.value.size }