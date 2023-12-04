import aoc.AocParser.Companion.parselines
import aoc.*
import aoc.util.getDayInput
import kotlin.math.pow

val testInput = """
Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53
Card 2: 13 32 20 16 61 | 61 30 68 82 17 32 24 19
Card 3:  1 21 53 59 44 | 69 82 63 72 16 21 14  1
Card 4: 41 92 73 84 69 | 59 84 76 51 58  5 54 83
Card 5: 87 83 26 28 32 | 88 30 70 12 93 22 82 36
Card 6: 31 18 13 56 72 | 74 77 10 23 35 67 36 11
""".parselines

class CardInfo(val id: Int, val winning: List<Int>, val picked: List<Int>) {
    fun matches() = winning.intersect(picked).size
    fun points() = 2.0.pow(matches() - 1).toInt()
}

fun String.parse() = split(":").let {
    val spl2 = it[1].split("|")
    CardInfo(it[0].substringAfter("Card").trim().toInt(),
        spl2[0].trim().split("\\s+".toRegex()).map { it.toInt() },
        spl2[1].trim().split("\\s+".toRegex()).map { it.trim().toInt() })
}

// part 1

fun List<String>.part1(): Int = map { it.parse() }.sumOf { it.points() }

// part 2

fun List<String>.part2(): Int {
    val cards = map { it.parse() }.associateBy { it.id }
    val counts = (1..cards.size).associateWith { 1 }.toMutableMap()
    var sum = 0
    while (counts.isNotEmpty()) {
        val e1 = counts.entries.first()
        val card = cards[e1.key]!!
        val count = e1.value
        val matches = card.matches()
        (1..matches).forEach {
            counts[e1.key + it] = counts[e1.key + it]!! + count
        }
        sum += count
        counts.remove(e1.key)
    }
    return sum
}

// calculate answers

val day = 4
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
