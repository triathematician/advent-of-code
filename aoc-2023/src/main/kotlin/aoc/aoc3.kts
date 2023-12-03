import aoc.AocParser.Companion.parselines
import aoc.*
import aoc.util.Loc
import aoc.util.getDayInput

val testInput = """
467..114..
...*......
..35..633.
......#...
617*......
.....+.58.
..592.....
......755.
...$.*....
.664.598..
""".parselines

fun List<String>.grid() = map { it.toList() }

fun List<List<Char>>.symbols() = indices.flatMap { y ->
    this[y].indices.flatMap { x ->
        val c = this[y][x]
        if (c != '.' && !c.isDigit())
            listOf(c to Loc(x, y))
        else
            emptyList()
    }
}.groupBy { it.first }.mapValues { it.value.map { it.second } }

fun Map<Char, List<Loc>>.symbolLocs() = entries.flatMap { it.value }

fun List<List<Char>>.numLocs() = indices.flatMap { y ->
    val search = "[0-9]+".toRegex()
    search.findAll(this[y].joinToString("")).map {
        it.value.toInt() to Loc(it.range.first, y)
    }
}

// part 1

fun List<String>.part1(): Int {
    val grid = grid()
    val nums = grid().numLocs()
    val symbols = grid.symbols().symbolLocs()
    return nums.filter {
        (1..it.first.toString().length).any { i ->
            val testLoc = it.second.plus(i - 1, 0)
            symbols.any { it.adjacentTo(testLoc, diagonal = true) }
        }
    }.sumOf { it.first }
}

// part 2

fun List<String>.part2(): Int {
    val grid = grid()
    val nums = grid().numLocs()
    val symbols = grid.symbols()['*']!!
    return symbols.sumOf { loc ->
        val numsAdj = nums.filter {
            (1..it.first.toString().length).any { i ->
                val testLoc = it.second.plus(i - 1, 0)
                testLoc.adjacentTo(loc, diagonal = true)
            }
        }.map { it.first }
        if (numsAdj.size == 2)
            numsAdj[0] * numsAdj[1]
        else
            0
    }
}

// calculate answers

val day = 3
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
