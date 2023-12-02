import aoc.AocParser.Companion.parselines
import aoc.AocRunner
import aoc.aocInput
import aoc.print

val day = 3

val testInput = """

""".parselines

val input = aocInput(day).parselines

// test case

val testResult = 0
val testResult2 = 0

// part 1

val answer1 = 0
answer1.print

// part 2

val answer2 = 0
answer2.print

// print results

AocRunner(day,
    test = { "$testResult, $testResult2" },
    part1 = { answer1 },
    part2 = { answer2 }
).run()
