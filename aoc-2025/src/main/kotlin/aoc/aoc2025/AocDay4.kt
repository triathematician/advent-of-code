package aoc.aoc2025

import aoc.AocDay
import aoc.util.CharGrid
import aoc.util.Coord
import aoc.util.grid
import aoc.util.neighbors8
import aoc.util.x
import aoc.util.y

class AocDay4: AocDay(4, 2025) {
    companion object { @JvmStatic fun main(args: Array<String>) { AocDay4().run() } }

    override val testinput = """
        ..@@.@@@@.
        @@@.@.@.@@
        @@@@@.@.@@
        @.@@@@..@.
        @@.@@@@.@@
        .@@@@@@@.@
        .@.@.@.@@@
        @.@@@.@@@@
        .@@@@@@@@.
        @.@.@@@.@.
    """.trimIndent().lines()

    fun canRemove(grid: CharGrid): List<Coord> {
        var result = mutableListOf<Coord>()
        for (c in grid.allIndices()) {
            val v = grid.at(c)
            if (v == '.') continue
            val neighbors = c.neighbors8(grid)
            val count = neighbors.count { grid.at(it) == '@' }
            if (count < 4) result.add(c)
        }
        return result
    }

    override fun calc1(input: List<String>): Int {
        val grid = input.grid()
        return canRemove(grid).size
    }
    override fun calc2(input: List<String>): Int {
        val grid = input.grid()
        val  totalRemoved = mutableListOf<Coord>()
        var oldRemainder = 0
        var remainder = grid.count { it == '@' }
        while (oldRemainder != remainder) {
            val toRemove = canRemove(grid)
            toRemove.forEach { grid.put(it.x, it.y, '.') }
            totalRemoved += toRemove
            oldRemainder = remainder
            remainder = grid.count { it == '@' }
        }
        return totalRemoved.size
    }
}