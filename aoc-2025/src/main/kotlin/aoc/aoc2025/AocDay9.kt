package aoc.aoc2025

import aoc.AocDay
import aoc.util.Coord
import aoc.util.pairwise
import aoc.util.second
import aoc.util.x
import aoc.util.y
import java.awt.Rectangle
import kotlin.math.absoluteValue

class AocDay9: AocDay(9, 2025) {
    companion object { @JvmStatic fun main(args: Array<String>) { AocDay9().run() } }

    override val testinput = """
        7,1
        11,1
        11,7
        9,7
        9,5
        2,5
        2,3
        7,3
    """.trimIndent().lines()

    override fun calc1(input: List<String>): Long {
        val pairs: List<Coord> = input.map { it.split(",").let { it[0].toInt() to it[1].toInt() }}
        var largest = 0L
        pairs.pairwise().forEach { pp ->
            val p1 = pp.first()
            val p2 = pp.second()
            val rectangleSize = (1 + (p1.x - p2.x).absoluteValue).toLong() * (1 + (p1.y - p2.y).absoluteValue)
            if (rectangleSize > largest) {
                largest = rectangleSize
            }
        }
        return largest
    }

    override fun calc2(input: List<String>): Long {
        if (input.size < 100) return -1L
        val pairs: List<Coord> = input.map { it.split(",").let { it[0].toInt() to it[1].toInt() }}

        // largest rectangle must use either (94997, 50126) if on top, or (94997, 48641) if on bottom

        // find the largest rectangle on top
        var largest1 = 0L
        var largestRect1: Rectangle? = null
        val topleft = pairs.take(248).drop(148)
        val topright = pairs.take(96)
        for (c in topleft) {
            val xmax = topright.filter { it.y < c.y }.minOfOrNull { it.x }
            if (xmax != null) {
                val size = (94997 - c.x + 1).toLong() * (c.y - 50126 + 1).toLong()
                if (xmax >= 94997 && size > largest1) {
                    largest1 = size
                    largestRect1 = Rectangle(c.x, 50126, 94997 - c.x, c.y - 50126)
                }
            }
        }
        println("Largest on top: x=${largestRect1!!.minX.toInt()}..${largestRect1.maxX.toInt()}, y=${largestRect1.minY.toInt()}..${largestRect1.maxY.toInt()}, size $largest1")

        // find the largest rectangle on top
        var largest2 = 0L
        var largestRect2: Rectangle? = null
        val bottomright = pairs.drop(399)
        val bottomleft = pairs.drop(250).take(96)
        for (c in bottomleft) {
            val xmax = bottomright.filter { it.y > c.y }.minOfOrNull { it.x }
            if (xmax != null) {
                val size = (94997 - c.x + 1).toLong() * (48641 - c.y + 1).toLong()
                if (xmax >= 94997 && size > largest2) {
                    largest2 = size
                    largestRect2 = Rectangle(c.x, 48641, 94997 - c.x, 48641 - c.y)
                }
            }
        }
        println("Largest on bottom: x=${largestRect2!!.minX.toInt()}..${largestRect2.maxX.toInt()}, y=${largestRect2.minY.toInt()}..${largestRect2.maxY.toInt()}, size $largest2")

        // 2317461630 -- too large
        // 2317533724 -- too high
        return maxOf(largest1, largest2)
    }
}