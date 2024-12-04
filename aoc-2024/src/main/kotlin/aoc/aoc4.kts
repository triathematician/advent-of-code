import aoc.AocParser.Companion.parselines
import aoc.*
import aoc.util.*

val testInput = """
MMMSXXMASM
MSAMXMSMSA
AMXSXMAAMM
MSAMASMSMX
XMASAMXAMM
XXAMMXXAMA
SMSMSASXSS
SAXAMASAAA
MAMMMXMMMM
MXMXAXMASX
""".parselines

// part 1

fun List<String>.part1(): Int {
    return allIndices2().sumOf {
        xmas(it.x, it.y, 1, -1) +
            xmas(it.x, it.y, 1, 0) +
            xmas(it.x, it.y, 1, 1) +
            xmas(it.x, it.y, 0, -1) +
            xmas(it.x, it.y, 0, 1) +
            xmas(it.x, it.y, -1, -1) +
            xmas(it.x, it.y, -1, 0) +
            xmas(it.x, it.y, -1, 1)
    }
}

fun CharGrid.xmas(x: Int, y: Int, dx: Int, dy: Int): Int {
    if (contains(Coord(x + 3 * dx, y + 3 * dy)) &&
            at(x, y) == 'X' &&
            at(x + dx, y + dy) == 'M' &&
            at(x + 2 * dx, y + 2 * dy) == 'A' &&
            at(x + 3 * dx, y + 3 * dy) == 'S')
        return 1
    return 0
}

// part 2

fun List<String>.part2(): Int {
    return allIndices2().sumOf {
        xmas2(it.x, it.y)
    }
}

fun CharGrid.xmas2(x: Int, y: Int): Int {
    if ((x - 1 to y - 1) !in this || (x + 1 to y + 1) !in this || at(x, y) != 'A')
        return 0
    if (at(x - 1, y - 1) == 'M' && at(x - 1, y + 1) == 'M') {
        if (at(x + 1, y - 1) == 'S' && at(x + 1, y + 1) == 'S') {
            return 1
        }
    } else if (at(x - 1, y - 1) == 'M' && at(x + 1, y - 1) == 'M') {
        if (at(x - 1, y + 1) == 'S' && at(x + 1, y + 1) == 'S') {
            return 1
        }
    } else if (at(x - 1, y - 1) == 'S' && at(x + 1, y - 1) == 'S') {
        if (at(x - 1, y + 1) == 'M' && at(x + 1, y + 1) == 'M') {
            return 1
        }
    } else if (at(x - 1, y + 1) == 'S' && at(x + 1, y + 1) == 'S') {
        if (at(x - 1, y - 1) == 'M' && at(x + 1, y - 1) == 'M') {
            return 1
        }
    } else if (at(x - 1, y - 1) == 'S' && at(x - 1, y + 1) == 'S') {
        if (at(x + 1, y - 1) == 'M' && at(x + 1, y + 1) == 'M') {
            return 1
        }
    } else if (at(x - 1, y + 1) == 'S' && at(x - 1, y - 1) == 'S') {
        if (at(x + 1, y + 1) == 'M' && at(x + 1, y - 1) == 'M') {
            return 1
        }
    }
    return 0
}

// calculate answers

val day = 4
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
