import aoc.util.AocParser.Companion.parselines
import aoc.*
import aoc.getDayInput
import aoc.util.second

val testInput = """
47|53
97|13
97|61
97|47
75|29
61|13
75|53
29|13
97|29
53|29
61|53
97|53
61|29
47|13
75|47
97|75
47|61
75|61
47|29
75|13
53|13

75,47,61,53,29
97,61,53,29,13
75,29,13
75,97,47,61,53
61,13,29
97,13,75,29,47
""".parselines

class ParseRules(
    val prec: Map<Int, Set<Int>>,
    val manuals: List<List<Int>>
)

fun List<String>.parse(): ParseRules {
    val rules = filter { it.length == 5 }
    val manuals = filter { it.length > 5 }
    return ParseRules(
        rules.map { it.ints("|") }.groupBy { it.first() }
            .mapValues { it.value.map { it.second() }.toSet() },
        manuals.map { it.ints(",") }
    )
}

// part 1

fun List<String>.part1(): Int {
    val rules = parse()
    return rules.manuals.sumOf {
        if (it.checkManual(rules.prec))
            it[it.size / 2]
        else
            0
    }
}

fun List<Int>.checkManual(prec: Map<Int, Set<Int>>): Boolean {
    withIndex().forEach { (i, x) ->
        drop(i + 1).forEach { y ->
            if (prec[y]?.contains(x) == true)
                return false
        }
    }
    return true
}

// part 2

fun List<String>.part2(): Int {
    val rules = parse()
    val incorrect = rules.manuals
        .filter { !it.checkManual(rules.prec) }
    return incorrect
        .map { it.orderCorrectly(rules.prec) }
        .sumOf { it[it.size / 2] }
}

fun List<Int>.orderCorrectly(prec: Map<Int, Set<Int>>): List<Int> {
    if (size == 1) return this
    val whatComesAfter = flatMap { prec[it] ?: emptySet() }.toSet().intersect(this)
    val first = (this - whatComesAfter).first()
    return listOf(first) + (this - first).orderCorrectly(prec)
}

// calculate answers

val day = 5
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
