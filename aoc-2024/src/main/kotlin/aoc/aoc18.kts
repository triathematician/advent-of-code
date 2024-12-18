import aoc.util.AocParser.Companion.parselines
import aoc.*
import aoc.getDayInput
import aoc.util.*

val testInput = """
5,4
4,2
4,5
3,0
2,1
6,3
2,4
1,5
0,6
3,3
2,6
5,1
1,2
5,5
2,5
6,5
1,4
0,4
6,4
1,1
6,1
1,0
0,5
1,6
2,0
""".parselines

class Maze(val width: Int, val height: Int) {
    val walls = mutableSetOf<Coord>()
    var pos = ORIGIN
    var end = ORIGIN

    fun contains(c: Coord) = c.x in 0..width && c.y in 0..height

    /** Return options for next step. */
    fun adj(c: Coord) = DIRS4.map { c + it }.filter { it !in walls && contains(it) }
}

// part 1

fun List<String>.part1(): Int {
    val points = map { it.parseInts().let { Coord(it[0], it[1]) } }.toSet()
    val testCase = points.size < 50
    val size = if (testCase) 6 else 70
    val maze = Maze(size, size).apply {
        end = Coord(size, size)
        walls.addAll(points.take(if (testCase) 12 else 1024))
    }
    return Pathfinder(maze.pos) { maze.adj(it) }.findWayTo(maze.end).size - 1
}

// part 2

fun List<String>.part2(): String {
    val points = map { it.parseInts().let { Coord(it[0], it[1]) } }
    val testCase = points.size < 50
    val size = if (testCase) 6 else 70
    (1..points.size).forEach {
        val maze = Maze(size, size).apply {
            end = Coord(size, size)
            walls.addAll(points.take(it))
        }
        val p = Pathfinder(maze.pos) { maze.adj(it) }.findWayTo(maze.end)
        if (p.first() == maze.pos && p.last() == maze.end) {
//            println("Found path with $it blocks: ${p.size - 1}")
        } else {
            println("No path with $it blocks")
            return "${points[it-1].x},${points[it-1].y}"
        }
    }
    return ""
}

// calculate answers

val day = 18
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
