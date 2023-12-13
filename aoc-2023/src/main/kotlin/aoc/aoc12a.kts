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

    fun arrangements() = arrangements(line, nums)
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

fun String.parse() = Springs(chunk(0), chunk(1).split(",").map { it.toInt() })

// part 1

fun List<String>.part1(): Long = sumOf {
    print(".")
    it.parse().arrangements()
}.also { println() }

// part 2

fun List<String>.part2(): Long = 0L

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
