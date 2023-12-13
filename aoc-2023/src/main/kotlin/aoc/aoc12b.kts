import aoc.AocParser.Companion.parselines
import aoc.*
import aoc.util.getDayInput

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
    val line = "." + _line
        .replace("?" + "#".repeat(max), "." + "#".repeat(max))
        .replace("#".repeat(max) + "?", "#".repeat(max) + ".")
        .replace("[.]+".toRegex(), ".")

    // create five copies of line joined by "?"
    fun unfold() = Springs(
        "." +
        (1..5).joinToString("?") { line.drop(1) },
        (1..5).flatMap { nums }
    )

    fun arrangements() = arrangements(line, nums)
    fun arrangements2(print: Boolean): Long {
        val res2 = arrangementsCached(line, nums)
        if (print) {
            println("$line $nums res2 $res2")
//            val res = arrangements(line, nums)
//            if (res != res2) {
//                println("$line $nums res $res != res2 $res2")
//            }
        }
        return res2
    }
}

fun arrangements(str: String, nums: List<Int>): Long {
    if ('?' !in str) {
        val match = str.split("\\.+".toRegex()).map { it.length }.filter { it > 0 } == nums
        return if (match) 1 else 0
    }
    val option1 = str.replaceFirst('?', '#')
    val option2 = str.replaceFirst('?', '.')
    return arrangements(option1, nums) + arrangements(option2, nums)
}

val cache = mutableMapOf<String, Long>()

fun arrangementsCached(str: String, nums: List<Int>): Long {
    if (str.length > 30)
        return arrangements3(str, nums)

    val case = "$str ${nums.joinToString(",")}"
    if (case in cache)
        return cache[case]!!
    val res = arrangements3(str, nums)
    cache[case] = res
    return res
}

fun arrangements3(str: String, nums: List<Int>): Long {
    if (nums.isEmpty())
        return if ('#' in str) 0 else 1
    val remainder = nums.sumOf { it + 1 } - 1
    if (str.length < remainder)
        return 0

    // find match in line, where match is sequence of n of ? or #, followed by either ? or . or end of line
    val n = nums.first()
    val match = "^[.?]*?([#?]{$n}([?.]|$))".toRegex()
    val found = match.find(str) ?: return 0
    val group1 = found.groups[1]!!
    val withThisGroup = arrangements3(str.substring(group1.range.last + 1), nums.drop(1))
    val withoutThisGroup = if (group1.value[0] == '#') 0 else
        arrangements3(str.substring(group1.range.first + 1), nums)
    return withThisGroup + withoutThisGroup
}

fun String.parse() = Springs(chunk(0), chunk(1).split(",").map { it.toInt() })

// part 1

fun List<String>.part1(): Long = sumOf {
    print(".")
    it.parse().arrangements2(print = false)
}.also { println() }

// part 2

fun List<String>.part2(): Long = withIndex().sumOf {
    val mod = (size / 100).coerceAtLeast(1)
    if (it.index % mod == 0)
        print("x")
    else
        print(".")
    it.value.parse().unfold().arrangements2(print = false)
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
