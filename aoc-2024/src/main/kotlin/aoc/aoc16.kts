import aoc.util.AocParser.Companion.parselines
import aoc.*
import aoc.getDayInput
import aoc.util.*

val testInput = """
###############
#.......#....E#
#.#.###.#.###.#
#.....#.#...#.#
#.###.#####.#.#
#.#.#.......#.#
#.#.#####.###.#
#...........#.#
###.#.#####.#.#
#...#.....#.#.#
#.#.#.###.#.#.#
#.....#...#.#.#
#.###.#.#.#.#.#
#S..#.....#...#
###############
""".parselines

class Maze(val width: Int, val height: Int) {
    val walls = mutableSetOf<Coord>()
    val open = mutableSetOf<Coord>()
    var pos = ORIGIN
    var dir = RIGHT
    var end = ORIGIN

    /** Return scores for what's next in the grid. */
    fun next(posDir: PosDir): Map<PosDir, Int> {
        val result = mutableMapOf<PosDir, Int>()
        val pos = posDir.pos
        val dir = posDir.dir
        if (pos + dir in open)
            result[PosDir(pos + dir, dir)] = 1
        if (pos + dir.turnLeft() in open)
            result[PosDir(pos + dir.turnLeft(), dir.turnLeft())] = 1001
        if (pos + dir.turnRight() in open)
            result[PosDir(pos + dir.turnRight(), dir.turnRight())] = 1001
        return result
    }
}

fun create(input: CharGrid): Maze {
    val map = Maze(input[0].length, input.size)
    input.allIndices2().forEach {
        when (input[it]) {
            '#' -> map.walls.add(it)
            '.' -> map.open.add(it)
            'S' -> map.pos = it
            'E' -> map.end = it
            else -> error("Unknown char ${input[it]}")
        }
    }
    map.open.add(map.end)
    return map
}

// part 1

fun List<String>.setup(): AstarSearch<PosDir> {
    val maze = create(this)
    return AstarSearch(PosDir(maze.pos, maze.dir),
        { it.pos == maze.end },
        { maze.next(it) },
        { it.pos.taxicab(maze.end) + if (it.pos.x == maze.end.x || it.pos.y == maze.end.y) 0 else 1000 }
    )
}

fun List<String>.part1(): Int {
    val pathfinder = setup()
    val path = pathfinder.minimizeCost()
    return path.second
}

// part 2

fun List<String>.part2(): Int {
    val pathfinder = setup()
    val path = pathfinder.minimizeCostAll()
    val set = path.first.flatten().map { it.pos }.toSet()
    println(set.joinToString(" ") { "${it.first},${it.second}" })
    return set.size
}

// calculate answers

val day = 16
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
