package aoc.aoc2025

import aoc.AocDay
import aoc.util.ANSI_DARK_GREEN
import aoc.util.ANSI_RESET
import aoc.util.AstarSearch
import aoc.util.Pathfinder
import aoc.util.eachCount

class AocDay10: AocDay(10, 2025) {
    companion object { @JvmStatic fun main(args: Array<String>) { AocDay10().run() } }

    override val testinput = """
        [.##.] (3) (1,3) (2) (2,3) (0,2) (0,1) {3,5,4,7}
        [...#.] (0,2,3,4) (2,3) (0,4) (0,1,2) (1,2,3,4) {7,5,12,7,2}
        [.###.#] (0,1,2,3,4) (0,3,4) (0,1,2,4,5) (1,2) {10,11,11,5,10,5}
    """.trimIndent().lines()

    override fun calc1(input: List<String>): Int {
        val configs = input.map { it.toLightConfig() }
        return configs.sumOf { it.fewestPresses() }
    }

    fun String.toLightConfig(): LightConfig {
        val goal = substringAfter("[").substringBefore("]").map { it == '#' }.toBooleanArray()
        val buttons = substringAfter("]").substringBefore("{").trim().split(" ")
            .map { it.substringAfter("(").substringBefore(")").split(",").map { it.toInt() }.toIntArray() }
        val joltage = substringAfter("{").substringBefore("}").split(",").map { it.toInt() }.toIntArray()
        return LightConfig(goal, buttons, joltage)
    }

    fun LightConfig.fewestPresses(): Int {
        val lightState = goal.map { false }
        val goal = goal.toList()
        val fewest = Pathfinder.shortestPathTo(lightState, { it == goal }) { state ->
            buttons.map { button ->
                val newState = state.toMutableList()
                button.forEach { idx -> newState[idx] = !newState[idx] }
                newState
            }
        }
        return fewest.size - 1
    }

    override fun calc2(input: List<String>): Int {
        val configs = input.map { it.toLightConfig() }.sortedBy {
            it.buttons.size * it.joltage.size * it.joltage.max()
        }
        return configs.sumOf {
            val t0 = System.currentTimeMillis()
            print("[${it.goal.map { if (it) '#' else '.' }.joinToString("")}] ")
            print("${it.joltage.max()}x${it.joltage.size}x${it.buttons.size} ")
            val n = it.fewestPresses6()
            println("$ANSI_DARK_GREEN($n presses in ${(System.currentTimeMillis() - t0)/1000}s)$ANSI_RESET")
            n
        }
    }

    fun IntArray.press(indices: IntArray): IntArray {
        val newState = this.copyOf()
        indices.forEach { idx ->
            newState[idx] = newState[idx] + 1
        }
        return newState
    }

    fun LightConfig.fewestPresses2(): Int {
        val lightState = goal.map { 0 }.toIntArray()
        val fewest = Pathfinder.shortestPathTo(lightState, { it.contentEquals(joltage) }) { state ->
            buttons.map { state.press(it) }
                .filter { it.withIndex().none { (i, v) -> v > joltage[i] }
            }
        }
        return fewest.size - 1
    }

    fun LightConfig.fewestPresses3(): Int {
        val lightState = goal.map { 0 }.toIntArray()
        val fewest = AstarSearch.toFinish(lightState, joltage, { state ->
            buttons.map { state.press(it) }
                .filter { it.withIndex().none { (i, v) -> v > joltage[i] }
            }.toSet()
        }, heuristicToFinish = { state ->
            // need at least one button press to fix the worst difference
            joltage.indices.maxOf { joltage[it] - state[it] }
        })
        return fewest.minimizeCost().first.size - 1
    }

    fun LightConfig.fewestPresses4(): Int {
        if (buttons.size == 1) {
            val presses = joltage[buttons[0].first()]
            val valid = joltage.indices.all { i ->
                if (i in buttons[0]) joltage[i] == presses else joltage[i] == 0
            }
            return if (valid) presses else 9999999
        }
        val button1 = buttons.first()
        val maxFirst = button1.minOf { joltage[it] } // maximum # of times the first button can be pressed
        val res = (0..maxFirst).minOf { n ->
            val remainder = joltage.indices.map { joltage[it] - (if (it in button1) n else 0) }.toIntArray()
            when {
                remainder.all { it == 0 } -> n
                buttons.size == 1 -> 9999999
                else -> LightConfig(goal, buttons.drop(1), remainder).fewestPresses4() + n
            }
        }
        return res
    }

    fun LightConfig.fewestPresses5(): Int {
        // start with the least common index in buttons
        val index = leastCommonIndex
        val target = joltage[index]
        val buttonsForTarget = buttons.filter { index in it }

        val partitions = target.partitionInto(buttonsForTarget.size)
        if (partitions.isEmpty()) {
            println("TARGET: $target, INDEX: $index, BUTTONS_FOR_TARGET: $buttonsForTarget")
        }
        val res = partitions.minOf { partition ->
            val remainder = joltage.copyOf()
            buttonsForTarget.forEachIndexed { i, button ->
                val pressCount = partition[i]
                button.forEach { idx ->
                    remainder[idx] = remainder[idx] - pressCount
                }
            }
            when {
                remainder.any { it < 0 } -> 9999999
                remainder.all { it == 0 } -> partition.sum()
                buttons.size == buttonsForTarget.size -> 9999999
                else -> LightConfig(goal, buttons - buttonsForTarget.toSet(), remainder).fewestPresses5() + partition.sum()
            }
        }
        return res
    }

    fun LightConfig.fewestPresses6(): Int {
        // start with the least common index in buttons
        val index = joltage.indices.minBy { i ->
            joltage[i] * buttons.filter { i in it }.size
        }
        val target = joltage[index]
        val buttonsForTarget = buttons.filter { index in it }

        val partitions = target.partitionInto(buttonsForTarget.size)
        val res = partitions.minOf { partition ->
            val remainder = joltage.copyOf()
            buttonsForTarget.forEachIndexed { i, button ->
                val pressCount = partition[i]
                button.forEach { idx ->
                    remainder[idx] = remainder[idx] - pressCount
                }
            }
            when {
                remainder.any { it < 0 } -> 9999999
                remainder.all { it == 0 } -> partition.sum()
                buttons.size == buttonsForTarget.size -> 9999999
                else -> LightConfig(goal, buttons - buttonsForTarget.toSet(), remainder).fewestPresses5() + partition.sum()
            }
        }
        return res
    }

    /** Partition integer into n parts, producing a list of size n. */
    fun Int.partitionInto(n: Int): List<IntArray> {
        return partitionCache.getOrPut(this to n) {
            if (n == 1) return listOf(intArrayOf(this))
            val parts = mutableListOf<IntArray>()
            for (i in 0..this) {
                val subparts = (this - i).partitionInto(n - 1)
                subparts.forEach {
                    val arr = IntArray(it.size + 1)
                    arr[0] = i
                    System.arraycopy(it, 0, arr, 1, it.size)
                    parts.add(arr)
                }
            }
            parts
        }
    }

    val partitionCache = mutableMapOf<Pair<Int, Int>, List<IntArray>>()
}

class LightConfig(
    val goal: BooleanArray,
    val buttons: List<IntArray>,
    val joltage: IntArray
) {
    val leastCommonIndex = buttons.flatMap { it.toList() }.eachCount().minByOrNull { it.value }!!.key
}