import aoc.AocRunner
import aoc.util.getDayInput

val input = getDayInput(1, 2023)
val digits = listOf("one", "two", "three", "four", "five", "six", "seven", "eight", "nine")

val sum1 = input
    .sumOf { it.first { it.isDigit() }.digitToInt() * 10 + it.last { it.isDigit() }.digitToInt() }

val sum2 = input.sumOf { it.firstAndLastDigit() }

fun String.digitAt(index: Int) = digits.indexOfFirst { substring(index).startsWith(it) } + 1

fun String.firstAndLastDigit(): Int {
    val firstNum = indexOfFirst { it.isDigit() }
    val firstStr = digits.map { indexOf(it) }.filter { it != -1 }.minOrNull()
    val lastNum = indexOfLast { it.isDigit() }
    val lastStr = digits.map { lastIndexOf(it) }.filter { it != -1 }.maxOrNull()
    val firstDig = when {
        firstNum == -1 -> digitAt(firstStr!!)
        firstStr == null || firstStr > firstNum -> get(firstNum).digitToInt()
        else -> digitAt(firstStr)
    }
    val lastDig = when {
        lastNum == -1 -> digitAt(lastStr!!)
        lastStr == null || lastStr < lastNum -> get(lastNum).digitToInt()
        else -> digitAt(lastStr)
    }
    return firstDig * 10 + lastDig
}

AocRunner(1,
    part1 = sum1,
    part2 = sum2
).run()