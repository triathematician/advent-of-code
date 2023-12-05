package aoc

import aoc.util.chunk
import aoc.util.chunkint

class AocDay14: AocDay(14) {
    companion object { @JvmStatic fun main(args: Array<String>) { AocDay14().run() } }

    override val testinput = """
        Comet can fly 14 km/s for 10 seconds, but then must rest for 127 seconds.
        Dancer can fly 16 km/s for 11 seconds, but then must rest for 162 seconds.
    """.trimIndent().lines()

    class Reindeer(val name: String, val speed: Int, val st: Int, val rt: Int) {
        var dist = 0
        override fun toString() = "$name:${dist}km"
        /** Advance reindeer at given time, advancing by speed of flying or 0 if resting. */
        fun advance(t: Int): Int {
            val rem = t % (st + rt)
            val flying = rem < st
            if (flying)
                dist += speed
            return dist
        }
        fun traveled(t: Int): Int {
            val cycles = t / (st + rt)
            val rem = t % (st + rt)
            return speed*st*cycles + speed*minOf(st, rem)
        }
    }

    fun String.parse() = Reindeer(chunk(0), chunkint(3), chunkint(6), chunkint(13))

    override fun calc1(input: List<String>): Int {
        val rd = input.map { it.parse() }
        return rd.maxOf { it.traveled(2503) }
    }
    override fun calc2(input: List<String>): Int {
        val rd = input.map { it.parse() }
        val points = rd.associateWith { 0 }.toMutableMap()
        (0..2502).forEach { t ->
            val dists = rd.map { it.advance(t) }
            val max = dists.maxOrNull()!!
            rd.filter { it.dist == max }.forEach { points[it] = points[it]!! + 1 }
            println("$t: $rd $points")
        }
        return points.values.maxOrNull()!!
    }
}