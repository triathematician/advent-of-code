import aoc.util.*

val leaders = """
Day1: 1:16/2:05 (groups of ints)
Day2: 3:43/6:16 (rock-paper-scissors)
Day3: 3:03/5:24 (strings)
Day4: 2:12/3:22 (intranges)
Day5: 6:42/7:58 (stacks and moves)
Day6: 1:52/2:25 (strings, chars)
Day7: 10:49/14:47 (tree, commands)
Day8: 4:30/10:12 (int matrix, rows/cols)
Day9: 7:32/14:08 (moves in a matrix)
Day10: 5:17/12:17 (computing signals)
Day11: 13:07/18:05 (basic arithmetic)
Day12: 7:39/9:45 (moving in a grid)
Day13: 8:16/12:56 (sorting nested list)
Day14: 10:33/13:54 (falling sand in a grid)
Day15: 10:40/27:14 (grid search)
Day16: 23:53/64:17 (graph search, with weighted edges)
Day17: 24:14/40:48 (tetris)
Day18: 2:55/12:29 (cube volumes)
Day19: 48:27/57:45 (BFS with pruning)
Day20: 15:41/21:14 (modulo)
Day21: 4:28/16:15 (arithmetic tree)
Day22: 19:04/74:31 (walking around a cube)
Day23: 21:46/24:43 (moving in a grid)
Day24: 
Day25: 
""".trimIndent()

val personalstats = """
Day       Time    Rank  Score       Time    Rank  Score
 23   07:48:57    6995      0   07:54:11    6742      0
 22   07:13:52    7229      0   08:52:41    3217      0
 21   06:44:05   10950      0   07:56:36    8537      0
 20   01:08:50    1861      0   01:19:55    1627      0
 19   22:52:40    8817      0   23:40:03    7816      0
 18   07:37:35   11758      0   08:22:39    8598      0
 16   09:50:53    7488      0   21:03:40    8609      0
 15   06:36:38   14288      0   07:48:04    9918      0
 14   08:37:34   18149      0   08:48:48   16949      0
 13   06:51:27   15548      0   06:57:10   14462      0
 12   06:03:11   14960      0   06:07:22   14124      0
 11   05:54:18   20231      0   06:10:41   14302      0
 10   04:53:33   22694      0   05:04:16   18993      0
  9   06:44:04   28759      0   07:10:32   21487      0
  8   06:37:31   35157      0   06:46:11   28809      0
  7   07:27:34   30703      0   07:31:43   28911      0
  6   06:49:13   55613      0   06:52:26   54474      0
  5   20:44:21   86085      0   20:48:42   83829      0
  4       >24h  115777      0       >24h  113821      0
  3       >24h  134014      0       >24h  127546      0
  2       >24h  146252      0       >24h  140012      0
  1       >24h  180775      0       >24h  174116      0
""".trimIndent().lines().drop(1).map {
    val columns = it.trim().split("\\s+".toRegex())
    Data(columns[0].toInt(), columns[1], columns[2].toInt(), columns[4], columns[5].toInt())
}

personalstats.printChart()

println("\n---Rank 1---")
personalstats.map { it.rank }.sorted().printSummary()

println("\n---Rank 2---")
personalstats.map { it.rank2 }.sorted().printSummary()

println("\n---Rank Improvement---")
personalstats.map { it.rank2 - it.rank }.sorted().printSummary()

fun List<Int>.printSummary() {
    println("Best rank: ${first()}")
    println("Median rank: ${this[size / 2]}")
    println("Worst rank: ${last()}")
}

fun List<Data>.printChart() {
    val rankOp = { it: Number -> kotlin.math.log2(it.toDouble()) - 9 }

    val sortedRanks = (map { it.rank } + map { it.rank2 }).map(rankOp).sorted()
    val rankMin = 0.0
    val rankMax = sortedRanks.last()

    val chartHeight = 30
    val rankRatio = (rankMax - rankMin) / chartHeight.toDouble()

    val header = "%-3s | %-${chartHeight + 9}s | %-${chartHeight + 9}s |".format("Day", "Part 1", "Part 2")
    println(header)
    println("-".repeat(header.length))
    map { data ->
        val barHeight1 = ((rankOp(data.rank) - rankMin) / rankRatio).toInt()
        val barHeight2 = ((rankOp(data.rank2) - rankMin) / rankRatio).toInt()

        val bar1 = "*".repeat(barHeight1)
        val bar2 = "*".repeat(barHeight2)

        val line = "%-3s | %${chartHeight + 9}s | %-${chartHeight + 9}s |".format(data.day,
            "[${data.rank}] $bar1",
            "$bar2 [${data.rank2}]"
        )   .replace("[", "$ANSI_WHITE[")
            .replace("]", "]$ANSI_RESET")
            .replace("**", "$ANSI_RED*$ANSI_GREEN*")
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