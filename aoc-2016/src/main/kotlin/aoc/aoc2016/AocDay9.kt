package aoc.aoc2016

import aoc.AocDay
import aoc.util.between
import aoc.util.ints

class AocDay9: AocDay(9, 2016) {
    companion object { @JvmStatic fun main(args: Array<String>) { AocDay9().run() } }

    override val testinput = """
        ADVENT
        A(1x5)BC
        (3x3)XYZ
        A(2x2)BCD(2x2)EFG
        (6x1)(1x3)A
        X(8x2)(3x3)ABCY
        (25x3)(3x3)ABC(2x3)XY(5x2)PQRSTX(18x9)(3x2)TWO(5x7)SEVEN
        (27x12)(20x12)(13x14)(7x10)(1x12)A
    """.trimIndent().lines()

    fun String.decompress(): String {
        val sb = StringBuilder()
        val regex = """\((\d+)x(\d+)\)""".toRegex()
        var start = 0
        regex.findAll(this).forEach {
            if (start <= it.range.first) {
                val (len, count) = it.destructured
                sb.append(substring(start, it.range.first))
                val repeat = substring(it.range.last + 1, it.range.last + 1 + len.toInt())
                sb.append(repeat.repeat(count.toInt()))
                start = it.range.last + 1 + len.toInt()
            }
        }
        sb.append(substring(start))
        return sb.toString()
    }

    fun String.decompressCount1(): Long {
        if ('(' !in this) return length.toLong()
        val before = substringBefore("(", this)
        val after = substringAfter(")", "")
        val between = between("(", ")").ints("x")
        val afterSubstr = after.take(between[0])
        val afterRepeatCount = between[1]
        return before.length.toLong() +
                (afterSubstr.repeat(afterRepeatCount) + after.substringAfter(afterSubstr).decompress()).decompressCount1()
    }

    fun String.decompressCount2(): Long {
        var total = 0L
        val regex = """\((\d+)x(\d+)\)""".toRegex()
        var start = 0
        regex.findAll(this).forEach {
            if (start <= it.range.first) {
                val (len, count) = it.destructured
                total += it.range.first - start
                val repeat = substring(it.range.last + 1, it.range.last + 1 + len.toInt())
                total += repeat.decompressCount2() * count.toInt()
                start = it.range.last + 1 + len.toInt()
            }
        }
        total += length - start
        return total
    }

    override fun calc1(input: List<String>): Int {
        input.forEach { if (it.length < 100) println(it.decompress()) }
        return input[0].decompress().length
    }
    override fun calc2(input: List<String>): Long {
        input.forEach { if (it.length < 100) println(it.decompressCount2()) }
        return input[0].decompressCount2()
    }
}