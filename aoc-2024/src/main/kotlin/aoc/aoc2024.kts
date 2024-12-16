import aoc.util.*
import java.io.File
import java.time.LocalDate
import java.time.Month

private val YEAR = 2024
val COLORA = ANSI_B_BLUE
val COLORB = ANSI_B_PURPLE
val COLORC = ANSI_B_PINK

val leaders = """
--100th Best Times for Each Puzzle--
Day16: 2:40/13:47 (classic maze pathfinding)
Day15: 9:30/32:00 (pushing boxes)
Day14: 3:06/15:48 (robot easter egg)
Day13: 3:37/11:04 (integer programming)
Day12: 2:04/17:42 (grid regions)
Day11: 1:36/6:24 (integer duplicators)
Day10: 2:07/4:14 (simple pathfinding)
Day9: 5:51/14:05 (defrag checksums)
Day8: 3:57/7:12 (antenna grid)
Day7: 1:59/3:47 (inserting operators)
Day6: 2:40/8:53 (paths in grid)
Day5: 1:58/3:43 (ordering rules)
Day4: 1:30/5:41 (xmas word search)
Day3: 1:24/3:22 (parsing calculations)
Day2: 3:02/4:42 (monotonic sequences)
Day1: 1:24/2:31 (sort similarity)
""".trimIndent()

val personalstats = """
Day       Time   Rank   Score       Time    Rank  Score InputFile
 16   07:36:19   12648      0   08:18:34    8895      0  06:46:41
 15   07:51:56   17842      0   08:23:59   10077      0  07:21:24
 14   06:50:12   18194      0   07:19:19   14422      0  06:39:35
 13   06:45:12   19126      0   07:33:15   14939      0  06:30:27
 12   07:25:24   21029      0   07:49:46   11686      0  07:08:48
 11   07:05:24   28994      0   07:17:31   19360      0  06:57:14
 10   10:51:22   30257      0   10:52:10   29045      0  10:40:01
  9   07:23:54   25619      0   07:32:19   16224      0  06:46:11
  8   07:35:39   26750      0   07:37:39   24005      0  07:26:27
  7   08:01:32   29681      0   08:10:16   27082      0  07:31:48
  6   07:04:43   34915      0   31:50:42   46903      0  06:57:41
  5   08:04:59   40462      0   08:10:09   31847      0  07:55:36
  4   07:33:53   41264      0   07:43:03   35252      0  07:24:02
  3   09:53:26   65231      0   10:02:58   54671      0  09:49:45
  2   12:10:20   78024      0   12:13:02   55710      0  12:03:13
  1   35:55:16  135368      0   35:59:44  126387      0  35:48:12
""".trimIndent().lines().drop(1).map {
    val columns = it.trim().split("\\s+".toRegex())
    Data(columns[0].toInt(), columns[1], columns[2].toInt(), columns[4], columns[5].toInt(), columns[7])
}

personalstats.printChart()
personalstats.printRankStats()
personalstats.printTimeStats()

printHeader("Updating Files")
updateFiles()
println("Done!")

fun updateFiles() {
    val rootPath = File(File("").absolutePath.substringBeforeLast("aoc-$YEAR") + "aoc-$YEAR")
    val codePath = File(rootPath, "src/main/kotlin/aoc/")
    val day = LocalDate.now().let {
        if (it.year == YEAR && it.month == Month.DECEMBER) minOf(it.dayOfMonth + 1, 25) else -1
    }
    if (day != -1) {
        val codeTemplate = File(codePath, "aoc_.kts").readText().replace("day = 0", "day = $day")
        val inputCode = File(codePath, "aoc$day.kts")
        if (!inputCode.exists()) {
            inputCode.writeText(codeTemplate)
            println("Created file ${inputCode.name}")
        }
    }
}

//region UTILS

fun printHeader(str: String) {
    println("\n---$str${ANSI_RESET}---")
}

