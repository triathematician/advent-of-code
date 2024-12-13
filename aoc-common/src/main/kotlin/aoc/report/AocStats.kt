import aoc.util.*

object AocStats {
    val COLORA = ANSI_B_BLUE
    val COLORB = ANSI_B_PURPLE
    val COLORC = ANSI_B_PINK

    fun printStats(personalStats: String) {
        val data = personalStats.trimIndent().lines().drop(1).map {
            val columns = it.trim().split("\\s+".toRegex())
            Data(
                day = columns[0].toInt(),
                time = columns[1],
                rank = columns[2].toInt(),
                time2 = columns[4],
                rank2 = columns[5].toInt(),
                timeFile = columns.getOrNull(7)
            )
        }
        data.printChart(COLORA, COLORB, COLORC)
        data.printRankStats()
        data.printTimeStats()
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

fun List<Int>.printHistogram(min: Int? = 0, bucketSize: Int = 1000) {
    if (isEmpty()) {
        println("EMPTY")
        return
    }
    val min = min ?: min()
    val max = max()
    val range = max - min
    val buckets = IntArray(range / bucketSize + 1)
    forEach { buckets[(it - min) / bucketSize]++ }
    buckets.forEachIndexed { i, count ->
        val rangeStart = min + i * bucketSize
        val rangeEnd = rangeStart + bucketSize
        val bar = "*".repeat(count * 3)
        println("%5d - %5d: $bar".format(rangeStart, rangeEnd-1))
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
        print("%2d:00 - %2d:59: $bar".format(rangeStart, rangeEnd-1))
        if (numMin/60 in rangeStart until rangeEnd)
            print("  $ANSI_BOLD${ANSI_LIGHT_GREEN}BEST: ${numMin.minSec()}$ANSI_RESET")
        if (numMed/60 in rangeStart until rangeEnd)
            print("  $ANSI_BOLD${ANSI_LIGHT_YELLOW}MEDIAN: ${numMed.minSec()}$ANSI_RESET")
        if (numMax/60 in rangeStart until rangeEnd)
            print("  $ANSI_BOLD${ANSI_BROWN}WORST: ${numMax.minSec()}$ANSI_RESET")
        println()
    }
    print("     >= %2d:00: ${"***".repeat(nOver)}".format(useMin + buckets.size * bucketSize))
    if (numMax >= overflow * 60)
        print("  $ANSI_BOLD${ANSI_BROWN}WORST: ${numMax.hms()}$ANSI_RESET")
    println()
}

fun List<Data>.printChart(color1: String, color2: String, color3: String) {
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
            .colorStars(color1, color2, color3)
        println(line)
    }
}

fun List<Data>.printRankStats() {
    printHeader("Part 1 Ranks")
    map { it.rank }.sorted().printSummary()
    map { it.rank }.printHistogram(bucketSize = 10000)

    printHeader("Part 2 Ranks")
    map { it.rank2 }.sorted().printSummary()
    map { it.rank2 }.printHistogram(bucketSize = 10000)

    printHeader("Number of Ranks Dropped")
    map { it.rank - it.rank2 }.sortedDescending().improvementSummary()
    map { it.rank - it.rank2 }.printHistogram(min = -15000, bucketSize = 5000)
}

fun List<Data>.printTimeStats() {
    printHeader("Time to Solve Part 1")
//    map { it.timePart1 }.sorted().map { it.hms() }.printSummary()
    mapNotNull { it.timePart1 }.printHistogramMinutes(bucketSize = 5, overflow = 60)

    printHeader("Time to Solve Part 2")
//    map { it.timePart2 }.sorted().map { it.hms() }.printSummary()
    mapNotNull { it.timePart2 }.printHistogramMinutes(bucketSize = 5, overflow = 60)

    printHeader("Time to Solve Both")
//    map { it.timeBoth }.sorted().map { it.hms() }.printSummary()
    mapNotNull { it.timeBoth }.printHistogramMinutes(bucketSize = 5, overflow = 60)
}

fun String.colorStars(color1: String, color2: String, color3: String): String {
    // find each substring of stars, and add a color code before each, then an ansi reset after the last star
    val find = "\\*+".toRegex()
    var result = ""
    var pos = 0
    find.findAll(this).forEach {
        result += substring(pos, it.range.first)
        val range = it.range
        val size = range.last - range.first + 1
        result += (1..size/3+1).flatMap { listOf("$color1*", "$color2*", "$color3*") }
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
    val timeFile: String?
) {
    val timePart1 = timeFile?.let { time.parseTime() - it.parseTime() }
    val timePart2 = if (time == ">24h" || time2 == ">24h") null else time2.parseTime() - time.parseTime()
    val timeBoth = if (timePart1 != null && timePart2 != null) timePart1 + timePart2 else null

    private fun String.parseTime() = split(':').map { it.toInt() }.let { (h, m, s) -> h * 3600 + m * 60 + s }
}

fun Int.hms() = if (this >= 3600) {
    "%02d:%02d:%02d".format(this / 3600, (this % 3600) / 60, this % 60)
} else {
    "%5d:%02d".format(this / 60, this % 60)
}

fun Int.minSec() = "%2d:%02d".format(this / 60, this % 60)

//endregion