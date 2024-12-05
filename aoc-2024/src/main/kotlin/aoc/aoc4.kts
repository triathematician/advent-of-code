import aoc.util.AocParser.Companion.parselines
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

fun List<String>.part1() = allIndices2().sumOf {
    DIRS8.filter { dir ->
        matchAt("XMAS", it, dir)
    }.size
}

// part 2

fun List<String>.part2(): Int {
    return allIndices2().sumOf {
        xmas2(it)
    }
}

fun CharGrid.mas(c: Coord, dir: Coord) = matchAt("MAS", c, dir)

fun CharGrid.xmas2(c: Coord): Int {
    if (c + NW !in this || c + SE !in this || at(c) != 'A')
        return 0
    val foundX = (mas(c + NW, SE) || mas(c + SE, NW)) &&
            (mas(c + NE, SW) || mas(c + SW, NE))
    return if (foundX) 1 else 0
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
