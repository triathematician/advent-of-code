import aoc.util.AocParser.Companion.parselines
import aoc.*
import aoc.getDayInput
import java.math.BigInteger

val testInput = """
125 17
""".parselines

// part 1

fun List<String>.part1(): Int {
    var nums = first().split(" ")
    (1..25).onEach {
        nums = nums.blink()
    }
    return nums.size
}

fun List<String>.blink() = flatMap {
    when {
        it == "0" -> listOf("1")
        it.length % 2 == 1 -> listOf((it.toBigInteger() * BigInteger.valueOf(2024)).toString())
        else -> listOf(it.substring(0, it.length / 2), it.substring(it.length / 2).removeZeros())
    }
}

/** Remove all prefix zeros except for the last. */
fun String.removeZeros() = dropWhile { it == '0' }.ifEmpty { "0" }

// part 2

fun List<String>.part2(): Long {
    var nums = first().split(" ")
        .groupingBy { it }.eachCount().mapValues { it.value.toLong() }
    (1..75).onEach {
        nums = nums.blink2()
        println("$it - ${nums.values.sum()}")
    }
    return nums.values.sum()
}

fun Map<String, Long>.blink2() = flatMap { (k, v) ->
    when {
        k == "0" -> listOf("1" to v)
        k.length % 2 == 1 -> listOf((k.toBigInteger() * BigInteger.valueOf(2024)).toString() to v)
        else -> listOf(k.substring(0, k.length / 2) to v, k.substring(k.length / 2).removeZeros() to v)
    }
}.groupingBy { it.first }.fold(0L) { acc, (_, v) -> acc + v }

// calculate answers

val day = 11
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
