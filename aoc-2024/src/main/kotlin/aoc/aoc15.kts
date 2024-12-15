import aoc.util.AocParser.Companion.parselines
import aoc.*
import aoc.getDayInput
import aoc.util.*

val testInput = """
#######
#...#.#
#.....#
#..OO@#
#..O..#
#.....#
#######

<vv<<^^<<^^
""".parselines

val testInputB = """
##########
#..O..O.O#
#......O.#
#.OO..O.O#
#..O@..O.#
#O#..O...#
#O..O..O.#
#.OO.O.OO#
#....O...#
##########

<vv>^<v^>v>^vv^v>v<>v^v<v<^vv<<<^><<><>>v<vvv<>^v^>^<<<><<v<<<v^vv^v>^
vvv<<^>^v^^><<>>><>^<<><^vv^^<>vvv<>><^^v>^>vv<>v<<<<v<^v>^<^^>>>^<v<v
><>vv>v^v^<>><>>>><^^>vv>v<^^^>>v^v^<^^>v^^>v^<^v>v<>>v^v^<v>v^^<^^vv<
<<v<^>>^^^^>>>v^<>vvv^><v<<<>^^^vv^<vvv>^>v<^^^^v<>^>vvvv><>>v^<<^^^^^
^><^><>>><>^^<<^^v>>><^<v>^<vv>>v>>>^v><>^v><<<<v>>v<v<v>vvv>^<><<>^><
^>><>^v<><^vvv<^^<><v<<<<<><^v<<<><<<^^<v<^^^><^>>^<v^><<<^>>^v<v^v<v^
>^>>^v>vv>^<<^v<>><<><<v<<v><>v<^vv<<<>^^v^>^^>>><<^v>>v^v><^^>>^<>vv^
<><^^>^^^<><vvvvv^v<v<<>^v<v>v<<^><<><<><<<^^<<<^<<>><<><^^^>^^<>^>v<>
^^>vv<^v^v<vv>^<><v<^v>^^^>>>^^vvv^>vvv<>>>^<^>>>>>^<<^v>^vvv<>^<><<v>
v^^>>><<^^<>>^v^<v^vv<>v^<<>^<^v^v><^<<<><<^<v><v<>vv>>v><v^<vv<>v^<<^
""".parselines

val DIRS = mapOf('<' to LEFT, '>' to RIGHT, '^' to UP, 'v' to DOWN)

class Map(val width: Int, val height: Int) {
    val walls = mutableSetOf<Coord>()
    var lastrobot = ORIGIN
    var robot = ORIGIN
    val boxes = mutableSetOf<Coord>()

    fun contains(c: Coord): Boolean {
        return c.x in (1 until width) && c.y in (1 until height)
    }

    fun moveRobot(move: Char) {
        val dir = DIRS[move]!!
        // find empty space in front of robot, if it exists
        (1..width).forEach { dist ->
            val there = robot + dir * dist
            if (!contains(there) || walls.contains(there))
                return
            if (!boxes.contains(there)) {
                ((dist-1) downTo 1).forEach {
                    if (boxes.contains(robot + dir * it)) {
                        boxes.remove(robot + dir * it)
                        boxes.add(robot + dir * (it + 1))
                    }
                }
                lastrobot = robot
                robot += dir
                return
            }
        }
    }

    fun print() {
        (0 until height).forEach { y ->
            (0 until width).forEach { x ->
                val c = Coord(x, y)
                print(when {
                    robot == c -> "$ANSI_BLUE@$ANSI_RESET"
                    lastrobot == c -> "${ANSI_DARK_BLUE}@$ANSI_RESET"
                    walls.contains(c) -> "$ANSI_GRAY#$ANSI_RESET"
                    boxes.contains(c) -> "${ANSI_PINK}O$ANSI_RESET"
                    else -> "${ANSI_GRAY}.$ANSI_RESET"
                })
            }
            println()
        }
    }

    fun sumCoords() = boxes.sumOf { it.x + it.y * 100 }
}

fun create(input: CharGrid): Map {
    val map = Map(input[0].length, input.size)
    input.allIndices2().forEach {
        when (input[it]) {
            '#' -> map.walls.add(it)
            'O' -> map.boxes.add(it)
            '@' -> map.robot = it
            '.' -> {}
            else -> error("Unknown char ${input[it]}")
        }
    }
    map.lastrobot = map.robot
    return map
}