fun List<*>.printSummary() {
    println("${ANSI_LIGHT_GREEN}Best:   $ANSI_BOLD${first()}${ANSI_RESET}")
    println("${ANSI_LIGHT_YELLOW}Median: $ANSI_BOLD${this[size / 2]}${ANSI_RESET}")
    println("${ANSI_BROWN}Worst:  $ANSI_BOLD${last()}${ANSI_RESET}")
}

fun List<*>.improvementSummary() {
    println("${ANSI_LIGHT_GREEN}Largest drop: $ANSI_BOLD${first()}${ANSI_RESET}")
    println("${ANSI_LIGHT_YELLOW}Median drop:  $ANSI_BOLD${this[size / 2]}${ANSI_RESET}")
    println("${ANSI_BROWN}Lowest drop:  $ANSI_BOLD${last()}${ANSI_RESET}")
}

fun List<Data>.printRankStats() {
    printHeader("Part 1 Ranks")
    map { it.rank }.sorted().printSummary()
    map { it.rank }.printHistogram(bucketSize = 5000, overflow = 65000)

    printHeader("Part 2 Ranks")
    map { it.rank2 }.sorted().printSummary()
    map { it.rank2 }.printHistogram(bucketSize = 5000, overflow = 125000)

    printHeader("Number of Ranks Dropped")
    map { it.rank - it.rank2 }.sortedDescending().improvementSummary()
    map { it.rank - it.rank2 }.printHistogram(min = -10000, bucketSize = 2000, overflow = 22000)
}

fun List<Data>.printTimeStats() {
    printHeader("Time to Solve Part 1")
    map { it.timePart1 }.printHistogramMinutes(bucketSize = 2, overflow = 60)

    printHeader("Time to Solve Part 2")
    map { it.timePart2 }.printHistogramMinutes(bucketSize = 2, overflow = 60)

    printHeader("Time to Solve Both")
    map { it.timeBoth }.printHistogramMinutes(bucketSize = 5, overflow = 120)
}

//region CHART PRINTERS

fun List<Int>.printHistogram(min: Int? = 0, bucketSize: Int = 1000, overflow: Int = Int.MAX_VALUE) {
    val SIZE = 3
    if (isEmpty()) {
        println("EMPTY")
        return
    }
    val min = min ?: min()
    val max = minOf(max(), overflow)
    val range = max - min
    val buckets = IntArray(range / bucketSize + 1)
    var ucount = 0
    var ocount = 0
    forEach {
        when {
            it < min -> ucount++
            it >= overflow -> ocount++
            else -> buckets[(it - min) / bucketSize]++
        }
    }
    if (ucount > 0) {
        val ubar = "*".repeat(ucount * SIZE)
        println("     < %6d: $ubar".format(min))
        if (buckets[0] == 0)
            println(".".repeat(13))
    }
    buckets.withIndex().dropWhile { it.value == 0 }.dropLastWhile { it.value == 0 }
        .forEach { (i, count) ->
            val rangeStart = min + i * bucketSize
            val rangeEnd = rangeStart + bucketSize
            val bar = "*".repeat(count * SIZE)
            println("%5d — %5d: $bar".format(rangeStart, rangeEnd - 1))
        }
    if (ocount > 0) {
        if (buckets.last() == 0)
            println(".".repeat(13))
        val obar = "*".repeat(ocount * SIZE)
        println("%5d +      : $obar".format(overflow))
    }
}

