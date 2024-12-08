import aoc.util.AocParser.Companion.parselines
import aoc.*
import aoc.getDayInput
import aoc.util.*

val testInput = """
............
........0...
.....0......
.......0....
....0.......
......A.....
............
............
........A...
.........A..
............
............
""".parselines

// part 1

fun List<String>.part1(): Int {
    val min = Coord(0, 0)
    val max = Coord(get(0).length - 1, size - 1)
    val freqLocs = allIndices2().filter { at(it) != '.' }
        .groupBy { at(it) }
    val antinodeLocs = mutableSetOf<Coord>()
    freqLocs.forEach { (ch, locs) ->
        locs.pairwise().forEach { pair ->
            val p1 = pair.first()
            val p2 = pair.second()
            val an1 = p1 + 2 * (p2 - p1)
            val an2 = p2 + 2 * (p1 - p2)
            antinodeLocs += setOf(an1, an2)
        }
    }
    return antinodeLocs.filter {
        it.x in min.x..max.x && it.y in min.y..max.y
    }.size
}

// part 2

fun List<String>.part2(): Int {
    val min = Coord(0, 0)
    val max = Coord(get(0).length - 1, size - 1)
    val freqLocs = allIndices2().filter { at(it) != '.' }
        .groupBy { at(it) }
    val antinodeLocs = mutableSetOf<Coord>()
    freqLocs.forEach { (ch, locs) ->
        locs.pairwise().forEach { pair ->
            val p1 = pair.first()
            val p2 = pair.second()
            antinodeLocs += (-100..100).map { p1 + it * (p2 - p1) }
        }
    }
    return antinodeLocs.filter {
        it.x in min.x..max.x && it.y in min.y..max.y
    }.size
}

// calculate answers

val day = 8
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
