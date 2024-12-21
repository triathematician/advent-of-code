import aoc.util.AocParser.Companion.parselines
import aoc.*
import aoc.getDayInput
import aoc.util.*

val testInput = """
###############
#...#...#.....#
#.#.#.#.#.###.#
#S#...#.#.#...#
#######.#.#.###
#######.#.#...#
#######.#.###.#
###..E#...#...#
###.#######.###
#...###...#...#
#.#####.#.###.#
#.#...#.#.#...#
#.#.#.#.#.#.###
#...#...#...###
###############
""".parselines

class Maze(val width: Int, val height: Int) {
    val walls = mutableSetOf<Coord>()
    val open = mutableSetOf<Coord>()
    var start = ORIGIN
    var end = ORIGIN

    // track min number of steps from start to each coord
    val stepsFromStart = mutableMapOf<Coord, Int>()
    // track min number of steps from each coord to end of maze
    val stepsToEnd = mutableMapOf<Coord, Int>()

    fun contains(c: Coord) = c.x in 0..width && c.y in 0..height

    /** Return options for next step. */
    fun adj(c: Coord) = DIRS4.map { c + it }.filter { it !in walls && contains(it) }

    /** Get steps to end with no wall removed. */
    fun solve(): Int {
        return Pathfinder(start) { adj(it) }.findWayTo(end).size - 1
    }

    /** Get steps to end with given wall removed. */
    fun solveWithWallRemoved(wall: Coord): Int {
        val newWalls = walls - wall
        fun adj2(c: Coord) = DIRS4.map { c + it }.filter { it !in newWalls && contains(it) }
        return Pathfinder(start) { adj2(it) }.findWayTo(end).size - 1
    }

    /** Fill in minimum number of steps from start to each coord, and from each coord to end. */
    fun generateStepCounts() {
        var steps = 0
        var frontier = adj(start)
        stepsFromStart[start] = steps
        while (frontier.isNotEmpty()) {
            steps++
            frontier.forEach { stepsFromStart[it] = steps }
            frontier = frontier.flatMap { adj(it) }.filter { it !in stepsFromStart }
        }

        steps = 0
        frontier = adj(end)
        stepsToEnd[end] = steps
        while (frontier.isNotEmpty()) {
            steps++
            frontier.forEach { stepsToEnd[it] = steps }
            frontier = frontier.flatMap { adj(it) }.filter { it !in stepsToEnd }
        }
    }
}

fun create(input: CharGrid): Maze {
    val map = Maze(input[0].length, input.size)
    input.allIndices2().forEach {
        when (input[it]) {
            '#' -> map.walls.add(it)
            '.' -> map.open.add(it)
            'S' -> map.start = it
            'E' -> map.end = it
            else -> error("Unknown char ${input[it]}")
        }
    }
    map.open.add(map.end)
    return map
}

// part 1

fun List<String>.part1(): Int {
    val maze = create(this)
    val steps = maze.solve()
    println("Solved maze in $steps steps")
    val count = mutableMapOf<Coord, Int>()
    maze.walls.forEach {
        val steps2 = maze.solveWithWallRemoved(it)
        if (steps2 < steps) {
            count[it] = steps - steps2
        }
    }
    count.entries.groupBy { it.value }.toSortedMap().forEach { (k, v) ->
        println("  - There are ${v.size} cheats that save $k picoseconds.")
    }
    return count.values.count { it >= 100 }
}

// part 2

fun Coord.adj2() = (-2..2).flatMap { x ->
    (-2..2).map { y -> Coord(this.x + x, this.y + y) }
}.filter { it.taxicab(this) <= 2 }

fun Coord.adj20() = (-20..20).flatMap { x ->
    (-20..20).map { y -> Coord(this.x + x, this.y + y) }
}.filter { it != this && it.taxicab(this) <= 20 }

fun List<String>.part1b(): Int {
    val maze = create(this)
    val steps = maze.solve()
    println("Solved maze in $steps picoseconds")
    maze.generateStepCounts()
    println("  - verifying by picoseconds counts: ${maze.stepsFromStart[maze.end]} and ${maze.stepsToEnd[maze.start]}")
    val cheatCountByPico = sortedMapOf<Int, Int>()
    (maze.open + maze.start).forEach { cheatStart ->
        val startToIt = maze.stepsFromStart[cheatStart]!!
        cheatStart.adj2().filter { it in maze.open }.forEach { cheatEnd ->
            val gap = cheatStart.taxicab(cheatEnd)
            val itToEnd = maze.stepsToEnd[cheatEnd]!!
            val stepsSaved = steps - (startToIt + gap + itToEnd)
            if (stepsSaved > 0) {
                cheatCountByPico[stepsSaved] = (cheatCountByPico[stepsSaved] ?: 0) + 1
            }
        }
    }
    if (maze.width < 50)
        cheatCountByPico.forEach { (k, v) ->
            println("  - There are $v cheats that save $k picoseconds.")
        }
    return cheatCountByPico.filter { it.key >= 100 }.values.sum()
}
fun List<String>.part2(): Int {
    val maze = create(this)
    val steps = maze.solve()
    println("Solved maze in $steps picoseconds")
    maze.generateStepCounts()
    val cheatCountByPico = sortedMapOf<Int, Int>()
    (maze.open + maze.start).forEach { cheatStart ->
        val startToIt = maze.stepsFromStart[cheatStart]!!
        cheatStart.adj20().filter { it in maze.open && it !in maze.adj(cheatStart) }.forEach { cheatEnd ->
            val gap = cheatStart.taxicab(cheatEnd)
            val itToEnd = maze.stepsToEnd[cheatEnd]!!
            val stepsSaved = steps - (startToIt + gap + itToEnd)
            if (stepsSaved > 0) {
                cheatCountByPico[stepsSaved] = (cheatCountByPico[stepsSaved] ?: 0) + 1
            }
        }
    }
    if (maze.width < 50)
        cheatCountByPico.forEach { (k, v) ->
            if (k >= 50)
                println("  - There are $v cheats that save $k picoseconds.")
        }
    return cheatCountByPico.filter { it.key >= 100 }.values.sum()
}

// calculate answers

val day = 20
val input = getDayInput(day, 2024)
val testResultA = testInput.part1()
val testResult = testInput.part1b()
val testResult2 = testInput.part2()
val answer1 = input.part1b()
val answer2 = input.part2()

// print results

AocRunner(day,
    test = { "$testResult, $testResult2" },
    part1 = { answer1 },
    part2 = { answer2 }
).run()