class Map2(val width: Int, val height: Int) {
    val walls = mutableSetOf<Coord>()
    var lastrobot = ORIGIN
    var robot = ORIGIN
    val boxes = mutableSetOf<Coord>()

    fun contains(c: Coord): Boolean {
        return c.x in (1 until width) && c.y in (1 until height)
    }

    fun moveRobot(move: Char) {
        val dir = DIRS[move]!!
        val boxesToMove = mutableSetOf<Coord>() // collection of boxes to move with robot, by location of box
        val spacesToCheck = mutableSetOf(robot + dir) // locations that have to be either free or a box to be able to move
        while (spacesToCheck.isNotEmpty()) {
            if (spacesToCheck.any { !contains(it) || walls.contains(it) })
                return
            val foundBoxes = mutableSetOf<Coord>()
            spacesToCheck.forEach {
                when {
                    boxes.contains(it) -> foundBoxes.add(it)
                    boxes.contains(it + LEFT) -> foundBoxes.add(it + LEFT)
                }
            }
            // all spaces are clear, now we need to update spacesToCheck based on the new boxes we found
            spacesToCheck.clear()
            foundBoxes.forEach {
                boxesToMove.add(it)
                val pos1 = it
                val pos2 = it + RIGHT
                // check direction box would be moved in, while making sure we're not including the box location itself
                spacesToCheck.addAll(setOf(pos1 + dir, pos2 + dir) - setOf(pos1, pos2))
            }
        }
        // all boxes are movable, move robot and boxes
        lastrobot = robot
        robot += dir
        val newBoxLocs = boxesToMove.map { it + dir }
        boxes.removeAll(boxesToMove)
        boxes.addAll(newBoxLocs)
    }

    fun sumCoords() = boxes.sumOf { it.x + it.y * 100 }

    fun print() {
        (0 until height).forEach { y ->
            (0 until width).forEach { x ->
                val c = Coord(x, y)
                print(when {
                    robot == c -> "$ANSI_BLUE@$ANSI_RESET"
                    lastrobot == c -> "${ANSI_DARK_BLUE}@$ANSI_RESET"
                    walls.contains(c) -> "$ANSI_GRAY#$ANSI_RESET"
                    boxes.contains(c) -> "${ANSI_PINK}[$ANSI_RESET"
                    boxes.contains(c + LEFT) -> "${ANSI_PINK}]$ANSI_RESET"
                    else -> "${ANSI_GRAY}.$ANSI_RESET"
                })
            }
            println()
        }
    }
}

fun List<String>.expand() = map {
    val sb = StringBuilder()
    it.forEach {
        when (it) {
            '#' -> sb.append("##")
            'O' -> sb.append("[]")
            '.' -> sb.append("..")
            '@' -> sb.append("@.")
            else -> error("Unknown char $it")
        }
    }
    sb.toString()
}

fun create2(input: CharGrid): Map2 {
    val map = Map2(input[0].length, input.size)
    input.allIndices2().forEach {
        when (input[it]) {
            '#' -> map.walls.add(it)
            '[' -> map.boxes.add(it)
            ']' -> {}
            '@' -> map.robot = it
            '.' -> {}
            else -> error("Unknown char ${input[it]}")
        }
    }
    map.lastrobot = map.robot
    return map
}

// part 1

fun List<String>.part1(): Int {
    val map = create(takeWhile { it.isNotEmpty() })
    val moves = takeLastWhile { it.isNotEmpty() }.joinToString("")
    moves.forEach { map.moveRobot(it) }
    map.print()
    val result = map.sumCoords()
    println("-".repeat(100)+ " Sum of Coordinates: $result")
    return result
}

// part 2

fun List<String>.part2(): Int {
    val mapChars = takeWhile { it.isNotEmpty() }.expand()
    val map = create2(mapChars)
    map.print()
    val moves = takeLastWhile { it.isNotEmpty() }.joinToString("")
    moves.forEach {
        map.moveRobot(it)
    }
    map.print()
    val result = map.sumCoords()
    println("-".repeat(100)+ " Sum of Coordinates: $result")
    return result
}

// calculate answers

val day = 15
val input = getDayInput(day, 2024)
val testResult = testInput.part1()
val testResultB = testInputB.part1()
val testResult2 = testInput.part2()
val testResult2B = testInputB.part2()
val answer1 = input.part1().also { it.print }
val answer2 = input.part2().also { it.print }

// print results

AocRunner(day,
    test = { "$testResult / $testResultB, $testResult2 / $testResult2B" },
    part1 = { answer1 },
    part2 = { answer2 }
).run()
