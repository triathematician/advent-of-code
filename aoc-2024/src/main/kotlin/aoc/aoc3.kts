import aoc.util.AocParser.Companion.parselines
import aoc.*
import aoc.getDayInput

val testInput = """
xmul(2,4)&mul[3,7]!^don't()_mul(5,5)+mul(32,64](mul(11,8)undo()?mul(8,5))
""".parselines

// part 1

val regex = "mul\\((\\d{1,3}),(\\d{1,3})\\)".toRegex()

fun List<String>.part1(): Int {
    var sum = 0
    forEach { line ->
        regex.findAll(line).forEach { match ->
            val (a, b) = match.destructured
            sum += a.toInt() * b.toInt()
        }
    }
    return sum
}

// part 2

fun List<String>.part2(): Int {
    val doStatements = joinToString("").splitOn("do()") { it.substringBefore("don't()") }
    return doStatements.part1()
}

// calculate answers

val day = 3
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
