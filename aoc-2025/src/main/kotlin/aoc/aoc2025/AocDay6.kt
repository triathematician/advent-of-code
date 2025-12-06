package aoc.aoc2025

import aoc.AocDay

class AocDay6: AocDay(6, 2025) {
    companion object { @JvmStatic fun main(args: Array<String>) { AocDay6().run() } }

    override val testinput = """
        123 328  51 64 
         45 64  387 23 
          6 98  215 314
        *   +   *   +
    """.trimIndent().lines()

    override fun calc1(input: List<String>): Long {
        val nums = input.indices.toList().dropLast(1).map {
            input[it].trim().split("\\s+".toRegex()).map { it.toLong() }
        }
        val op = input.last().trim().split("\\s+".toRegex())
        var sum = 0L
        op.indices.forEach { i ->
            if (op[i] == "+")
                sum += nums.sumOf { it[i] }
            else
                sum += nums.map { it[i] }.reduce { a, b -> a * b }
        }
        return sum
    }
    override fun calc2(input: List<String>): Long {
        val maxLen = input.maxOfOrNull { it.length }!!
        input.forEach { println(it.take(100)) }
        var i = 0 // position in the grid
        var iNext = 0 // position of next op
        var sum = 0L
        var count = 0
        while (i < input.last().length) {
            val nextP = input.last().indexOf('+', i+1)
            val nextT = input.last().indexOf('*', i+1)
            iNext = when {
                nextP == -1 && nextT == -1 -> maxLen+1
                nextP == -1 -> nextT
                nextT == -1 -> nextP
                else -> minOf(nextP, nextT)
            }

            val op = input.last()[i]
            val nums = (i until iNext-1).map { r ->
                input.dropLast(1).map { it[r] }.joinToString("").trim().toLong()
            }
            if (op == '+')
                sum += nums.sum()
            else
                sum += nums.reduce { a, b -> a*b }
            i = iNext
            count++
        }
        return sum
    }
}