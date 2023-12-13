import aoc.AocParser.Companion.parselines
import aoc.*
import aoc.util.CharGrid
import aoc.util.getDayInput

val testInput = """
#.##..##.
..#.##.#.
##......#
##......#
..#.##.#.
..##..##.
#.#.##.#.

#...##..#
#....#..#
..##..###
#####.##.
#####.##.
..##..###
#....#..#
""".parselines

fun List<String>.parse(): List<CharGrid> = joinToString("\n").split("\n\n")
    .map { it.split("\n") }

fun CharGrid.reflectionHorizontal(smudge: Boolean): Int {
    for (x in 1..size-1) {
        val list1 = take(x)
        val list2 = takeLast(size-x)
        val size = minOf(list1.size, list2.size)
        if (equal(list1.takeLast(size), list2.take(size).reversed(), smudge)) {
            return x
        }
    }
    return 0
}

fun equal(list1: List<String>, list2: List<String>, smudge: Boolean): Boolean {
    return if (!smudge) {
        list1 == list2
    } else {
        list1.joinToString("\n").zip(list2.joinToString("\n"))
            .count { it.first != it.second } == 1
    }
}

fun CharGrid.reflectionVertical(smudge: Boolean): Int {
    val grid = map { it.toList() }
    val gridTransposed = grid[0].indices.map {
        grid.map { row -> row[it] }.joinToString("")
    }
    return gridTransposed.reflectionHorizontal(smudge)
}

// part 1

fun List<String>.part1(): Int = parse().sumOf {
    val rv = it.reflectionVertical(smudge = false)
    val rh = it.reflectionHorizontal(smudge = false)
    println(it.joinToString("\n"))
    println("col=$rv, row=$rh")
    println("-".repeat(30))
    rv + 100*rh
}

// part 2

fun List<String>.part2(): Int = parse().sumOf {
    val rv = it.reflectionVertical(smudge = true)
    val rh = it.reflectionHorizontal(smudge = true)
    println(it.joinToString("\n"))
    println("col=$rv, row=$rh")
    println("-".repeat(30))
    rv + 100*rh
}

// calculate answers

val day = 13
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
