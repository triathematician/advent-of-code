import aoc.util.AocParser.Companion.parselines
import aoc.*
import aoc.getDayInput
import aoc.util.*

val testInput = """
#####
.####
.####
.####
.#.#.
.#...
.....

#####
##.##
.#.##
...##
...#.
...#.
.....

.....
#....
#....
#...#
#.#.#
#.###
#####

.....
.....
#.#..
###..
###.#
###.#
#####

.....
.....
.....
#....
#.#..
#.#.#
#####
""".parselines

fun List<String>.parse() = splitOnEmptyLines().map {
    if (it[0] == "#####") {
        // lock
        Lock((0..4).map { col -> it.indices.last { row -> it[row][col] == '#' } })
    } else if (it[0] == ".....") {
        // key
        Key((0..4).map { col -> 6 - it.indices.first { row -> it[row][col] == '#' } })
    } else {
        error("Unknown item type")
    }
}

interface Item
class Lock(val cols: List<Int>) : Item {
    fun fits(k: Key) : Boolean {
        return cols.zip(k.cols).all { (l, k) -> l + k <= 5 }
    }
}
class Key(val cols: List<Int>) : Item

// part 1

fun List<String>.part1(): Int {
    val items = parse()
    val locks = items.filterIsInstance<Lock>()
    val keys = items.filterIsInstance<Key>()
    return locks.sumOf { l -> keys.count { l.fits(it) }}
}

// part 2

fun List<String>.part2() = 0

// calculate answers

val day = 25
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
