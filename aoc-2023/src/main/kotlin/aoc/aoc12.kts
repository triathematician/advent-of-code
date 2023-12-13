import aoc.AocParser.Companion.parselines
import aoc.AocRunner
import aoc.chunk
import aoc.print
import aoc.util.getDayInput

val testInput = """
???.### 1,1,3
.??..??...?##. 1,1,3
?#?#?#?#?#?#?#? 1,3,1,6
????.#...#... 4,1,1
????.######..#####. 1,6,5
?###???????? 3,2,1
""".parselines

class Springs(_line: String, val nums: SpringLengths) {
    val max = nums.maxOrNull()!!
    val line = _line
        // small optimization - require . on either side of max run of #s
        .replace("?" + "#".repeat(max), "." + "#".repeat(max))
        .replace("#".repeat(max) + "?", "#".repeat(max) + ".")
        // small optimization - remove redundant .'s
        .replace("[.]+".toRegex(), ".")

    var print = false

    /**
     * Track the number of matches for a subset of springs,
     * where the first spring in the subset starts at the given column,
     * and the remainder have valid locations after that.
     */
    val matchTable = (0..nums.size).map { line.map { -1L }.toMutableList() }

    /** Easy getter for the table. */
    fun matchesOf(springIndex: Int, mustBeAt: Int) = matchTable[springIndex][mustBeAt]
    /** Updates the match table. */
    fun updateMatchTable(springIndex: Int, mustBeAt: Int, count: Long) = count.also {
        if (print && count > 0)
            println("There are $count arrangements with Spring${springIndex} occurring at $mustBeAt")
        matchTable[springIndex][mustBeAt] = count
    }

    /** Global number of arrangements. */
    fun arrangements(): Long {
        if (print) println("Calculating arrangements for $line")
        val firstHash = line.indexOf('#').let { if (it < 0) line.length else it }
        val res = (0..minOf(firstHash, maxLocsPerSpring[0])).sumOf {
            calcMatchesOf(springIndex = 0, mustBeAt = it)
        }
        if (print)
            println("There are $res arrangements for $line")
        return res
    }

    fun calcMatchesOf(springIndex: Int, mustBeAt: Int): Long {
        // use cached value if possible
        if (matchesOf(springIndex, mustBeAt) >= 0)
            return matchesOf(springIndex, mustBeAt)
        else if (!validSpringLocation(mustBeAt, nums[springIndex]))
            return updateMatchTable(springIndex, mustBeAt, 0)
        else if (springIndex == nums.size - 1) {
            // if we're looking at the last spring, validity depends on whether or not there are more #s after
            val remainingHashes = line.substring(mustBeAt+nums[springIndex]).count { it == '#' }
            return updateMatchTable(springIndex, mustBeAt, if (remainingHashes == 0) 1 else 0)
        }

        // walk through all valid locations of the next spring
        // minimum must occur after the current spring
        val minNextLoc = mustBeAt + nums[springIndex] + 1
        // maximum must be no further than the max loc, but can't skip any #'s
        val nextHash = line.indexOf('#', minNextLoc).let { if (it < 0) line.length else it }
        val maxNextLoc = minOf(nextHash, maxLocsPerSpring[springIndex+1])
        val possibleLocationsOfNext = minNextLoc..maxNextLoc
        val count = possibleLocationsOfNext.sumOf { loc ->
            calcMatchesOf(springIndex+1, loc)
        }
        return updateMatchTable(springIndex, mustBeAt, count)
    }

    val maxLocsPerSpring: SpringLocs

    init {
        val maxes = mutableListOf<Int>()
        var i = line.length
        nums.reversed().forEach {
            maxes += i - it
            i -= it + 1
        }
        maxLocsPerSpring = maxes.reversed()
    }

    /** Test if a spring could exist at the given target location. */
    fun validSpringLocation(loc: Int, num: Int) =
        loc + num <= line.length &&
                '.' !in line.substring(loc, loc + num) &&
                line.getOrNull(loc-1) != '#' &&
                line.getOrNull(loc+num) != '#'
}

typealias SpringLocs = List<Int>
typealias SpringLengths = List<Int>

fun String.parse() = Springs(chunk(0), chunk(1).split(",").map { it.toInt() })

// part 1

fun List<String>.part1(): Long = sumOf {
    print(".")
    it.parse().arrangements()
}.also { println() }

// part 2

fun List<String>.part2(): Long = withIndex().sumOf {
    val mod = (size / 100).coerceAtLeast(1)
    print(if (it.index % mod == 0) "x" else ".")
    val str = it.value.chunk(0)
    val nums = it.value.chunk(1).split(",").map { it.toInt() }
    val unfolded = ("$str?").repeat(4)+str
    val unfoldedNums = (1..5).flatMap { nums }
    val spr = Springs(unfolded, unfoldedNums)
    spr.print = false
    spr.arrangements()
}.also { println() }

// calculate answers

val day = 12
val input = getDayInput(day, 2023)
val testResult = testInput.part1().also { it.print }
val answer1 = input.part1().also { it.print }
val testResult2 = testInput.part2().also { it.print }
val answer2 = input.part2().also { it.print }

// print results

AocRunner(day,
    test = { "$testResult, $testResult2" },
    part1 = { answer1 },
    part2 = { answer2 }
).run()
