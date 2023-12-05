import aoc.AocParser.Companion.parselines
import aoc.*
import aoc.util.getDayInput

val testInput = """
seeds: 79 14 55 13

seed-to-soil map:
50 98 2
52 50 48

soil-to-fertilizer map:
0 15 37
37 52 2
39 0 15

fertilizer-to-water map:
49 53 8
0 11 42
42 0 7
57 7 4

water-to-light map:
88 18 7
18 25 70

light-to-temperature map:
45 77 23
81 45 19
68 64 13

temperature-to-humidity map:
0 69 1
1 0 69

humidity-to-location map:
60 56 37
56 93 4
""".parselines

val List<Long>.destStart
    get() = this[0]
val List<Long>.sourceStart
    get() = this[1]
val List<Long>.destRange
    get() = LongRange(this[0], this[0]+this[2]-1)
val List<Long>.sourceRange
    get() = LongRange(this[1], this[1]+this[2]-1)

fun intersect(r1: LongRange, r2: LongRange) =
    if (r1.last < r2.first || r2.last < r1.first)
        null
    else
        LongRange(maxOf(r1.first, r2.first), minOf(r1.last, r2.last))

/** Remove intersection of target from all provided ranges. */
fun List<LongRange>.remove(o: LongRange) = flatMap {
    val intersection = intersect(it, o)
    if (intersection == null)
        listOf(it)
    else {
        val r1 = LongRange(it.first, intersection.first-1)
        val r2 = LongRange(intersection.last+1, it.last)
        listOfNotNull(r1, r2).filter { !it.isEmpty() }
    }
}

class SeedData(
    val seeds: List<Long>,
    val seedRanges: List<LongRange>,
    val s2s: List<List<Long>>,
    val s2f: List<List<Long>>,
    val f2w: List<List<Long>>,
    val w2l: List<List<Long>>,
    val l2t: List<List<Long>>,
    val t2h: List<List<Long>>,
    val h2loc: List<List<Long>>
) {
    val seedsByRange
        get() = seedRanges.flatMap { it.toList() }
    /** Determine the mapping of the seed for the given list. */
    fun map(value: Long, map: List<List<Long>>): Long {
        val inRange = map.firstOrNull { value in it.sourceRange }
        return if (inRange == null)
            value
        else
            value + inRange.destStart - inRange.sourceStart
    }
    /** Determine the mapping of the seed range for the given list. */
    fun map(range: LongRange, map: List<List<Long>>): List<LongRange> {
        var unmapped = listOf(range)
        val mapped = map.flatMap {
            val rSource = it.sourceRange
            val delta = it.destStart - it.sourceStart
            val intersection = intersect(range, rSource)
            if (intersection == null)
                listOf()
            else {
                val r = LongRange(maxOf(range.first, rSource.first) + delta, minOf(range.last, rSource.last) + delta)
                unmapped = unmapped.remove(LongRange(r.first - delta, r.last - delta))
                listOf(r)
            }
        }
        return mapped + unmapped
    }
}

class Seed(val id: Long, val soil: Long, val fertilizer: Long, val water: Long, val light: Long, val temperature: Long, val humidity: Long, val loc: Long)

fun String.parse(): SeedData {
    val groups = split("\n\n".toRegex())
    val seeds = groups[0].substringAfter(" ").longs(" ")
    return SeedData(
        seeds,
        seeds.chunked(2).map { LongRange(it[0], it[0]+it[1]-1) },
        groups[1].substringAfter(":\n").lines().map { it.longs(" ") },
        groups[2].substringAfter(":\n").lines().map { it.longs(" ") },
        groups[3].substringAfter(":\n").lines().map { it.longs(" ") },
        groups[4].substringAfter(":\n").lines().map { it.longs(" ") },
        groups[5].substringAfter(":\n").lines().map { it.longs(" ") },
        groups[6].substringAfter(":\n").lines().map { it.longs(" ") },
        groups[7].substringAfter(":\n").lines().map { it.longs(" ") }
    )
}

// part 1

fun List<String>.part1(): Long {
    val data = joinToString("\n").parse()
    val seeds = data.seeds.map {
        with (data) {
            val soil = map(it, s2s)
            val fertilizer = map(soil, s2f)
            val water = map(fertilizer, f2w)
            val light = map(water, w2l)
            val temperature = map(light, l2t)
            val humidity = map(temperature, t2h)
            val loc = map(humidity, h2loc)
            Seed(it, soil, fertilizer, water, light, temperature, humidity, loc)
        }
    }
    return seeds.minOf { it.loc }
}

// part 2

fun List<String>.part2(): Long {
    val data = joinToString("\n").parse()
    val soil = data.seedRanges.flatMap { data.map(it, data.s2s) }
    val fertilizer = soil.flatMap { data.map(it, data.s2f) }
    val water = fertilizer.flatMap { data.map(it, data.f2w) }
    val light = water.flatMap { data.map(it, data.w2l) }
    val temperature = light.flatMap { data.map(it, data.l2t) }
    val humidity = temperature.flatMap { data.map(it, data.t2h) }
    val loc = humidity.flatMap { data.map(it, data.h2loc) }
    return loc.minOf { it.first }
}

// calculate answers

val day = 5
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
