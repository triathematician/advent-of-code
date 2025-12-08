package aoc.aoc2025

import aoc.AocDay
import aoc.util.pairwise
import aoc.util.second

class AocDay8: AocDay(8, 2025) {
    companion object { @JvmStatic fun main(args: Array<String>) { AocDay8().run() } }

    override val testinput = """
        162,817,812
        57,618,57
        906,360,560
        592,479,940
        352,342,300
        466,668,158
        542,29,236
        431,825,988
        739,650,466
        52,470,668
        216,146,977
        819,987,18
        117,168,530
        805,96,715
        346,949,466
        970,615,88
        941,993,340
        862,61,35
        984,92,344
        425,690,689
    """.trimIndent().lines()

    fun List<String>.coords() = map {
        it.trim().split(",").map { it.toInt() }
            .let { Coord3(it[0], it[1], it[2]) }
    }

    override fun calc1(input: List<String>): Long {
        val cc = input.coords()
        val n = if (input.size < 100) 10 else 1000
        val dists = cc.pairwise()
            .associateWith { it.first().distanceSq(it.second()) }
            .entries
            .sortedBy { it.value }
            .take(n)
        var circuits = cc.map { Circuit(setOf(it)) }
        dists.forEach {
            val c2 = it.key
            val circ1 = circuits.find { c -> c.cc.contains(c2.first()) }!!
            val circ2 = circuits.find { c -> c.cc.contains(c2.second()) }!!
            if (circ1 != circ2) {
                circuits = (circuits - setOf(circ1, circ2)) + setOf(Circuit(circ1.cc + circ2.cc))
            }
        }
        val largest3 = circuits.sortedByDescending { it.cc.size }.take(3)
        return largest3[0].cc.size.toLong() * largest3[1].cc.size.toLong() * largest3[2].cc.size.toLong()
    }

    override fun calc2(input: List<String>): Long {
        val cc = input.coords()
        val dists = cc.pairwise()
            .associateWith { it.first().distanceSq(it.second()) }
            .entries
            .sortedBy { it.value }
        var circuits = cc.map { Circuit(setOf(it)) }
        dists.forEach {
            val c2 = it.key
            val circ1 = circuits.find { c -> c.cc.contains(c2.first()) }!!
            val circ2 = circuits.find { c -> c.cc.contains(c2.second()) }!!
            if (circ1 != circ2) {
                circuits = (circuits - setOf(circ1, circ2)) + setOf(Circuit(circ1.cc + circ2.cc))
            }
            if (circuits.size == 1) {
                return c2.first().x.toLong() * c2.second().x.toLong()
            }
        }
        return -1L
    }
}

data class Circuit(val cc: Set<Coord3>) {
    fun distanceSq(c2: Circuit): Long {
        return cc.minOf {
            c1 -> c2.cc.minOf { c2 -> c1.distanceSq(c2) }
        }
    }
}

data class Coord3(var x: Int, var y: Int, var z: Int) {
    fun distanceSq(c2: Coord3): Long {
        val xx = c2.x - x.toLong()
        val yy = c2.y - y.toLong()
        val zz = c2.z - z.toLong()
        return xx*xx + yy*yy + zz*zz
    }
}