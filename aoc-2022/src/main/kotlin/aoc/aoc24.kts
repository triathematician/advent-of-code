import aoc.AocParser.Companion.parselines
import aoc.AocRunner
import aoc.print
import aoc.util.*
import java.lang.IllegalStateException

val day = 24

val testInput = """
#.######
#>>.<^<#
#.<..<<#
#>v.><>#
#<^v^^>#
######.#
""".parselines

val input = """
#.######################################################################################################################################################
#>.^v.>^v>><>>^>>v>v^>.<>^.>v^v>..<<><>>>v.><^<.>^.<^>^v^>>v<^>><.^<^.<v>v><><^^<^>>^<<<>.>.>^^^<^<^>.>^v.>.>^>^v<<^^<>><vv<><>>>v^.>^<vvv>.^>v.>.v<v.<#
#<^^^><<vv<vv^^><>><>vv.vv<>vvvv<vv.>^>^>v^vvv>.<^>v<>>.<><<><>v<v^^<^.v^^><<><^><<.v.vv<<><.<<<<v^^>><^<><^<<<<.>v<^v<v>^>>>><.v>^>.>^<^>>v<v<^vv<v>v<#
#>>>>^.>.^v>v<.^<^vv^^^<>.>>.>><<.v<v^><^><.<<<><.^v^^v<><v^v><vv<<v<..<<^<.<>^<^v.^>^<v^v.^<v>v<><>>>^v<v.^^<><>><>v<^v>>>^v<v^>v<^vvv><v>^><<><^v>>^>#
#><<>^>v>.<^^v><v.<>v^<<<>^^v>^.v<<>><>v.v>v^.v>>>^<^><.>>v<<<>vv^.>^>^<><^.>.v^^<v<.^>^><>^<v>vv^v>v^<>><<vv>>>v^^<^v>>>v.<<>.v<><^^v<.^>v>.<^^><vv.^>#
#<.v^vv^vv>><v>^>>^.vvv^^<v<.v^>><<>v<v<<^<<<vv.>><>v.^>vvv<v<v>^<>>v><.>^.>.^<<<.^vvv>^..^>>.v<v.^v^<^^<<^<^>^v>^v<vvv.v^^v<<<>^v<<<v><>v^<<^><.v>v<v>#
#<^v^><>v^<.>vv>.^<<<vv^^<><^<^.^^<.^vv^>v^^.^<^>^.^<><><^<<>><vv<v^<>v<><>v^^v<.<v^<v>vvv>^>>^<^><>>^vv<^>><v><^.<<v><v.>^v^v>>>^<vv>v<^<><>.^^>.v><<>#
#<.><^v>^<.>>v<v>v<^v^v.^>v<.v>.<vv>^>v<v<<>^^.<>.<.vvv<^>v^.^<^v>^.>.v<.v>><<<^vv^.^<<^<^v<v^^^>.><>v.^^.^><<.<vvvv<v><<^vv.<^^^^>^<.<><<>>^>v>.^^v.<<#
#<<^.>v<><>.^>>^v<<^>vv<^v>><.<v^v^<<><v><^^.^^^>^.><^>v^v<<<<>>^>v>^v<^<>..v<.<v<><>v^>v^^v..<>^<><>^v<<v<<><<.v>...>^^v>.>v^.^>vv>^v<>v><<^.>^^..<<^<#
#><v^<^..>>v<v.<>vvv>v<v^>>>.v^<^v<>.>^<.<<^<>.<..>.^.>^^>.^.^^>v.v^>v<^>^v.<v^>v^^^vv><^>^>>>v><v<>>>..v.<^<.<<<>>><^<<v>>^<.>^^^<><^<.<^>^<v.^>vv>vv>#
#<><>v^..>v>^^^^<.v>v>..<>><^><<^v.v.<v^<v><><<<v>>>^^>>v<<>^^<^^^>v.<<>v^<v><v^<<v>>^v>>v><v^^>vv^<^v<^v^.><>vv>>>.>>v^<v^v>><^^v.^<v.><v^^<<<v^>>^v^>#
#>v><>><<^.>v^.v<<<v><^vv<.v^>v<.>vv>v>>^<^<<><>v>v<v..v<^<.vvv<^>^^v<^.>.^^^>^^v.^.>^>>>^><v<.v<^^^^.v.vv<<vv<.>v<^>v^<<vv^v><v>v^>>v.^.^<<.vv>^^^<^v<#
#<v^<>>^>>^..vv<^<<<v.v.^vv<.<^^<v^<.>^<.^.<<v^^^<^><>.>>>><^<>^>^v>><v^>v.<.^<v>^^>><><.>^v^>^<>^<^^.<^>^^^^^^vv<>vv^v^v>^v^<v<vv><<vv<^^^.>>^><<<.^>>#
#<.<v^.>>><vv^.<^^^>>>.><v<v>^<>>.^^vv>>^<<<^v><^^<v<>>^^^^<v^<>^^>><^>v^<<>>v<^.>vvvv>>^<.^>.v<^^<<v>>.vv.v^v<>>>><^v^>>>.v.^^v<<v>v^^>^v>^v<><.v><^v.#
#><<^v><<<.v<^^><^v<^v.<<<.^^.v>vvvv>^>><^^v<>.<>><<.v^>..^<>.><^>^>^v^^>v<<vv>^>vvv.>^^>^^<<>v>v^>^^^<^v>.v>^<><^<.v<^><<>^.^>>><<v^>v^v^v<^v>>^^^><^<#
#<^>v<>.>>^.v>^<v><>.v.^^v>^<..v.^>^>.v>>><vv^<>^^>v><>vv.<v<v^vv<.^vv<><v>>>^v><v^vvvvv.<vv>>^v<<.v>v^>v><<<^.<<<<>vv>^>>^<>^^<vvv^<vvvvvv><^^<<<>^^<>#
#>>.>>.^>..<^<^.<<v<><>..^^<..v<.><^^<v.v^v><>^vv<v<^v<^>.v<vvvv><<<>^>^v^>v<>><vv^v<>>>>>v<v<>^.>><v<v.>><v<v^^<v.<><.<>^^^^v<<^>>.><<.><.v^^..>>>.v>>#
#>v<v^^<^vvv.>^<>>v^v.vv^>v^vvvvv<>.v^.v<.><>^>>^<<v<^^v>v><^.>>^><>v<vv>v<<^>^>^.<v>vv<<>^^>^<<>v><v>.<<^.<>><<^<<.^<^<<<.^>.<.vv>v^v<^>vvv>^v<<<<v<<<#
#<^<>v^.^<><>^<>^>v<v.^v..v..vv^.<.^^>^>^><.vv>>.<^..^^^<^<.^^<^^v^<^.<<<<<v<^<<><v>^v.<<>.^>><^>^v>><<v<>^v^<<^>>v<<^.v<<^vv>>v>^.v^^^<><vv^^<<>.v.<><#
#..vv<v><^vv>.v<v<^v><vvv>.v>^v^>>vv^.^>v.v^^<<>^^<>^<^^>.<^v<v>^>^<v<<v<>v<^^^>v>><><<><.<<><^<v<<>v^^>>v><>^v^.v>^^v<^><><^v<^<vv.vvv><><<>v<<><>v<.>#
#>>.<^^>><<<^>v<^^>>^<<>><<.<^^^<^^^<^<v<^<<^><<v<v<^^>vv<<^v>.^<^^>><>^^^.>v>v^<^>>v><<^<^^^>v><<>^^v><>v..<.^^>..vv.<v>>>^<<v^<v^^v>>>>.>vv.>>^v>^v<.#
######################################################################################################################################################.#
""".parselines

