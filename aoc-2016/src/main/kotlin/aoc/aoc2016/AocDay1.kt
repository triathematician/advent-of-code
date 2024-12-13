package aoc.aoc2016

import aoc.AocDay
import aoc.util.*

class AocDay1: AocDay(1, 2016) {
    companion object { @JvmStatic fun main(args: Array<String>) { AocDay1().run() } }

    override val testinput = """
        R8, R4, R4, R8
    """.trimIndent().lines()

    override fun calc1(input: List<String>): Int {
        var pos = ORIGIN
        var dir = UP
        input[0].split(", ").forEach {
            when (it[0]) {
                'R' -> dir = dir.turnRight()
                'L' -> dir = dir.turnLeft()
            }
            pos += dir * it.substring(1).toInt()
        }
        return pos.taxicab(ORIGIN)
    }

    override fun calc2(input: List<String>): Int {
        val visited = mutableSetOf<Coord>()
        var pos = ORIGIN
        var dir = UP
        input[0].split(", ").forEach {
            when (it[0]) {
                'R' -> dir = dir.turnRight()
                'L' -> dir = dir.turnLeft()
            }
            (1..it.substring(1).toInt()).forEach {
                pos += dir
                if (visited.contains(pos)) return pos.taxicab(ORIGIN)
                else visited += pos
            }
        }
        return -1
    }
}