import aoc.util.AocParser.Companion.parselines
import aoc.*
import aoc.getDayInput
import aoc.util.*
import kotlin.math.min

val testInput = """
Button A: X+94, Y+34
Button B: X+22, Y+67
Prize: X=8400, Y=5400

Button A: X+26, Y+66
Button B: X+67, Y+21
Prize: X=12748, Y=12176

Button A: X+17, Y+86
Button B: X+84, Y+37
Prize: X=7870, Y=6450

Button A: X+69, Y+23
Button B: X+27, Y+71
Prize: X=18641, Y=10279
""".parselines

class ButtonPrize(val a: Coord, val b: Coord, val prize: Coord) {
    fun solve(): Int {
        var min_cost = Int.MAX_VALUE
        for (na in 0..100) {
            val rem = prize.x - na * a.x
            if (rem % b.x == 0) {
                val nb = rem / b.x
                val cost = 3 * na + nb
                if (nb >= 0) {
                    val y = na * a.y + nb * b.y
                    if (y == prize.y && cost < min_cost) {
                        min_cost = cost
                    }
                }
            }
        }
        return if (min_cost == Int.MAX_VALUE) 0 else min_cost
    }
    fun solve_alt(): Int {
        val soln = solveInt(Exy(a.x, b.x, prize.x), Exy(a.y, b.y, prize.y))
        return if (soln == null) 0 else 3 * soln.x + soln.y
    }
    fun solve2(): Long {
        val soln = solveLong(ExyLong(a.x.toLong(), b.x.toLong(), 10000000000000L + prize.x),
                             ExyLong(a.y.toLong(), b.y.toLong(), 10000000000000L + prize.y))
        return if (soln == null) 0 else 3 * soln.first + soln.second
    }
}

// part 1

fun List<String>.prizes() = joinToString("\n").split("\n\n").map {
    val lines = it.lines().map {
        it.substringAfter(": ").split(", ")
            .map { it.substringAfter("+").substringAfter("=").toInt() }
    }
    ButtonPrize(
        lines[0][0] to lines[0][1],
        lines[1][0] to lines[1][1],
        lines[2][0] to lines[2][1]
    )
}

fun List<String>.part1() = prizes().sumOf { it.solve_alt() }

// part 2

fun List<String>.part2() = prizes().sumOf { it.solve2() }

// calculate answers

val day = 13
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
