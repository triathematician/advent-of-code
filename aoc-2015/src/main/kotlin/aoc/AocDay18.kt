package aoc

import aoc.util.*

class AocDay18: AocDay(18) {
    companion object { @JvmStatic fun main(args: Array<String>) { AocDay18().run() } }

    override val testinput = """
        .#.#.#
        ...##.
        #....#
        ..#...
        #.#..#
        ####..
    """.trimIndent().lines()

    private fun CharGrid.step(): CharGrid {
        val res = CharGrid(ch)
        allIndices().forEach { (x, y) ->
            val n = Coord(x, y).neighbors8(this).count { at(it) == '#' }
            res.put(x, y, when {
                get(x, y) == '#' && n in 2..3 -> '#'
                get(x, y) == '.' && n == 3 -> '#'
                else -> '.'
            })
        }
        return res
    }

    override fun calc1(input: List<String>): Int {
        val steps = if (input.size < 10) 5 else 100
        var grid = CharGrid(input)
        (1..steps).forEach {
           grid = grid.step()
        }
        return grid.count { it == '#' }
    }
    override fun calc2(input: List<String>): Int {
        val steps = if (input.size < 10) 5 else 100
        val sz = input.size
        var grid = CharGrid(input)
        grid.put(0, 0, '#')
        grid.put(0, sz-1, '#')
        grid.put(sz-1, 0, '#')
        grid.put(sz-1, sz-1, '#')
        (1..steps).forEach {
            grid = grid.step()
            grid.put(0, 0, '#')
            grid.put(0, sz-1, '#')
            grid.put(sz-1, 0, '#')
            grid.put(sz-1, sz-1, '#')
        }
        return grid.count { it == '#' }
    }
}