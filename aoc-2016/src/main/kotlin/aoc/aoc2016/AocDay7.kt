package aoc.aoc2016

import aoc.AocDay

class AocDay7: AocDay(7, 2016) {
    companion object { @JvmStatic fun main(args: Array<String>) { AocDay7().run() } }

    override val testinput = """
        abba[mnop]qrst
        abcd[bddb]xyyx
        aaaa[qwer]tyui
        ioxxoj[asdfgh]zxcvbn
    """.trimIndent().lines()

    fun String.abba() = windowed(4, 1).any {
        it[0] == it[3] && it[1] == it[2] && it[0] != it[1]
    }

    fun String.aba() = windowed(3, 1).any { it[0] == it[2] && it[0] != it[1] }
    fun String.aba3() = length == 3 && this[0] == this[2] && this[0] != this[1]
    val String.abaInvert
        get() = "${this[1]}${this[0]}${this[1]}"

    override fun calc1(input: List<String>) = input.count {
        val pairs = it.split("[", "]").chunked(2)
        pairs.map { it.first() }.any { it.abba() } &&
                !pairs.mapNotNull { it.getOrNull(1) }.any { it.abba() }
    }
    override fun calc2(input: List<String>) = input.count {
        val pairs = it.split("[", "]").chunked(2)
        val abaInTriples = pairs.mapNotNull { it.getOrNull(1) }.flatMap { it.windowed(3, 1) }.toSet()
        val abaOutTriples = pairs.map { it.first() }.flatMap { it.windowed(3, 1) }.filter { it.aba3() }.toSet()
        abaOutTriples.any { it.abaInvert in abaInTriples }
    }
}