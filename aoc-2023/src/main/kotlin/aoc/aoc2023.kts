import aoc.util.ANSI_LIGHTBLUE
import aoc.util.ANSI_RESET
import aoc.util.ANSI_WHITE
import aoc.util.randomBlueWhite
import java.io.File
import java.time.LocalDate
import java.time.Month

val leaders = """
Day5: 1:15/2:05 (quadratic boats)
Day5: 1:35/8:38 (mapping ranges)
Day4: 0:43/1:22 (winning numbers)
Day3: 3:11/5:09 (grid numbers/symbols)
Day2: 0:37/1:34 (parsing input)
Day1: 0:12/2:24 (extracting digits)
""".trimIndent()

val personalstats = """
Day       Time   Rank  Score       Time   Rank  Score
  6   06:25:54  36207      0   06:30:27  34923      0
  5   06:42:10  30775      0   07:15:42  14406      0
  4   07:19:24  47716      0   07:30:24  37456      0
  3   05:52:13  27871      0   05:56:34  21730      0
  2   07:06:01  44750      0   07:08:30  41246      0
  1   07:36:34  61695      0   07:36:39  37712      0
""".trimIndent().lines().drop(1).map {
    val columns = it.trim().split("\\s+".toRegex())
    Data(columns[0].toInt(), columns[1], columns[2].toInt(), columns[4], columns[5].toInt())
}

personalstats.printChart()

fun printHeader(str: String) {
    println("\n---${str}---".randomBlueWhite() + ANSI_RESET)
}

printHeader("Part 1")
personalstats.map { it.rank }.sorted().printSummary()

printHeader("Part 2")
personalstats.map { it.rank2 }.sorted().printSummary()

printHeader("Rank Improvement between Parts")
personalstats.map { it.rank2 - it.rank }.sorted().printSummary()

printHeader("Updating Files")
val rootPath = File(File("").absolutePath.substringBeforeLast("aoc-2023")+"aoc-2023")
val codePath = File(rootPath, "src/main/kotlin/aoc/")
val inputPath = File(rootPath, "src/main/resources/aoc/input/")
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

fun List<Int>.printSummary() {
    println("Best rank: ${first()}")
    println("Median rank: ${this[size / 2]}")
    println("Worst rank: ${last()}")
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
            .replace("**", "$ANSI_LIGHTBLUE*$ANSI_WHITE*")
            .replace("* |", "*$ANSI_RESET |")
        println(line)
    }
}

data class Data(
    val day: Int,
    val time: String,
    val rank: Int,
    val time2: String,
    val rank2: Int
)

//endregion