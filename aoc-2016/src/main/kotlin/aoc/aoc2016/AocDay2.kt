package aoc.aoc2016

import aoc.AocDay
import aoc.util.*

class AocDay2: AocDay(2, 2016) {
    companion object { @JvmStatic fun main(args: Array<String>) { AocDay2().run() } }

    override val testinput = """
        ULL
        RRDDD
        LURDL
        UUUUD
    """.trimIndent().lines()

    override fun calc1(input: List<String>): List<Int> {
        var start = 5
        return input.map {
            start = it.code(start)
            start
        }
    }

    fun String.code(start: Int): Int {
        var pos = start
        forEach {
            when (it) {
                'U' -> if (pos > 3) pos -= 3
                'D' -> if (pos <= 6) pos += 3
                'L' -> if (pos % 3 != 1) pos--
                'R' -> if (pos % 3 != 0) pos++
            }
        }
        return pos
    }

    override fun calc2(input: List<String>): List<Char> {
        var start = '5'
        return input.map {
            start = it.code2(start)
            start
        }
    }

    val board = """
            1
          2 3 4
        5 6 7 8 9
          A B C
            D
    """.trimIndent().lines().grid()

    val moves = (('1'..'9') + ('A'..'D')).associateWith {
        val pos = board.find(it)
        val up = board.getOrNull(pos + UP)
        val down = board.getOrNull(pos + DOWN)
        val left = board.getOrNull(pos + LEFT + LEFT)
        val right = board.getOrNull(pos + RIGHT + RIGHT)
        listOf(up, down, left, right).map { if (it == ' ') null else it }
    }

    fun String.code2(start: Char): Char {
        var pos = start
        forEach {
            pos = when (it) {
                'U' -> moves[pos]!![0] ?: pos
                'D' -> moves[pos]!![1] ?: pos
                'L' -> moves[pos]!![2] ?: pos
                'R' -> moves[pos]!![3] ?: pos
                else -> throw IllegalStateException("Invalid move: $it")
            }
        }
        return pos
    }
}