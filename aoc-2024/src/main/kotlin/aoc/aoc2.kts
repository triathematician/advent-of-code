import aoc.AocParser.Companion.parselines
import aoc.*
import aoc.util.getDayInput
import aoc.util.pairwise
import aoc.util.parseInts
import kotlin.math.absoluteValue

val testInput = """
7 6 4 2 1
1 2 7 8 9
9 7 6 2 1
1 3 2 4 5
8 6 4 4 1
1 3 6 7 9
""".parselines

// part 1

fun List<String>.part1(): Int {
    val reports = map { it.parseInts() }
    return reports.count { it.isSafe() }
}

fun List<Int>.isSafe(): Boolean {
    val diffs = indices.drop(1).map { this[it] - this[it - 1] }
    val sameSign = diffs.all { it > 0 } || diffs.all { it < 0 }
    val diffSet = diffs.map { it.absoluteValue }.toSet() - setOf(1, 2, 3)
    return sameSign && diffSet.isEmpty()
}

// part 2

fun List<String>.part2(): Int {
    val reports = map { it.parseInts() }
    return reports.count { it.isFlexSafe() }
}

fun List<Int>.isFlexSafe(): Boolean {
    if (isSafe()) return true
    indices.forEach {
        val list = toMutableList()
        list.removeAt(it)
        if (list.isSafe()) return true
    }
    return false
}

// calculate answers

val day = 2
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
