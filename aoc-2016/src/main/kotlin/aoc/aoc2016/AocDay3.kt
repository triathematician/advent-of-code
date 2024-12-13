package aoc.aoc2016

import aoc.AocDay
import aoc.util.ints
import aoc.util.transpose
import aoc.util.triangle

class AocDay3: AocDay(3, 2016) {
    companion object { @JvmStatic fun main(args: Array<String>) { AocDay3().run() } }

    override val testinput = """
        101 301 501
        102 302 502
        103 303 503
        201 401 601
        202 402 602
        203 403 603
    """.trimIndent().lines()

    override fun calc1(input: List<String>) = input.count {
        it.ints(" ").triangle()
    }
    override fun calc2(input: List<String>) = input.map { it.ints(" ") }
        .chunked(3) { it.transpose() }
        .flatten()
        .count { it.triangle() }
}