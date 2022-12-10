import aoc.ANSI_GREEN
import aoc.ANSI_RESET
import aoc.AocParser.Companion.parselines
import aoc.AocRunner

val day = 10

val testInput = """
    addx 15
    addx -11
    addx 6
    addx -3
    addx 5
    addx -1
    addx -8
    addx 13
    addx 4
    noop
    addx -1
    addx 5
    addx -1
    addx 5
    addx -1
    addx 5
    addx -1
    addx 5
    addx -1
    addx -35
    addx 1
    addx 24
    addx -19
    addx 1
    addx 16
    addx -11
    noop
    noop
    addx 21
    addx -15
    noop
    noop
    addx -3
    addx 9
    addx 1
    addx -3
    addx 8
    addx 1
    addx 5
    noop
    noop
    noop
    noop
    noop
    addx -36
    noop
    addx 1
    addx 7
    noop
    noop
    noop
    addx 2
    addx 6
    noop
    noop
    noop
    noop
    noop
    addx 1
    noop
    noop
    addx 7
    addx 1
    noop
    addx -13
    addx 13
    addx 7
    noop
    addx 1
    addx -33
    noop
    noop
    noop
    addx 2
    noop
    noop
    noop
    addx 8
    noop
    addx -1
    addx 2
    addx 1
    noop
    addx 17
    addx -9
    addx 1
    addx 1
    addx -3
    addx 11
    noop
    noop
    addx 1
    noop
    addx 1
    noop
    noop
    addx -13
    addx -19
    addx 1
    addx 3
    addx 26
    addx -30
    addx 12
    addx -1
    addx 3
    addx 1
    noop
    noop
    noop
    addx -9
    addx 18
    addx 1
    addx 2
    noop
    noop
    addx 9
    noop
    noop
    noop
    addx -1
    addx 2
    addx -37
    addx 1
    addx 3
    noop
    addx 15
    addx -21
    addx 22
    addx -6
    addx 1
    noop
    addx 2
    addx 1
    noop
    addx -10
    noop
    noop
    addx 20
    addx 1
    addx 2
    addx 2
    addx -6
    addx -11
    noop
    noop
    noop
""".parselines

val input = """
    addx 1
    addx 4
    addx 1
    noop
    addx 4
    addx 3
    addx -2
    addx 5
    addx -1
    noop
    addx 3
    noop
    addx 7
    addx -1
    addx 1
    noop
    addx 6
    addx -1
    addx 5
    noop
    noop
    noop
    addx -37
    addx 7
    noop
    noop
    noop
    addx 5
    noop
    noop
    noop
    addx 9
    addx -8
    addx 2
    addx 5
    addx 2
    addx 5
    noop
    noop
    addx -2
    noop
    addx 3
    addx 2
    noop
    addx 3
    addx 2
    noop
    addx 3
    addx -36
    noop
    addx 26
    addx -21
    noop
    noop
    noop
    addx 3
    addx 5
    addx 2
    addx -4
    noop
    addx 9
    addx 5
    noop
    noop
    noop
    addx -6
    addx 7
    addx 2
    noop
    addx 3
    addx 2
    addx 5
    addx -39
    addx 34
    addx 5
    addx -35
    noop
    addx 26
    addx -21
    addx 5
    addx 2
    addx 2
    noop
    addx 3
    addx 12
    addx -7
    noop
    noop
    noop
    noop
    noop
    addx 5
    addx 2
    addx 3
    noop
    noop
    noop
    noop
    addx -37
    addx 21
    addx -14
    addx 16
    addx -11
    noop
    addx -2
    addx 3
    addx 2
    addx 5
    addx 2
    addx -15
    addx 6
    addx 12
    addx -2
    addx 9
    addx -6
    addx 7
    addx 2
    noop
    noop
    noop
    addx -33
    addx 1
    noop
    addx 2
    addx 13
    addx 15
    addx -21
    addx 21
    addx -15
    noop
    noop
    addx 4
    addx 1
    noop
    addx 4
    addx 8
    addx 6
    addx -11
    addx 5
    addx 2
    addx -35
    addx -1
    noop
    noop
""".parselines

// test case

val testResult = apply(testInput).strengthsAt(listOf(20, 60, 100, 140, 180, 220))
draw(apply(testInput))

/** Apply input, get list of register values during cycle N. */
fun apply(input: List<String>): List<Int> {
    var clock = 1
    var x = 1
    val res = mutableListOf<Int>()
    res += x
    input.forEach {
        when (it) {
            "noop" -> {
                res += x
                clock++
            }
            else -> {
                clock += 2
                res += x
                x += it.substringAfter(" ").toInt()
                res += x
            }
        }
    }
    return res
}

fun List<Int>.strengthsAt(times: List<Int>): Int {
    return times.sumOf { it * this[it - 1] }
}

fun draw(signal: List<Int>) {
    signal.chunked(40).filter { it.size == 40 }.forEach { row ->
        (0..39).forEach {
            val s = row[it]
            if (it in s-1..s+1) print("${ANSI_GREEN}##${ANSI_RESET}") else print("..")
        }
        println()
    }
}

// part 1

val answer1 = apply(input).strengthsAt(listOf(20, 60, 100, 140, 180, 220))

// part 2

val answer2 = draw(apply(input))

// print results

AocRunner(day,
    info = { listOf("Leaderboard: 5:17/12:17", "Answer: 15020/EFUGLPAP") },
    test = { testResult },
    part1 = { answer1 },
    part2 = { answer2 }
).run()
