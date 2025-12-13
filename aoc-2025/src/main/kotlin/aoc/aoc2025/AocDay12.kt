package aoc.aoc2025

import aoc.AocDay
import aoc.util.ANSI_DARK_GREEN
import aoc.util.ANSI_RED
import aoc.util.ANSI_RESET
import aoc.util.CharGrid
import kotlin.collections.sortedBy

class AocDay12: AocDay(12, 2025) {
    companion object { @JvmStatic fun main(args: Array<String>) { AocDay12().run() } }

    override val testinput = """
        0:
        ###
        ##.
        ##.

        1:
        ###
        ##.
        .##

        2:
        .##
        ###
        ##.

        3:
        ##.
        ###
        ##.

        4:
        ###
        #..
        ###

        5:
        ###
        .#.
        ###

        4x4: 0 0 0 0 2 0
        12x5: 1 0 1 0 2 2
        12x5: 1 0 1 0 3 2
    """.trimIndent().lines()

    fun List<String>.parse(): Puzzle12 {
        val patterns: List<CharGrid> = takeWhile { 'x' !in it }.joinToString("\n").trim()
            .split("\n\n")
            .map { CharGrid(it.trim().lines().drop(1)) }

        val challenges = dropWhile { 'x' !in it }
            .map {
                val len = it.substringBefore('x').trim().toInt()
                val wid = it.substringAfter('x').substringBefore(':').trim().toInt()
                val counts = it.substringAfter(':').trim().split(' ').map { it.toInt() }
                Challenge(len, wid, counts)
            }

        return Puzzle12(patterns, challenges)
    }

    override fun calc1(input: List<String>): Int {
        val puz = input.parse()
        println(puz)
        return puz.challenges.count { !puz.clearlyImpossible(it) }
    }

    override fun calc2(input: List<String>) = null
}

data class Puzzle12(
    val patterns: List<CharGrid>,
    var challenges: List<Challenge>
) {
    init {
        challenges = challenges.sortedBy { it.length * it.width - unitsNeeded(it) }
    }
    override fun toString(): String {
        val sb = StringBuilder()
        patterns.first().ch.indices.forEach { i ->
            sb.append(patterns.joinToString("   ") { it.ch[i] })
            sb.append("\n")
        }
        sb.append("\nChallenges:\n")
        challenges.forEach { c ->
            val impossible = clearlyImpossible(c)
            val color = if (impossible) ANSI_RED else ANSI_DARK_GREEN
            val available = c.length * c.width - unitsNeeded(c)
            sb.append("$color  ${c.length}x${c.width}: ${c.counts.joinToString(" ")} [$available] $ANSI_RESET\n")
        }
        return sb.toString()
    }

    fun CharGrid.countUsed() = count { it == '#' }

    fun unitsNeeded(challenge: Challenge) =
        challenge.counts.zip(patterns).sumOf { (count, pattern) ->
            count * pattern.countUsed()
        }

    fun clearlyImpossible(c: Challenge): Boolean = unitsNeeded(c) > c.length * c.width
}

data class Challenge(
    val length: Int,
    val width: Int,
    val counts: List<Int>
)