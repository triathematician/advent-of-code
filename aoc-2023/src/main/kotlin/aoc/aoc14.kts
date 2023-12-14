import aoc.AocParser.Companion.parselines
import aoc.*
import aoc.util.getDayInput

val testInput = """
O....#....
O.OO#....#
.....##...
OO.#O....O
.O.....O#.
O.#..O.#.#
..O..#O..O
.......O..
#....###..
#OO..#....
""".parselines

class Rocks(val initLines: List<String>) {
    var curLines: MutableList<MutableList<Char>> = initLines.map { it.toMutableList() }.toMutableList()
    val rows
        get() = curLines.indices
    val cols
        get() = curLines[0].indices
    fun roll1() {
        initLines.indices.drop(1).forEach {
            curLines[it].forEachIndexed { r, ch ->
                if (ch == 'O' && curLines[it-1][r] == '.') {
                    curLines[it-1][r] = 'O'
                    curLines[it][r] = '.'
                }
            }
        }
    }
    fun nRoll(): Rocks {
        repeat(initLines.size) { roll1() }
        return this
    }
    fun score(): Int {
        var score = 0
        curLines.forEachIndexed { r, line ->
            line.forEachIndexed { c, ch ->
                if (ch == 'O') score += curLines.size - r
            }
        }
        return score
    }
    /** Rotate so that the left column is now the first row. */
    fun rotate() {
        val newLines = mutableListOf<MutableList<Char>>()
        cols.forEach { c ->
            newLines += rows.reversed().map { curLines[it][c] }.toMutableList()
        }
        curLines = newLines
    }
    fun print() {
        curLines.forEach { println(it.joinToString("")) }
    }

    fun str() = curLines.joinToString("") { it.joinToString("") }
}

// part 1

fun List<String>.part1(): Int {
    return Rocks(this).nRoll().score()
}

// part 2

fun List<String>.part2(): Int {
    val cycles = 1000000000
    var cycleSize = 7
    var rocks = Rocks(this)
    val scores = mutableListOf<Int>()
    (1..cycles).forEach {
        if (it % 1000 == 0) print(".")
        repeat(4) {
            rocks = rocks.nRoll()
            rocks.rotate()
        }
        scores += rocks.score()

        // look for a cycle after a number of runs
        if (it % 1000 == 0) {
            (7..1000).forEach { cs ->
                val last7 = scores.takeLast(cs)
                val previous7 = scores.takeLast(2*cs).dropLast(cs)
                if (last7 == previous7) {
                    cycleSize = cs
                    println(".. found a cycle of $cycleSize after $it iterations")
                    println("      Cycle: $last7")
                    println("      Iterations remaining: ${cycles - it}")
                    val scoreEnd = last7[(cycles - 1 - it) % cs]
                    println("      Score at end: $scoreEnd")
                    return scoreEnd
                }
            }
            println(".. unable to find a cycle after $it iterations")
        }
    }
    println()
    return rocks.score()
}

// calculate answers

val day = 14
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
