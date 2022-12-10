import aoc.AocParser.Companion.parselines
import aoc.AocRunner
import aoc.print

val day = 11

val testInput = """
    
""".parselines

val input = """
    
""".parselines

// test case

val testResult = 0

// part 1

val answer1 = 0
answer1.print

// part 2

val answer2 = 0
answer2.print

// print results

AocRunner(day,
    info = { listOf("Leaderboard: ", "Answers: ") },
    test = { testResult },
    part1 = { answer1 },
    part2 = { answer2 }
).run()
