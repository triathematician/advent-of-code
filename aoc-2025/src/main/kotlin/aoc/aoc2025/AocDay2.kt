package aoc.aoc2025

import aoc.AocDay
import kotlin.math.log10
import kotlin.math.pow

class AocDay2: AocDay(2, 2025) {
    companion object { @JvmStatic fun main(args: Array<String>) { AocDay2().run() } }

    override val testinput = """
        11-22,95-115,998-1012,1188511880-1188511890,222220-222224,1698522-1698528,446443-446449,38593856-38593862,565653-565659,824824821-824824827,2121212118-2121212124
    """.trimIndent().lines()

    fun List<String>.ranges() =
        this[0].split(",").map {
            val (a, b) = it.split("-").map { v -> v.toLong() }
            a..b
        }

    fun Long.testRepeatedDigits2(): Boolean {
        val digs = log10(toDouble()).toInt() + 1
        if (digs % 2 == 1) return false
        val half = digs / 2
        val left = this / 10.0.pow(half).toLong()
        val right = this % 10.0.pow(half).toLong()
        return left == right
    }

    fun Long.testRepeatedDigits(n: Int): Boolean {
        val digs = log10(toDouble()).toInt() + 1
        if (n > digs) return false
        if (digs % n != 0) return false
        val partSize = digs / n
        val parts = mutableListOf<Long>()
        var rem = this
        repeat(n) {
            val part = rem % 10.0.pow(partSize).toLong()
            parts.add(part)
            rem /= 10.0.pow(partSize).toLong()
        }
        return parts.all { it == parts[0] }
    }

    fun Long.testRepeatedDigitsAny() = (2..20).any { testRepeatedDigits(it) }

    override fun calc1(input: List<String>): Long {
        return input.ranges().sumOf {
            it.sumOf { if (it.testRepeatedDigits2()) it else 0L }
        }
    }
    override fun calc2(input: List<String>): Long {
        return input.ranges().sumOf {
            it.sumOf { if (it.testRepeatedDigitsAny()) it else 0L }
        }
    }
}