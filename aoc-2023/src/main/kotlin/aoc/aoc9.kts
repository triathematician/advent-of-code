import aoc.AocParser.Companion.parselines
import aoc.*
import aoc.util.getDayInput

val testInput = """
0 3 6 9 12 15
1 3 6 10 15 21  
10 13 16 21 30 45 
""".parselines

// part 1

fun String.parse() = split(" ").map { it.toInt() }

fun List<Int>.diffs() = zipWithNext { a, b -> b - a }

fun List<Int>.extrapolate(): Long {
    val diffs = mutableListOf<List<Int>>()
    var cur = this
    while (cur.any { it != 0 }) {
        diffs.add(cur)
        cur = cur.diffs()
    }
    val lastNums = diffs.map { it.last() }
    val res = lastNums.sumOf { it.toLong() }
    return res
}

fun List<Int>.extrapolateBack(): Long {
    val diffs = mutableListOf<List<Int>>()
    var cur = this
    while (cur.any { it != 0 }) {
        diffs.add(cur)
        cur = cur.diffs()
    }
    val firstNums = diffs.map { it.first() }
    val res = firstNums.mapIndexed { i, it ->
        if (i % 2 == 0) it.toLong() else -it.toLong()
    }.sum()
    return res
}

fun List<String>.part1() = sumOf { it.parse().extrapolate() }

// part 2

fun List<String>.part2() = sumOf { it.parse().extrapolateBack() }

// calculate answers

val day = 9
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
