package aoc.aoc2025

import aoc.AocDay

class AocDay11: AocDay(11, 2025) {
    companion object { @JvmStatic fun main(args: Array<String>) { AocDay11().run() } }

    override val testinput = """
        aaa: you hhh
        you: bbb ccc
        bbb: ddd eee
        ccc: ddd eee fff
        ddd: ggg
        eee: out
        fff: out
        ggg: out
        hhh: ccc fff iii
        iii: out
    """.trimIndent().lines()

    val testinput2 = """
        svr: aaa bbb
        aaa: fft
        fft: ccc
        bbb: tty
        tty: ccc
        ccc: ddd eee
        ddd: hub
        hub: fff
        eee: dac
        dac: fff
        fff: ggg hhh
        ggg: out
        hhh: out
    """.trimIndent().lines()

    fun String.parse(): Setup {
        val first = substringBefore(":")
        val leadsTo = substringAfter(":").trim().split(" ").toSet()
        return Setup(first, leadsTo)
    }

    override fun calc1(input: List<String>): Long {
        val nodes = input.map { it.parse() }.associateBy { it.name }
        return "you".pathsTo(nodes, "out")
    }

    /** Calculate number of paths to target node, aggregating the result in the table. */
    fun String.pathsTo(nodes: Map<String, Setup>, tgt: String, table: MutableMap<String, Long> = mutableMapOf()): Long {
        if (this in table) return table[this]!!
        val result = when {
            this == tgt -> return 1L
            this !in nodes -> return 0L
            else -> nodes[this]!!.leadsTo.sumOf { it.pathsTo(nodes, tgt, table) }
        }
        table[this] = result
        return result
    }

    override fun test2() = calc2(testinput2)

    override fun calc2(input: List<String>): Long {
        val nodes = input.map { it.parse() }.associateBy { it.name }
        val a = "svr".pathsTo(nodes - "dac", "fft")
        val c = "fft".pathsTo(nodes, "dac")
        val f = "dac".pathsTo(nodes - "fft", "out")
        val b = "svr".pathsTo(nodes - "fft", "dac")
        val d = "dac".pathsTo(nodes, "fft")
        val e = "fft".pathsTo(nodes - "dac", "out")
        return a * c * f + b * d * e
    }
}

class Setup(val name: String, val leadsTo: Set<String>)