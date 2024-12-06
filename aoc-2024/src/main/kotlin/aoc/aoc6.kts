import aoc.util.AocParser.Companion.parselines
import aoc.*
import aoc.getDayInput
import aoc.util.*

val testInput = """
....#.....
.........#
..........
..#.......
.......#..
..........
.#..^.....
........#.
#.........
......#...
""".parselines

// part 1

fun CharGrid.part1(): Int {
//    val coords = allIndices2()
    val pos = findCoords2 { it == '^' }.values.first().first()
    return path(pos, NORTH).first.map { it.pos }.toSet().size
}

data class PosDir(val pos: Coord, val dir: Coord)

/** Get set of coords until leaving the map, or repeating. */
fun CharGrid.path(p: Coord, d: Coord): Pair<Set<PosDir>, Boolean> {
    val coords = allIndices2()
    var pos = PosDir(p, d)
    val traveled = mutableListOf<PosDir>()
    while (true) {
        traveled += pos
        val next = pos.pos + pos.dir
        if (next in coords && at(next) == '#') {
            pos = PosDir(pos.pos, when (pos.dir) {
                NORTH -> EAST
                EAST -> SOUTH
                SOUTH -> WEST
                WEST -> NORTH
                else -> error("Invalid direction")
            })
        } else if (next in coords) {
            pos = PosDir(next, pos.dir)
        } else {
            return traveled.toSet() to false
        }
        if (pos in traveled) {
            return traveled.toSet() to true
        }
    }
}

// part 2

fun CharGrid.part2(): Int {
    val pos = findCoords2 { it == '^' }.values.first().first()
    // only makes sense for obstacles to be somewhere in current path
    val openPos = path(pos, NORTH).first.map { it.pos }.toSet()
    return openPos.count {
        loopFrom(pos, it)
    }
}

fun CharGrid.loopFrom(pos: Coord, obstacle: Coord): Boolean {
    val cg2 = toMutableList().map { it.toMutableList() }
    cg2[obstacle.y][obstacle.x] = '#'
    return cg2.map { String(it.toCharArray()) }.path(pos, NORTH).second
}

// calculate answers

val day = 6
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