enum class Dir(val dx: Int, val dy: Int) {
    UP(0,-1),
    RIGHT(1,0),
    DOWN(0,1),
    LEFT(-1,0),
    WAIT(0, 0)
}

class BlizzardMap(input: List<String>) {
    val width = input[0].length - 2
    val height = input.size - 2
    val xrange = 0 until width
    val yrange = 0 until height
    val blizzards = mutableListOf<BlizzardPos>()

    val start = 0 to -1
    val end = width-1 to height

    init {
        input[0].indices.drop(1).dropLast(1).forEach { x ->
            input.indices.drop(1).dropLast(1).forEach { y ->
                when (input.get(x, y)) {
                    '.' -> {}
                    '>' -> blizzards += BlizzardPos(x-1, y-1, Dir.RIGHT)
                    '<' -> blizzards += BlizzardPos(x-1, y-1, Dir.LEFT)
                    '^' -> blizzards += BlizzardPos(x-1, y-1, Dir.UP)
                    'v' -> blizzards += BlizzardPos(x-1, y-1, Dir.DOWN)
                    else -> TODO()
                }
            }
        }
    }

    fun blizzardLocs(t: Int): Set<Coord> {
        return blizzards.map {
            Math.floorMod(it.x + it.dir.dx * t, width) to Math.floorMod(it.y + it.dir.dy * t, height)
        }.toSet()
    }

