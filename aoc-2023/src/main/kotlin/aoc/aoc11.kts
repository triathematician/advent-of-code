import aoc.AocParser.Companion.parselines
import aoc.*
import aoc.util.*
import java.lang.Math.abs

val testInput = """
...#......
.......#..
#.........
..........
......#...
.#........
.........#
..........
.......#..
#...#.....
""".parselines

class Universe(lines: List<String>) {
    val colsToExpand = lines[0].indices.filter { i ->
        lines.all { it[i] == '.' }
    }
    val rowsToExpand = lines.indices.filter { i ->
        lines[i].all { it == '.' }
    }

    val grid = lines.let { lines ->
        val colsToExpand = lines[0].indices.filter { i ->
            lines.all { it[i] == '.' }
        }
        lines.flatMap {
            val expanded = it.expand(colsToExpand)
            if ("#" in it) {
                listOf(expanded)
            } else {
                listOf(expanded, expanded)
            }
        }
    }
    val galaxies = grid.findCoords2 { it == '#' }.flatMap { it.value }

    val oGrid = lines
    val oGalaxies = oGrid.findCoords2 { it == '#' }.flatMap { it.value }

    init {
        println("-".repeat(40))
        oGrid.forEach { println(it) }
        println("-".repeat(40))
        grid.forEach { println(it) }
        println("-".repeat(40))
    }

    fun String.expand(cols: List<Int>) =
        substring(0, cols.first()) + cols.mapIndexed { i, col ->
            ".." + substring(col + 1, cols.getOrNull(i + 1) ?: length)
        }.joinToString("")

    fun countShortestPaths(): Int {
        var sum = 0
        galaxies.indices.forEach { g1 ->
            val gal1 = galaxies[g1]
            galaxies.indices.forEach { g2 ->
                val gal2 = galaxies[g2]
                if (g2 > g1) {
                    sum += abs(gal1.x - gal2.x) + abs(gal1.y - gal2.y)
                }
            }
        }
        return sum
    }

    fun countShortestPaths2(expandNum: Int): Long {
        var sum = 0L
        oGalaxies.indices.forEach { g1 ->
            val gal1 = oGalaxies[g1]
            oGalaxies.indices.forEach { g2 ->
                if (g2 > g1) {
                    val gal2 = oGalaxies[g2]
                    val minx = minOf(gal1.x, gal2.x)
                    val maxx = maxOf(gal1.x, gal2.x)
                    sum += maxx - minx
                    sum += (minx+1..maxx-1).count { it in colsToExpand } * (expandNum - 1)

                    val miny = minOf(gal1.y, gal2.y)
                    val maxy = maxOf(gal1.y, gal2.y)
                    sum += maxy - miny
                    sum += (miny+1..maxy-1).count { it in rowsToExpand } * (expandNum - 1)
                }
            }
        }
        return sum
    }
}

// part 1

fun List<String>.part1() = Universe(this).countShortestPaths()

// part 2

fun List<String>.part2() = Universe(this).countShortestPaths2(
    if (size < 20) 100 else 1000000
)

// calculate answers

val day = 11
val input = getDayInput(day, 2023)
val testResult = testInput.part1()
val testResult2 = testInput.part2()
val answer1 = input.part1().also { it.print }
val answer2 = input.part2().also { it.print }

// print results

AocRunner(day,
    test = { "$testResult, $testResult2" },
    part1 = { answer1 },
    part2 = { answer2 }
).run()
