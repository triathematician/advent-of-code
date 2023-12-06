package aoc

import aoc.util.chunkint
import aoc.util.parseToMap

class AocDay16: AocDay(16) {
    companion object { @JvmStatic fun main(args: Array<String>) { AocDay16().run() } }

    override val testinput = """
        Sue 1: goldfish: 6, trees: 9, akitas: 0
        Sue 2: goldfish: 7, trees: 1, akitas: 0
        Sue 3: cars: 10, akitas: 6, perfumes: 7
        Sue 4: perfumes: 2, vizslas: 0, cars: 6
        Sue 5: goldfish: 1, trees: 3, perfumes: 10
    """.trimIndent().lines()

    val found = """
        children: 3
        cats: 7
        samoyeds: 2
        pomeranians: 3
        akitas: 0
        vizslas: 0
        goldfish: 5
        trees: 3
        cars: 2
        perfumes: 1
    """.trimIndent().parseToMap("\n") { it.toInt() } as Map<String, Int>

    fun String.parse() = split(':', limit = 2).let {
        val i = it[0].chunkint(1)
        val map = it[1].parseToMap { it.toInt() } as Map<String, Int>
        i to map
    }

    override fun calc1(input: List<String>): Int? {
        val sues = input.map { it.parse() }
        return sues.firstOrNull {
            it.second.all { (k, v) -> found[k] == v }
        }?.first
    }
    override fun calc2(input: List<String>): Int? {
        val sues = input.map { it.parse() }
        return sues.firstOrNull {
            it.second.all { (k, v) ->
                when (k) {
                    "cats", "trees" -> found[k]!! < v
                    "pomeranians", "goldfish" -> found[k]!! > v
                    else -> found[k] == v
                }
            }
        }?.first
    }
}