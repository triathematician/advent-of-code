package aoc

import aoc.util.chunk
import aoc.util.chunkint
import aoc.util.permutations

class AocDay13: AocDay(13) {
    companion object { @JvmStatic fun main(args: Array<String>) { AocDay13().run() } }

    override val testinput = """
        Alice would gain 54 happiness units by sitting next to Bob.
        Alice would lose 79 happiness units by sitting next to Carol.
        Alice would lose 2 happiness units by sitting next to David.
        Bob would gain 83 happiness units by sitting next to Alice.
        Bob would lose 7 happiness units by sitting next to Carol.
        Bob would lose 63 happiness units by sitting next to David.
        Carol would lose 62 happiness units by sitting next to Alice.
        Carol would gain 60 happiness units by sitting next to Bob.
        Carol would gain 55 happiness units by sitting next to David.
        David would gain 46 happiness units by sitting next to Alice.
        David would lose 7 happiness units by sitting next to Bob.
        David would gain 41 happiness units by sitting next to Carol.
    """.trimIndent().lines()

    data class Seating(val pers: String, val gain: String, val units: Int, val nextTo: String) {
        val delta = if (gain == "gain") units else -units
    }

    fun String.parse() = Seating(chunk(0), chunk(2), chunkint(3), chunk(10).dropLast(1))

    fun List<Seating>.score(a: String, b: String) = filter {
        (it.pers == a && it.nextTo == b) || (it.pers == b && it.nextTo == a)
    }.sumOf { it.delta }

    override fun calc1(input: List<String>): Int = input.map { it.parse() }.let {
        val people = it.map { it.pers }.toSet()
        people.toList().permutations().maxOf { p ->
            (p + p.first()).zipWithNext().sumOf {
                (a, b) -> it.score(a, b)
            }
        }
    }
    override fun calc2(input: List<String>): Int = input.map { it.parse() }.let {
        val people = it.map { it.pers }.toSet() + "you"
        people.toList().permutations().maxOf { p ->
            (p + p.first()).zipWithNext().sumOf {
                    (a, b) -> it.score(a, b)
            }
        }
    }
}