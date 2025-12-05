package aoc.aoc2025

import aoc.AocDay

class AocDay5: AocDay(5, 2025) {
    companion object { @JvmStatic fun main(args: Array<String>) { AocDay5().run() } }

    override val testinput = """
        3-5
        10-14
        16-20
        12-18

        1
        5
        8
        11
        17
        32
    """.trimIndent().lines()

    class Input(val ranges: List<LongRange>, val ingrs: List<Long>) {
        fun fresh(x: Long) = ranges.any { x in it }
    }

    fun List<String>.parse(): Input {
        val ranges = this[0].lines()
            .map { it.split("-").let { it[0].toLong()..it[1].toLong() } }
        val ingrs = this[1].lines().map { it.toLong() }
        return Input(ranges, ingrs)
    }

    override fun calc1(input: List<String>): Int {
        val inputX = input.joinToString("\n").split("\n\n").parse()
        return inputX.ingrs.count { inputX.fresh(it) }
    }
    override fun calc2(input: List<String>): Long {
        val ranges = input.joinToString("\n").split("\n\n").parse()
            .ranges.sortedBy { it.first }
        var merged = listOf<LongRange>()
        ranges.forEach { merged = mergeInto(it, merged) }
        return merged.sumOf {
            it.last - it.first + 1
        }
    }

    fun mergeInto(r: LongRange, ranges: List<LongRange>): List<LongRange> {
        if (ranges.isEmpty())
            return listOf(r)
        for (r2 in ranges) {
            if (!disjoint(r, r2)) {
                return mergeInto(union(r, r2), (ranges - setOf(r2)).toList())
            }
        }
        return ranges + listOf(r)
    }

    fun disjoint(r: LongRange, r2: LongRange) =
        r2.first > r.last || r2.last < r.first

    fun union(r: LongRange, r2: LongRange) =
        LongRange(minOf(r.first, r2.first), maxOf(r.last, r2.last))
}