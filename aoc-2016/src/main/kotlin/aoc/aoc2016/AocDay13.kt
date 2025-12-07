package aoc.aoc2016

import aoc.AocDay
import aoc.util.Coord
import aoc.util.Pathfinder
import aoc.util.neighbors4
import aoc.util.x
import aoc.util.y

class AocDay13: AocDay(13, 2016) {
    companion object { @JvmStatic fun main(args: Array<String>) { AocDay13().run() } }

    override val testinput = """
        10
    """.trimIndent().lines()

    fun open(c: Coord, fav: Int): Boolean {
        val n = c.x*c.x + 3*c.x + 2*c.x*c.y + c.y + c.y*c.y + fav
        val binary = Integer.toBinaryString(n)
        val ones = binary.count { it == '1' }
        return ones % 2 == 0
    }

    private fun printBoard(fav: Int) {
        (0..6).forEach { y ->
            (0..9).forEach { x ->
                if (open(x to y, fav))
                    print(".")
                else
                    print("#")
            }
            println()
        }
    }

    override fun calc1(input: List<String>): Int {
        val fav = input[0].toInt()
        printBoard(fav)
        val pathfinder = Pathfinder(Coord(1, 1), { x ->
            x.neighbors4().filter { it.x >= 0 && it.y >= 0 && open(it, fav) }
        })
        val target = if (fav == 10) Coord(7, 4) else Coord(31, 39)
        return pathfinder.findWayTo(target).size - 1
    }

    override fun calc2(input: List<String>): Int {
        val fav = input[0].toInt()
        val pathfinder = Pathfinder(Coord(1, 1), { x ->
            x.neighbors4().filter { it.x >= 0 && it.y >= 0 && open(it, fav) }
        })
        return pathfinder.visitedInStepCount(50).size
    }
}