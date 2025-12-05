package aoc.aoc2025

import aoc.AocDay
import kotlin.math.pow

class AocDay3: AocDay(3, 2025) {
    companion object { @JvmStatic fun main(args: Array<String>) { AocDay3().run() } }

    override val testinput = """
        987654321111111
        811111111111119
        234234234234278
        818181911112111
    """.trimIndent().lines()

    override fun calc1(input: List<String>) = input.sumOf { it.jolts() }

    fun String.jolts(): Long {
        var max = 0L
        (0 until length).forEach { i ->
            (i + 1 until length).forEach { j ->
                val n = this[i].digitToInt() * 10L + this[j].digitToInt()
                if (n > max) max = n
            }
        }
        return max
    }

    override fun calc2(input: List<String>) =
        input.sumOf { it.jolts2c(12) }

    data class Problem(val s: String, val n: Int)

    val cache = mutableMapOf<Problem, Long>()

    fun String.jolts2c(n: Int): Long {
        val p = Problem(this, n)
        return cache.getOrPut(p) {
            jolts2b(p)
        }
    }

    fun jolts2b(p: Problem): Long {
        val (s, n) = p
        if (n == s.length)
            return s.toLong()
        else if (n > s.length)
            return -1L
        else if (n == 0)
            return 0L
        val dig0 = s.dropLast(n-1).maxOf { it.digitToInt() }
        if (n == 1)
            return dig0.toLong()
        val startIndex = s.indexOfFirst { it.digitToInt() == dig0 }
        val maxA = dig0*10.0.pow(n-1).toLong() + s.substring(startIndex + 1).jolts2c(n-1)
        val nextDig0 = (1 until s.length).firstOrNull { s[it].digitToInt() == dig0 }
            ?: return maxA
        val maxB = s.substring(nextDig0).jolts2c(n)
        return maxOf(maxA, maxB)
    }

    fun String.jolts2(n: Int): Long {
        if (n == length)
            return toLong()
        else if (n == 0)
            return 0L
        var max = 0L
        (0 until length).forEach { i ->
            val maxsub = substring(i + 1).jolts2(n - 1)
            if (maxsub != -1L) {
                val num = this[i].digitToInt() * 10.0.pow(n - 1).toLong() + maxsub
                if (num > max)
                    max = num
            }
        }
        return max
    }
}