    fun isBlizzardAt(c: Coord, t: Int) = c in blizzardLocs(t)
    fun isValid(c: Coord) = c.x in xrange && c.y in yrange

    fun shortestPathTo(start: Coord, end: Coord, t0: Int): List<Dir> {
        var paths = listOf(listOf<Dir>() to start)
        var t = t0 + 1
        while (true) {
            paths = paths.flatMap { (pathSoFar, pos) ->
                Dir.values().mapNotNull {
                    val c = Coord(pos.x + it.dx, pos.y + it.dy)
                    if (c == end) {
                        val res = pathSoFar + it
                        println("${res.print()} -- ${res.size} steps")
                        return res
                    } else if ((!isValid(c) && c != start) || isBlizzardAt(c, t))
                        null
                    else
                        (pathSoFar + it) to c
                }
            }
            paths = paths.prune()
            if (paths.isEmpty())
                throw IllegalStateException()
            if (shouldPrint(t-t0))
                println("  ..evaluating ${paths.size} paths at time $t")
//        println("  ..evaluating ${paths.size} paths at time $t: " + paths.joinToString { it.print() })
            t++
        }
    }

    private fun shouldPrint(x: Int) =
        x <= 10 ||
        x <= 100 && x % 10 == 0 ||
        x <= 1000 && x % 100 == 0 ||
        x % 1000 == 0

    // only keep one path to given target coordinate at given target time
    private fun List<Pair<List<Dir>, Coord>>.prune(): List<Pair<List<Dir>, Coord>> {
        return groupBy { it.second }.map { (_, pathsToHere) ->
            pathsToHere.first()
        }
    }
}

data class BlizzardPos(var x: Int, var y: Int, val dir: Dir)


fun Pair<List<Dir>, Coord>.print() = first.print() + " " + second
fun List<Dir>.print() = joinToString("") { it.name.substring(0, 1) }

// test case

val testBlizzard = BlizzardMap(testInput)
val TEST_START = testBlizzard.start
val TEST_END = testBlizzard.end

println("-".repeat(40))
val testResult = testBlizzard.shortestPathTo(TEST_START, TEST_END, 0).size
println("-".repeat(40))
val tpath1 = testBlizzard.shortestPathTo(TEST_START, TEST_END, 0)
val tpath2 = testBlizzard.shortestPathTo(TEST_END, TEST_START, tpath1.size)
val tpath3 = testBlizzard.shortestPathTo(TEST_START, TEST_END, tpath1.size + tpath2.size)
println("-".repeat(40))
println("${tpath1.print()} -- ${tpath1.size} steps")
println("${tpath2.print()} -- ${tpath2.size} steps")
println("${tpath3.print()} -- ${tpath3.size} steps")
val testResult2 = tpath1.size + tpath2.size + tpath3.size

// part 1

val blizzard = BlizzardMap(input)
val START = blizzard.start
val END = blizzard.end

println("-".repeat(40))
val answer1 = blizzard.shortestPathTo(START, END, 0).size

// part 2

println("-".repeat(40))
val path1 = blizzard.shortestPathTo(START, END, 0)
val path2 = blizzard.shortestPathTo(END, START, path1.size)
val path3 = blizzard.shortestPathTo(START, END, path1.size + path2.size)
println("-".repeat(40))
println("${path1.print()} -- ${path1.size} steps")
println("${path2.print()} -- ${path2.size} steps")
println("${path3.print()} -- ${path3.size} steps")
val answer2 = path1.size + path2.size + path3.size

// print results

AocRunner(day,
    info = { listOf("Leaderboard: 21:08/26:48", "Answers: 334/934") },
    test = { "$testResult, $testResult2" },
    part1 = { answer1 },
    part2 = { answer2 }
).run()