import aoc.AocParser.Companion.parselines
import aoc.*
import aoc.util.getDayInput

val testInput = """
LR

AAA = (11B, XXX)
11B = (XXX, ZZZ)
ZZZ = (11B, XXX)
22A = (22B, XXX)
22B = (22C, 22C)
22C = (22Z, 22Z)
22Z = (22B, 22B)
XXX = (XXX, XXX)
""".parselines

class LeftRight(val left: String, val right: String)

fun String.parse() = chunk(0) to LeftRight(
    chunk(2).substring(1..3),
    chunk(3).substring(0..2))

// part 1

fun List<String>.part1(): Int {
    val instruct = get(0)
    val rules = drop(2).associate { it.parse() }
    return stepsTo(start = "AAA", end = { it == "ZZZ" }, instruct, rules)
}

fun stepsTo(start: String, end: (String) -> Boolean, instruct: String, rules: Map<String, LeftRight>): Int {
    var steps = 0
    var cur = start
    while (!end(cur)) {
        val r = instruct[steps % instruct.length]
        when (r) {
            'L' -> cur = rules[cur]!!.left
            'R' -> cur = rules[cur]!!.right
        }
        steps++
    }
    return steps
}

// part 2

fun List<String>.part2(): Long {
    val instruct = get(0)
    val rules = drop(2).associate { it.parse() }
    val starts = rules.keys.filter { it.last() == 'A' }
    val steps = starts.map { stepsTo(it, { it.last() == 'Z' }, instruct, rules) }
    println(steps)
    return steps.leastCommonMultiple()
}

fun List<Int>.leastCommonMultiple(): Long {
    val list = toMutableList()
    var lcm = 1L
    var divisor = 2
    while (list.any { it > 1 }) {
        if (list.any { it % divisor == 0 }) {
            lcm *= divisor
            list.forEachIndexed { index, i ->
                if (i % divisor == 0)
                    list[index] = i / divisor
            }
        } else {
            divisor++
        }
    }
    return lcm
}

// calculate answers

val day = 8
val input = getDayInput(day, 2023)
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
