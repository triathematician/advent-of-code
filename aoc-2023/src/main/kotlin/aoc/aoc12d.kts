import aoc.AocParser.Companion.parselines
import aoc.*
import aoc.util.getDayInput
import kotlin.math.pow

val testInput = """
???.### 1,1,3
.??..??...?##. 1,1,3
?#?#?#?#?#?#?#? 1,3,1,6
????.#...#... 4,1,1
????.######..#####. 1,6,5
?###???????? 3,2,1
""".parselines

class Springs(_line: String, val nums: List<Int>) {
    val max = nums.maxOrNull()!!
    val oline = _line
    val line = ".$_line"
        .replace("?" + "#".repeat(max), "." + "#".repeat(max))
        .replace("#".repeat(max) + "?", "#".repeat(max) + ".")
        .replace("[.]+".toRegex(), ".")

    // create five copies of line joined by "?"
    fun unfold1() = Springs(
        (1..5).joinToString("?") { line.drop(1) },
        (1..5).flatMap { nums }
    )

    fun unfoldNums() = (1..5).flatMap { nums }

    // create unfolding options with all possible joiners
    fun unfoldOptions() = listOf(
        "....",
        "...#", "..#.", ".#..", "#...",
        "..##", ".#.#", "#..#", ".##.", "#.#.", "##..",
        ".###", "#.##", "##.#", "###.",
        "####"
    ).map {
        val newLine = oline + it[0] + oline + it[1] + oline + it[2] + oline + it[3] + oline
        Springs(newLine, unfoldNums())
    }

    fun arrangements(print: Boolean): Long {
        val res2 = arrangementsCached(line, nums)
        if (print) {
            println("$line $nums res2 $res2")
        }
        return res2
    }
}

//region CACHE FOR RESULTS

val cache = mutableMapOf<String, Long>()

fun arrangementsCached(str: String, nums: List<Int>): Long {
    if (str.length > 30)
        return arrangements(str, nums)

    val case = "$str ${nums.joinToString(",")}"
    if (case in cache)
        return cache[case]!!
    val res = arrangements(str, nums)
    cache[case] = res
    return res
}

//endregion

fun arrangements(str: String, nums: List<Int>): Long {
    if (nums.isEmpty())
        return if ('#' in str) 0 else 1
    val needToFind = nums.sum()
    val minLength = needToFind + nums.size - 1
    val hashCount = str.count { it == '#' }
    if (str.length < minLength || needToFind < hashCount)
        return 0

    val i = nums.indices.maxBy { nums[it] }
    val n = nums[i]
    val match = "[?.](?=[#?]{$n}([?.]|$))".toRegex()

    var sum = 0L
    match.findAll(str).forEach {
        val strLeft = str.substring(0, it.range.first)
        val strRight = str.substring(it.range.last + n + 1)
        val numLeft = arrangementsCached(strLeft, nums.take(i))
        val numRight = arrangementsCached(strRight, nums.drop(i + 1))
        sum += numLeft * numRight
    }
//    if (str.length >= 3)
//        println("$str $nums $sum")
    return sum
}

fun String.parse() = Springs(chunk(0), chunk(1).split(",").map { it.toInt() })

// part 1

fun List<String>.part1(): Long = sumOf {
    print(".")
    it.parse().arrangements(print = false)
}.also { println() }

// part 2

fun List<String>.part2(): Long = withIndex().sumOf {
    val mod = (size / 100).coerceAtLeast(1)
    if (it.index % mod == 0)
        print("x")
    else
        print(".")
    val puzzle = it.value.parse()
    val arrangements = puzzle.arrangements(false)
    val options = puzzle.unfoldOptions()
    val sum = arrangements.toDouble().pow(5).toLong() +
        options.drop(1).sumOf { it.arrangements(false) }
    sum
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
