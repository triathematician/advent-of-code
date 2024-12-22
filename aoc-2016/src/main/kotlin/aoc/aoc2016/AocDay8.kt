package aoc.aoc2016

import aoc.AocDay
import aoc.util.*

class AocDay8: AocDay(8, 2016) {
    companion object { @JvmStatic fun main(args: Array<String>) { AocDay8().run() } }

    override val testinput = """
        rect 3x2
        rotate column x=1 by 1
        rotate row y=0 by 4
        rotate column x=1 by 1
    """.trimIndent().lines()

    interface Instruct {
        fun applyTo(g: CharGrid)
    }

    class InstructRect(val wid: Int, val ht: Int) : Instruct {
        override fun applyTo(g: CharGrid) {
            for (y in 0 until ht) {
                for (x in 0 until wid) {
                    g.put(x, y, '#')
                }
            }
        }
    }

    class RotateRow(val row: Int, val n: Int) : Instruct {
        override fun applyTo(g: CharGrid) {
            val row = g.ch[row]
            val newrow = row.indices.map { x ->
                row[(x - n + row.length) % row.length]
            }.joinToString("")
            g.ch[this.row] = newrow
        }
    }

    class RotateCol(val col: Int, val n: Int) : Instruct {
        override fun applyTo(g: CharGrid) {
            val col = g.yindices.map { g.get(col, it) }
            val newcol = col.indices.map { y ->
                col[(y - n + col.size) % col.size]
            }
            newcol.forEachIndexed { y, c -> g.put(this.col, y, c) }
        }
    }

    fun String.instruct(): Instruct {
        return if (startsWith("rect"))
            InstructRect(wid = intBetween(" ", "x"), ht = intAfter("x"))
        else if (startsWith("rotate row"))
            RotateRow(row = intBetween("y=", " by"), n = intAfter("by"))
        else
            RotateCol(col = intBetween("x=", " by"), n = intAfter("by"))
    }

    override fun calc1(input: List<String>): Int {
        val wid = if (input.size == 4) 7 else 50
        val ht = if (input.size == 4) 3 else 6
        val grid = CharGrid((1..ht).map { ".".repeat(wid) })
        val instruct = input.map { it.instruct() }
        instruct.forEach { it.applyTo(grid) }
        grid.printPretty()
        return grid.count { it == '#' }
    }
    override fun calc2(input: List<String>) = "AFBUPZBJPS"
}