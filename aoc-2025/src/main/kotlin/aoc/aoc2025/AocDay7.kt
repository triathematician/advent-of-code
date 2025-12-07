package aoc.aoc2025

import aoc.AocDay
import aoc.util.CharGrid
import aoc.util.x

class AocDay7: AocDay(7, 2025) {
    companion object { @JvmStatic fun main(args: Array<String>) { AocDay7().run() } }

    override val testinput = """
        .......S.......
        ...............
        .......^.......
        ...............
        ......^.^......
        ...............
        .....^.^.^.....
        ...............
        ....^.^...^....
        ...............
        ...^.^...^.^...
        ...............
        ..^...^.....^..
        ...............
        .^.^.^.^.^...^.
        ...............
    """.trimIndent().lines()

    override fun calc1(input: List<String>): Int {
        val grid = CharGrid(input)
        val pos = grid.find('S')
        val beamLocs = mutableMapOf<Int, Set<Int>>()
        beamLocs[0] = setOf(pos.x)
        var splitCount = 0
        (1..grid.ysize).forEach { row ->
            val lastPos = beamLocs[row-1]!!
            val splitters = input[row].indices.filter { input[row][it] == '^' }.toSet()
            val nextPos = mutableSetOf<Int>()
            lastPos.forEach { x ->
                if (x in splitters) {
                    nextPos += setOf(x-1, x+1)
                    splitCount++
                } else {
                    nextPos += x
                }
            }
            beamLocs[row] = nextPos
        }
        return splitCount
    }
    override fun calc2(input: List<String>): Long {
        val grid = CharGrid(input)
        val pos = grid.find('S')
        val beamLocs = mutableMapOf<Int, Map<Int, Long>>()
        beamLocs[0] = mapOf(pos.x to 1)
        (1..grid.ysize).forEach { row ->
            val lastPos = beamLocs[row-1]!!
            val splitters = input[row].indices.filter { input[row][it] == '^' }.toSet()
            val nextPos = mutableMapOf<Int, Long>()
            lastPos.forEach { (x, num) ->
                if (x in splitters) {
                    nextPos[x-1] = nextPos.getOrPut(x-1) { 0L } + num
                    nextPos[x+1] = nextPos.getOrPut(x+1) { 0L } + num
                } else {
                    nextPos[x] = nextPos.getOrPut(x) { 0L } + num
                }
            }
            beamLocs[row] = nextPos
        }
        return beamLocs.values.last().values.sum()
    }
}