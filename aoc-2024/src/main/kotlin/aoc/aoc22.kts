import aoc.util.AocParser.Companion.parselines
import aoc.*
import aoc.getDayInput
import aoc.util.*

val testInput = """
1
10
100
2024
""".parselines

val testInput2 = """
1
2
3
2024
""".parselines

fun Long.secretNumber(n: Int): Long {
    if (n == 0)
        return this
    val nextA = ((this * 64) xor this) % 16777216
    val nextB = ((nextA / 32) xor nextA) % 16777216
    val nextC = ((nextB * 2048) xor nextB) % 16777216
    return nextC.secretNumber(n - 1)
}

data class Buyer(val digits: List<Int>, val deltas: List<Int>) {
    /** Provide digit for each delta in the list. */
    val deltaLookup: MutableMap<List<Int>, Int> = mutableMapOf()
    init {
        val deltaEntries = deltas.windowed(4, 1).mapIndexed { i, list ->
            list to digits[i + 4]
        }
        deltaEntries.forEach {
            if (it.first !in deltaLookup)
                deltaLookup[it.first] = it.second
        }
    }
    fun bananasAtDelta(delta: List<Int>) = deltaLookup[delta] ?: 0
}

/** Generate list of secret digits (final digit) for the given seed, including the first digit. Also calculates deltas in a second list. */
fun Long.sequence(n: Int = 2000): Buyer {
    val digits = mutableListOf<Int>((this % 10).toInt())
    val deltas = mutableListOf<Int>()
    var cur = this
    repeat(n) {
        val next = cur.secretNumber(1)
        val dig = (next % 10).toInt()
        deltas.add(dig - digits.last())
        digits.add(dig)
        cur = next
    }
    return Buyer(digits, deltas)
}

// part 1

fun List<String>.part1() = sumOf { it.toLong().secretNumber(2000) }

// part 2

println(123L.sequence(10))

fun List<String>.part2(): Int {
    val allBuyers = map { it.toLong().sequence() }
    val allDeltas4 = allBuyers.flatMap { it.deltas.windowed(4, 1) }.eachCount().entries
        .sortedByDescending { it.value + it.key.sum() }
    println("There are ${allDeltas4.size} unique sequences of deltas.")
    var max = 0
    allDeltas4.forEach { (delta, n) ->
        val sum = allBuyers.sumOf { b -> b.bananasAtDelta(delta) }
        if (sum > max) {
            println("  new max bananas: $sum for $delta (occurred $n times)")
            max = sum
        }
    }
    return max
}

// calculate answers

val day = 22
val input = getDayInput(day, 2024)
val testResult = testInput.part1()
val testResult2 = testInput2.part2()
val answer1 = input.part1().also { it.print }
val answer2 = input.part2().also { it.print }

// print results

AocRunner(day,
    test = { "$testResult, $testResult2" },
    part1 = { answer1 },
    part2 = { answer2 }
).run()
