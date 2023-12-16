import aoc.AocParser.Companion.parselines
import aoc.*
import aoc.util.*

val testInput = """
.|...\....
|.-.\.....
.....|-...
........|.
..........
.........\
..../.\\..
.-.-/..|..
.|....-|.\
..//.|....
""".parselines

class LaserMaze(val lines: CharGrid) {
    fun at(c: Coord) = lines.getOrNull(c.y)?.getOrNull(c.x)
    fun at(x: Int, y: Int) = lines.getOrNull(y)?.getOrNull(x)
}

// part 1

fun List<String>.part1(print: Boolean, initPos: Coord = Coord(-1, 0), initDir: Coord = Coord(1, 0)): Int {
    val maze = LaserMaze(this)

    val accumulated = mutableSetOf<Pair<Coord, Coord>>()
    var lastAccumulated = setOf<Pair<Coord, Coord>>()
    var paths = setOf(initPos to initDir)

    while (paths.isNotEmpty() && (lastAccumulated.isEmpty() || lastAccumulated != accumulated)) {
        lastAccumulated = accumulated.toSet()
        val nextPaths = mutableSetOf<Pair<Coord, Coord>>()
        paths.forEach {
            val nxt = it.first + it.second
            val spc = maze.at(nxt)
            when  {
                spc == null -> { } // out of maze, ignore
                spc == '.' -> {
                    nextPaths += nxt to it.second
                }
                spc == '|' && it.second.x == 0 -> {
                    nextPaths += nxt to it.second
                }
                spc == '-' && it.second.y == 0 -> {
                    nextPaths += nxt to it.second
                }
                spc == '|' -> {
                    nextPaths += nxt to Coord(0, 1)
                    nextPaths += nxt to Coord(0, -1)
                }
                spc == '-' -> {
                    nextPaths += nxt to Coord(1, 0)
                    nextPaths += nxt to Coord(-1, 0)
                }
                spc == '/' -> {
                    nextPaths += nxt to Coord(-it.second.y, -it.second.x)
                }
                spc == '\\' -> {
                    nextPaths += nxt to Coord(it.second.y, it.second.x)
                }
            }
        }
        val newPaths = nextPaths - accumulated
        accumulated.addAll(newPaths)
        paths = newPaths

        // print maze
        if (print) {
            println("-".repeat(maze.lines[0].length))
            maze.lines.forEachIndexed { y, row ->
                val rowWithPaths = row.indices.map { x ->
                    val coord = Coord(x, y)
                    val acc = accumulated.filter { it.first == coord }.map { it.second }
                    if (acc.isEmpty() || maze.at(coord) != '.') maze.at(coord)
                    else if (acc.size == 1) {
                        when (acc.first()) {
                            Coord(0, 1) -> 'v'
                            Coord(0, -1) -> '^'
                            Coord(1, 0) -> '>'
                            Coord(-1, 0) -> '<'
                            else -> throw Exception("bad path")
                        }
                    } else acc.size.toString()[0]
                }
                println(rowWithPaths.joinToString(""))
            }
        }
    }

    if (print)
        println("-".repeat(maze.lines[0].length))

    return accumulated.map { it.first }.toSet().size
}

// part 2

fun List<String>.part2(): Int {
    val maze = LaserMaze(this)
    val max1 = maze.lines.yrange.maxOf { y ->
        print("..")
        maxOf(
            part1(false, Coord(-1, y), Coord(1, 0)),
            part1(false, Coord(maze.lines.xrange.last + 1, y), Coord(-1, 0))
        )
    }
    println("half done")
    val max2 = maze.lines.xrange.maxOf { x ->
        print("..")
        maxOf(
            part1(false, Coord(x, -1), Coord(0, 1)),
            part1(false, Coord(x, maze.lines.yrange.last + 1), Coord(0, -1))
        )
    }
    println("done")
    return maxOf(max1, max2)
}

// calculate answers

val day = 16
val input = getDayInput(day, 2023)
val testResult = testInput.part1(true).also { it.print }
val testResult2 = testInput.part2().also { it.print }
val answer1 = input.part1(false).also { it.print }
val answer2 = input.part2().also { it.print }

// print results

AocRunner(day,
    test = { "$testResult, $testResult2" },
    part1 = { answer1 },
    part2 = { answer2 }
).run()
