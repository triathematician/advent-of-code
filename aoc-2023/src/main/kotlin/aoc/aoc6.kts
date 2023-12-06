import aoc.AocParser.Companion.parselines
import aoc.*
import aoc.util.getDayInput
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.sqrt

val testInput = """
Time:      7  15   30
Distance:  9  40  200
""".parselines

class Race(val time: Long, val dist: Long) {
    fun numToBeat(): Long {
        val firstBeat = (0L..time).first { it * (time - it) > dist }
        return time - 2*firstBeat + 1
    }
    fun numToBeat_Solved(): Long {
        val t0 = .5*(time - sqrt(time*time - 4.0*dist))
        val t1 = .5*(time + sqrt(time*time - 4.0*dist))
        return (floor(t1) - ceil(t0)).toLong() + 1
    }
}

fun List<String>.parse(): List<Race> {
    val line0 = get(0).substringAfter(":").trim().split("\\s+".toRegex())
    val line1 = get(1).substringAfter(":").trim().split("\\s+".toRegex())
    return line0.indices.map {
        Race(line0[it].toLong(), line1[it].toLong())
    }
}

// part 1

fun List<String>.part1() = parse()
    .map { it.numToBeat_Solved() }
    .reduce { a, b -> a*b }

// part 2

fun List<String>.part2() = map { it.replace(" ", "") }
    .parse()
    .map { it.numToBeat_Solved() }
    .reduce { a, b -> a*b }

// calculate answers

val day = 6
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
