import aoc.AocParser.Companion.parselines
import aoc.*
import aoc.util.getDayInput
import kotlin.math.pow

val testInput = """
32T3K 765
T55J5 684
KK677 28
KTJJT 220
QQQJA 483
""".parselines

class Hand(val str: String) : Comparable<Hand> {
    override fun toString() = str

    val counts = str.toList().groupBy { it }.mapValues { it.value.size }
    val fiveOf = counts.values.any { it == 5 }
    val fourOf = counts.values.any { it == 4 }
    val threeOf = counts.values.any { it == 3 }
    val twoOf = counts.values.any { it == 2 }
    val twoPair = counts.values.count { it == 2 } == 2
    val fullHouse = threeOf && twoOf

    fun score() = when {
        fiveOf -> 7
        fourOf -> 6
        fullHouse -> 5
        threeOf -> 4
        twoPair -> 3
        twoOf -> 2
        else -> 1
    }

    val handRank = str.toList().mapIndexed { i, c -> c.rank() * 15.0.pow(4-i) }.sum()
    val handRank2 = counts.entries.groupBy { it.value }
        .entries
        .sortedByDescending { it.key }
        .flatMap { it.value.sortedByDescending { it.key.rank() } }
        .mapIndexed { i, en -> en.key.rank() * 15.0.pow(4-i) }
        .sum()

    private fun Char.rank() = when (this) {
        'A' -> 14
        'K' -> 13
        'Q' -> 12
        'J' -> 11
        'T' -> 10
        else -> toString().toInt()
    }

    override fun compareTo(other: Hand): Int {
        val score = score()
        val otherScore = other.score()
        return when {
            score > otherScore -> 1
            score < otherScore -> -1
            else -> handRank.compareTo(other.handRank)
        }
    }
}

class HandJoker(val str: String) : Comparable<HandJoker> {
    override fun toString() = str

    val counts = str.toList().groupBy { it }.mapValues { it.value.size }
        .filterKeys { it != 'J' }
    val jokers = str.count { it == 'J' }

    val maxCount = if (jokers == 5) 5 else counts.values.max() + jokers
    val fiveOf = maxCount == 5
    val fourOf = maxCount == 4
    val threeOf = maxCount == 3
    val twoOf = maxCount == 2
    val twoPair = counts.values.count { it == 2 } == 2
            || (counts.values.count { it == 2 } == 1 && jokers == 1)
    val fullHouse = maxCount == 3 && counts.size == 2

    fun score() = when {
        fiveOf -> 7
        fourOf -> 6
        fullHouse -> 5
        threeOf -> 4
        twoPair -> 3
        twoOf -> 2
        else -> 1
    }

    val handRank = str.toList().mapIndexed { i, c -> c.rank() * 15.0.pow(4-i) }.sum()

    private fun Char.rank() = when (this) {
        'A' -> 14
        'K' -> 13
        'Q' -> 12
        'J' -> 0
        'T' -> 10
        else -> toString().toInt()
    }

    override fun compareTo(other: HandJoker): Int {
        val score = score()
        val otherScore = other.score()
        return when {
            score > otherScore -> 1
            score < otherScore -> -1
            else -> handRank.compareTo(other.handRank)
        }
    }
}

// part 1

fun List<String>.part1(): Int {
    val bids = map { Hand(it.chunk(0)) to it.chunkint(1) }
    val sortedBids = bids.sortedBy { it.first }
    val wins = (1..sortedBids.size).map {
        sortedBids[it-1].second * it
    }
    return wins.sum()
}

// part 2

fun List<String>.part2(): Int {
    val bids = map { HandJoker(it.chunk(0)) to it.chunkint(1) }
    val sortedBids = bids.sortedBy { it.first }
    val wins = (1..sortedBids.size).map {
        sortedBids[it-1].second * it
    }
    return wins.sum()
}

// calculate answers

val day = 7
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