fun List<Int>.printHistogramMinutes(min: Int? = 0, bucketSize: Int = 1, overflow: Int = 60) {
    if (isEmpty()) {
        println("EMPTY")
        return
    }
    val numMin = min()
    val numMed = sorted()[size / 2]
    val numMax = max()
    val useMin = min ?: (numMin / 60)
    val useMax = filter { it < overflow * 60 }.maxOrNull()?.let { it / 60 } ?: useMin
    val range = useMax - useMin
    val buckets = IntArray(range / bucketSize + 1)
    var nOver = 0
    forEach {
        val bin = (it - useMin) / (bucketSize * 60)
        if (bin in buckets.indices)
            buckets[bin]++
        else
            nOver++
    }
    buckets.forEachIndexed { i, count ->
        val rangeStart = useMin + i * bucketSize
        val rangeEnd = rangeStart + bucketSize
        val bar = "*".repeat(count * 3)
        print("%2d:00 — %2d:59: $bar".format(rangeStart, rangeEnd-1))
        if (numMin/60 in rangeStart until rangeEnd)
            print("  $ANSI_BOLD${ANSI_LIGHT_GREEN}BEST: ${numMin.minSec()}$ANSI_RESET")
        if (numMed/60 in rangeStart until rangeEnd)
            print("  $ANSI_BOLD${ANSI_LIGHT_YELLOW}MEDIAN: ${numMed.minSec()}$ANSI_RESET")
        if (numMax/60 in rangeStart until rangeEnd)
            print("  $ANSI_BOLD${ANSI_BROWN}WORST: ${numMax.minSec()}$ANSI_RESET")
        println()
    }
    if (nOver > 0) {
        print("%2d:00 +     : ${"***".repeat(nOver)}".format(useMin + buckets.size * bucketSize))
        if (numMax >= overflow * 60)
            print("  $ANSI_BOLD${ANSI_BROWN}WORST: ${numMax.hms()}$ANSI_RESET")
        println()
    }
}

fun List<Data>.printChart() {
    val rankOp = { it: Number -> kotlin.math.log2(it.toDouble()) }

    val sortedRanks = (map { it.rank } + map { it.rank2 }).map(rankOp).sorted()
    val rankMin = sortedRanks.first()
    val rankMax = sortedRanks.last()

    val chartHeight = 20
    val rankRatio = (rankMax - rankMin) / chartHeight.toDouble()

    val header = "%-3s | %-${chartHeight + 9}s | %-${chartHeight + 9}s |".format("Day", "Part 1", "Part 2")
    println(header)
    println("-".repeat(header.length))
    map { data ->
        val barHeight1 = ((rankOp(data.rank) - rankMin) / rankRatio).toInt().coerceAtLeast(0)
        val barHeight2 = ((rankOp(data.rank2) - rankMin) / rankRatio).toInt().coerceAtLeast(0)

        val bar1 = "*".repeat(barHeight1)
        val bar2 = "*".repeat(barHeight2)

        val line = "%-3s | %${chartHeight + 9}s | %-${chartHeight + 9}s |".format(
            data.day,
            "[${data.rank}] $bar1",
            "$bar2 [${data.rank2}]"
        )
            .replace("[", "$ANSI_WHITE[")
            .replace("]", "]$ANSI_RESET")
            .colorStars()
        println(line)
    }
}

//endregion

//region ANSI UTILS AND FORMATTERS

fun String.colorStars(): String {
    // find each substring of stars, and add a color code before each, then an ansi reset after the last star
    val find = "\\*+".toRegex()
    var result = ""
    var pos = 0
    find.findAll(this).forEach {
        result += substring(pos, it.range.first)
        val range = it.range
        val size = range.last - range.first + 1
        result += (1..size/3+1).flatMap { listOf("$COLORA*", "$COLORB*", "$COLORC*") }
            .take(size)
            .joinToString("") + ANSI_RESET
        pos = range.last + 1
    }
    result += substring(pos)
    return result
}

fun Int.hms() = if (this >= 3600) {
    "%02d:%02d:%02d".format(this / 3600, (this % 3600) / 60, this % 60)
} else {
    "%5d:%02d".format(this / 60, this % 60)
}

fun Int.minSec() = "%2d:%02d".format(this / 60, this % 60)

//endregion

data class Data(
    val day: Int,
    val time: String,
    val rank: Int,
    val time2: String,
    val rank2: Int,
    val timeFile: String
) {
    val timePart1 = time.parseTime() - timeFile.parseTime()
    val timePart2 = time2.parseTime() - time.parseTime()
    val timeBoth = timePart1 + timePart2

    private fun String.parseTime() = split(':').map { it.toInt() }.let { (h, m, s) -> h * 3600 + m * 60 + s }
}

//endregion