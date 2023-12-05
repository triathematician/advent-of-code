package aoc

import aoc.util.chunk
import aoc.util.chunkint

class AocDay15: AocDay(15) {
    companion object { @JvmStatic fun main(args: Array<String>) { AocDay15().run() } }

    override val testinput = """
        Butterscotch: capacity -1, durability -2, flavor 6, texture 3, calories 8
        Cinnamon: capacity 2, durability 3, flavor -2, texture -1, calories 3
    """.trimIndent().lines()

    class Ingr(val name: String, val c: Int, val d: Int, val f: Int, val t: Int, val cal: Int)
    class Cookie(val ingr: Map<Ingr, Int>) {
        val c = ingr.map { it.key.c * it.value }.sum().coerceAtLeast(0)
        val d = ingr.map { it.key.d * it.value }.sum().coerceAtLeast(0)
        val f = ingr.map { it.key.f * it.value }.sum().coerceAtLeast(0)
        val t = ingr.map { it.key.t * it.value }.sum().coerceAtLeast(0)
        val cal = ingr.map { it.key.cal * it.value }.sum()
        fun score() = c*d*f*t
    }

    fun String.chunkints(n: Int) = chunk(n).substringBefore(",").toInt()
    fun String.parse() = Ingr(chunk(0).dropLast(1), chunkints(2), chunkints(4), chunkints(6), chunkints(8), chunkints(10))

    fun bestScore(ingrs: List<Ingr>, tsp: Int, c: Cookie, cals: Int?): Int {
        if (ingrs.size == 1) {
            val c2 = Cookie(c.ingr + (ingrs[0] to tsp))
            return if (cals != null && c2.cal != cals) 0 else c2.score()
        }
        return (0..tsp).maxOf {
            bestScore(ingrs.drop(1), tsp - it, Cookie(c.ingr + (ingrs[0] to it)), cals)
        }
    }

    override fun calc1(input: List<String>): Int {
        val ingrs = input.map { it.parse() }
        return bestScore(ingrs, 100, Cookie(mapOf()), null)
    }
    override fun calc2(input: List<String>): Int {
        val ingrs = input.map { it.parse() }
        return bestScore(ingrs, 100, Cookie(mapOf()), 500)
    }
}