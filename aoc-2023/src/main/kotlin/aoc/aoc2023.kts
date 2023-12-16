import aoc.util.ANSI_LIGHT_BLUE
import aoc.util.ANSI_RESET
import aoc.util.ANSI_WHITE
import aoc.util.randomBlueWhite
import java.io.File
import java.time.LocalDate
import java.time.Month

val leaders = """
--100th Best Times for Each Puzzle--
Day16: 11:36/15:30 (laser grids)
Day15: 2:10/11:04 (hashmap)
Day14: 4:10/17:15 (rock and roll grids) 
Day13: 8:58/13:46 (reflecting grids)
Day12: 8:12/22:57 (matching characters)
Day11: 6:07/9:18 (expanding galaxies)
Day10: 11:33/36:31 (snake maze)
Day9: 4:02/5:36 (adding number sequences)
Day8: 3:30/10:16 (lcm's)
Day7: 9:57/16:00 (poker hands)
Day6: 3:11/5:02 (quadratic boats)
Day5: 8:15/26:37 (mapping ranges)
Day4: 2:51/7:08 (winning numbers)
Day3: 7:09/11:37 (grid numbers/symbols)
Day2: 4:10/6:15 (parsing input)
Day1: 1:39/7:03 (extracting digits)
""".trimIndent()

val personalstats = """
Day       Time   Rank  Score       Time   Rank  Score InputFile
 16   06:28:06  12441      0   06:34:38  11454      0   6:12:21
 15   07:59:25  22455      0   08:10:21  18166      0   7:55:37
 14   06:40:41  17984      0   07:09:13  11628      0   6:33:37
 13   05:53:26  14036      0   06:00:33  10742      0   5:34:52
 12   05:25:00  12897      0   33:20:00  18228      0   5:12:56
 11   06:20:08  20526      0   06:27:08  18361      0   6:06:58
 10   05:40:39  16632      0   05:59:14   7738      0   5:28:38
  9   06:57:31  27348      0   07:04:31  26444      0   6:48:20
  8   05:15:02  29264      0   05:23:24  18001      0   5:08:53
  7   06:00:31  25809      0   06:16:35  20651      0   5:32:24
  6   06:25:54  36207      0   06:30:27  34923      0   6:18:54
  5   06:42:10  30775      0   07:15:42  14406      0   6:21:56
  4   07:19:24  47716      0   07:30:24  37456      0   7:11:05
  3   05:52:13  27871      0   05:56:34  21730      0   5:38:09
  2   07:06:01  44750      0   07:08:30  41246      0   6:56:28
  1   07:36:34  61695      0   07:36:39  37712      0   7:11:11   #NOTE - times here are off since I switched accounts
""".trimIndent().lines().drop(1).map {
    val columns = it.trim().split("\\s+".toRegex())
    Data(columns[0].toInt(), columns[1], columns[2].toInt(), columns[4], columns[5].toInt(), columns[7])
}

personalstats.printChart()

fun printHeader(str: String) {
    println("\n---${str}---".randomBlueWhite() + ANSI_RESET)
}

printHeader("Part 1 Ranks")
personalstats.map { it.rank }.sorted().printSummary()

printHeader("Part 2 Ranks")
personalstats.map { it.rank2 }.sorted().printSummary()

printHeader("Number of Ranks Dropped")
// drop 1 invalid time this year
personalstats.map { it.rank - it.rank2 }.sortedDescending().drop(1).improvementSummary()

printHeader("Time to Solve Part 1")
personalstats.map { it.timePart1 }.sorted().map { it.hms() }.printSummary()

printHeader("Time to Solve Part 2")
// drop 1 invalid time this year
personalstats.map { it.timePart2 }.sorted().drop(1).map { it.hms() }.printSummary()

printHeader("Time to Solve Both")
personalstats.map { it.timeBoth }.sorted().map { it.hms() }.printSummary()

printHeader("Updating Files")
val rootPath = File(File("").absolutePath.substringBeforeLast("aoc-2023")+"aoc-2023")
val codePath = File(rootPath, "src/main/kotlin/aoc/")
val day = LocalDate.now().let {
    if (it.year == 2023 && it.month == Month.DECEMBER) minOf(it.dayOfMonth + 1, 25) else -1
}
if (day != -1) {
    val codeTemplate = File(codePath, "aoc_.kts").readText().replace("day = 0", "day = $day")
    val inputCode = File(codePath, "aoc$day.kts")
    if (!inputCode.exists()) {
        inputCode.writeText(codeTemplate)
        println("Created file ${inputCode.name}")
    }
}

//region UTILS

fun List<*>.printSummary() {
    println("Best:   ${first()}")
    println("Median: ${this[size / 2]}")
    println("Worst:  ${last()}")
}

fun List<*>.improvementSummary() {
    println("Largest drop: ${first()}")
    println("Median drop:  ${this[size / 2]}")
    println("Lowest drop:  ${last()}")
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

        val line = "%-3s | %${chartHeight + 9}s | %-${chartHeight + 9}s |".format(data.day,
            "[${data.rank}] $bar1",
            "$bar2 [${data.rank2}]"
        )
            .replace("[", "$ANSI_WHITE[")
            .replace("]", "]$ANSI_RESET")
            .replace("**", "$ANSI_LIGHT_BLUE*$ANSI_WHITE*")
            .replace("* |", "*$ANSI_RESET |")
        println(line)
    }
}

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

fun Int.hms() = if (this >= 3600) {
    "%02d:%02d:%02d".format(this / 3600, (this % 3600) / 60, this % 60)
} else {
    "%5d:%02d".format(this / 60, this % 60)
}

//endregion