import aoc.report.randomColors
import aoc.util.*
import java.io.File
import java.time.LocalDate
import java.time.Month

val COLORA = ANSI_B_BLUE
val COLORB = ANSI_B_PURPLE
val COLORC = ANSI_B_PINK

val leaders = """
--100th Best Times for Each Puzzle--
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
  8   07:35:39   26750      0   07:37:39   24005      0  07:26:27
  7   08:01:32   29681      0   08:10:16   27082      0  07:31:48
  6   07:04:43   34915      0   31:50:42   46903      0  06:57:41
  5   08:04:59   40462      0   08:10:09   31847      0  07:55:36
  4   07:33:53   41264      0   07:43:03   35252      0  07:24:02
  3   09:53:26   65231      0   10:02:58   54671      0  09:49:45
  2   12:10:20   78024      0   12:13:02   55710      0  12:03:13
  1   36:10:00  135368      0   36:10:00  126387      0  35:48:12
""".trimIndent().lines().drop(1).map {
    val columns = it.trim().split("\\s+".toRegex())
    Data(columns[0].toInt(), columns[1], columns[2].toInt(), columns[4], columns[5].toInt(), columns[7])
}

personalstats.printChart()

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

val rootPath = File(File("").absolutePath.substringBeforeLast("aoc-2024")+"aoc-2024")
val codePath = File(rootPath, "src/main/kotlin/aoc/")
val day = LocalDate.now().let {
    if (it.year == 2024 && it.month == Month.DECEMBER) minOf(it.dayOfMonth + 1, 25) else -1
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

fun printHeader(str: String) {
    println("\n---${str.randomColors(COLORA, COLORB, COLORC)}${ANSI_RESET}---")
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