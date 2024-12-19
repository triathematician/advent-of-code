import aoc.util.AocParser.Companion.parselines
import aoc.*
import aoc.getDayInput
import aoc.util.*

val testInput = """
r, wr, b, g, bwu, rb, gb, br

brwrr
bggr
gbbr
rrbgbr
ubwu
bwurrg
brgr
bbrgwb
""".parselines

// part 1

class Challenge(input: List<String>) {
    val patterns = input[0].split(", ")
    val designs = input.drop(2)

    val cache = mutableMapOf<String, Boolean>()
    val cacheWays = mutableMapOf<String, Long>()

    fun feasibleDesign(d: String): Boolean {
        if (d in cache) return cache[d]!!
        val result = patterns.any {
            d == it || (d.startsWith(it) && feasibleDesign(d.drop(it.length)))
        }
        cache[d] = result
        return result
    }

    fun feasibleDesignWays(d: String): Long {
        if (d in cacheWays) return cacheWays[d]!!
        val result = patterns.sumOf {
            when {
                d == it -> 1L
                d.startsWith(it) -> feasibleDesignWays(d.drop(it.length))
                else -> 0L
            }
        }
        cacheWays[d] = result
        return result
    }
}

fun List<String>.part1() =
    Challenge(this).let { ch ->
        ch.designs.count { ch.feasibleDesign(it) }
    }

// part 2

fun List<String>.part2() =
    Challenge(this).let { ch ->
        ch.designs.sumOf { ch.feasibleDesignWays(it) }
    }

// calculate answers

val day = 19
val input = getDayInput(day, 2024)
val testResult = testInput.part1()
val testResult2 = testInput.part2()
val answer1 = input.part1().also { it.print }
val answer2 = input.part2().also { it.print }

// print results

AocRunner(day,
    test = { "$testResult, $testResult2" },
    part1 = { answer1 },
    part2 = { answer2 }
).run()
