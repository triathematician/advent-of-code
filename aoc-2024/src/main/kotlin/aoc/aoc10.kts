import aoc.util.AocParser.Companion.parselines
import aoc.*
import aoc.getDayInput
import aoc.util.*

val testInput = """
89010123
78121874
87430965
96549874
45678903
32019012
01329801
10456732
""".parselines

// part 1

fun List<String>.part1(): Int {
    val grid = this as CharGrid
    val allCoords = grid.allIndices2().toSet()
    // track the number of trailheads (0 starts) to each target coordinate
    val scores = mutableMapOf<Coord, Set<Coord>>()
    (0..9).forEach { ch ->
        val pos = allCoords.filter { p -> grid[p] == ('0' + ch) }
        pos.forEach { p ->
            if (ch == 0)
                scores[p] = setOf(p)
            else
                scores[p] = listOf(LEFT, RIGHT, UP, DOWN).filter {
                    p + it in allCoords && grid[p + it] == '0' + ch - 1
                }.flatMap { scores[p + it] ?: setOf() }.toSet()
        }
    }
    return allCoords.filter { grid[it] == '9' }.sumOf { scores[it]!!.size }
}

// part 2

fun List<String>.part2(): Int {
    val grid = this as CharGrid
    val allCoords = grid.allIndices2().toSet()
    // track the number of paths to each target coordinate
    val scores = mutableMapOf<Coord, Int>()
    (0..9).forEach { ch ->
        val pos = allCoords.filter { p -> grid[p] == ('0' + ch) }
        pos.forEach { p ->
            if (ch == 0)
                scores[p] = 1
            else
                scores[p] = listOf(LEFT, RIGHT, UP, DOWN).filter {
                    p + it in allCoords && grid[p + it] == '0' + ch - 1
                }.sumOf { scores[p + it]!! }
        }
    }
    return allCoords.filter { grid[it] == '9' }.sumOf { scores[it]!! }
}

// calculate answers

val day = 10
val input = getDayInput(day, 2024)
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
