import aoc.AocParser.Companion.parselines
import aoc.*
import aoc.util.*

val testInput = """
...........
.S-------7.
.|F-----7|.
.||.....||.
.||.....||.
.|L-7.F-J|.
.|..|.|..|.
.L--J.L--J.
...........
""".parselines

class Maze(val grid: List<String>) {
    val xr = grid[0].indices
    val yr = grid.indices
    val coords = yr.flatMap { y -> xr.map { x -> Coord(x, y) } }
    val start = coords.find { at(it) == 'S' }!!

    fun at(c: Coord) = grid[c.y][c.x]

    fun pipeNeighbors(c: Coord) = when(at(c)) {
        'S' -> if (xr.count() > 20)
            listOf(c.left, c.bottom) // 7
        else
            listOf(c.right, c.bottom) // F
        '|' -> listOf(c.top, c.bottom)
        '-' -> listOf(c.left, c.right)
        'J' -> listOf(c.left, c.top)
        'L' -> listOf(c.top, c.right)
        'F' -> listOf(c.right, c.bottom)
        '7' -> listOf(c.bottom, c.left)
        else -> emptyList()
    }

    fun tracePipeFromStart(): Map<Coord, Int> {
        val res = mutableListOf<List<Coord>>(listOf(start))
        val all = mutableSetOf(start)
        var last = setOf(start)
        while (last.isNotEmpty()) {
            val newNeighbors = last.flatMap { pipeNeighbors(it) }.filter { it !in all }
            res += newNeighbors
            all += newNeighbors
            last = newNeighbors.toSet()
        }
        return res.mapIndexed { i, l -> l.map { it to i } }.flatten().toMap()
    }

    fun insideLoop(loop: Set<Coord>, row: Int): Int {
        var str = xr.mapNotNull {
            val c = Coord(it, row)
            when {
                c !in loop -> ' '
                at(c) == 'S' -> if (xr.count() > 20) '7' else 'F'
                at(c) == '-' -> null
                else -> at(c)
            }
        }.joinToString("")
        str = str.replace("F7", "")
            .replace("LJ", "")
            .replace("FJ", "|")
            .replace("L7", "|")
        var res = 0
        var index = 0
        val search = "\\|(\\s*)\\|".toRegex()
        var match = search.find(str, index)
        while (match != null) {
            res += match.groupValues[1].length
            index = match.range.last + 1
            match = search.find(str, index)
        }
        return res
    }
}

// part 1

fun List<String>.part1(): Int {
    val maze = Maze(this)
    val pipe = maze.tracePipeFromStart()
    return pipe.values.max()
}

// part 2

fun List<String>.part2(): Int {
    val maze = Maze(this)
    val pipe = maze.tracePipeFromStart().keys
    return maze.yr.sumOf { maze.insideLoop(pipe, it) }
}

// calculate answers

val day = 10
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
