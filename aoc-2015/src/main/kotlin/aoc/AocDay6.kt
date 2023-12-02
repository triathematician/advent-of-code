package aoc

import aoc.util.*

class AocDay6: AocDay(6) {
    companion object { @JvmStatic fun main(args: Array<String>) { AocDay6().run() } }

    override val testinput = """
        turn on 0,0 through 999,999
        toggle 0,0 through 999,0
        turn off 499,499 through 500,500
    """.trimIndent().lines()

    enum class ToggleOp { TOGGLE, ON, OFF }
    data class Toggle(val op: ToggleOp, val corner1: Loc, val corner2: Loc) {
        val pos = (corner1.x..corner2.x).flatMap { x ->
            (corner1.y..corner2.y).map { Loc(x, it) }
        }

        fun applyTo(grid: IntGrid) {
            pos.forEach {
                when (op) {
                    ToggleOp.TOGGLE -> grid[it] = 1 - grid[it]
                    ToggleOp.ON -> grid[it] = 1
                    ToggleOp.OFF -> grid[it] = 0
                }
            }
        }

        fun applyTo2(grid: IntGrid) {
            pos.forEach {
                when (op) {
                    ToggleOp.TOGGLE -> grid[it] = grid[it] + 2
                    ToggleOp.ON -> grid[it] = grid[it] + 1
                    ToggleOp.OFF -> grid[it] = (grid[it] - 1).coerceAtLeast(0)
                }
            }
        }
    }

    fun parseToggleInstruction(it: String) = it.split(',', ' ').let {
        val ints = it.justTheInts()
        val op = it.lookup("toggle" to ToggleOp.TOGGLE, "on" to ToggleOp.ON, "off" to ToggleOp.OFF)
        Toggle(op, Loc(ints[0], ints[1]), Loc(ints[2], ints[3]))
    }

    override fun calc1(input: List<String>) = input.map { parseToggleInstruction(it) }.let {
        val grid = intgrid(1000, 1000)
        it.forEach { it.applyTo(grid) }
        grid.sumOf { it.sumOf { it }}
    }
    override fun calc2(input: List<String>) = input.map { parseToggleInstruction(it) }.let {
        val grid = intgrid(1000, 1000)
        it.forEach { it.applyTo2(grid) }
        grid.sumOf { it.sumOf { it }}
    }
}