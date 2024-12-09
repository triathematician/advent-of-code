import aoc.util.AocParser.Companion.parselines
import aoc.*
import aoc.getDayInput

val testInput = """
2333133121414131402
""".parselines

data class Data(val start: Int, val id: Int, val n: Int) {
    val checksum: Long = if (id == -1) 0 else
            (1..n).sumOf { (start + it - 1) * id.toLong() }
}

// part 1

fun String.parse(): List<Data> {
    val data = mutableListOf<Data>()
    var pos = 0
    var id = 0
    forEachIndexed { i, c ->
        val digit = c.toString().toInt()
        if (i % 2 == 0) {
            data += Data(pos, id, digit)
            id++
        } else {
            data += Data(pos, -1, digit)
        }
        pos += digit
    }
    return data
}

fun List<String>.part1(): Long {
    return first().parse().compactify().sumOf { it.checksum }
}

fun List<Data>.compactify(): List<Data> {
    var result = this.filter { it.n > 0 }
    while (true) {
        val firstEmpty = result.firstOrNull { it.id == -1 } ?: return result
        val firstIndex = result.indexOf(firstEmpty)
        val last = result.last()
        result = if (last.n < firstEmpty.n) {
            result.subList(0, firstIndex) +
                    Data(firstEmpty.start, last.id, last.n) +
                    Data(firstEmpty.start + last.n, -1, firstEmpty.n - last.n) +
                    result.subList(firstIndex + 1, result.size - 1)
        } else if (last.n == firstEmpty.n) {
            result.subList(0, firstIndex) +
                    Data(firstEmpty.start, last.id, last.n) +
                    result.subList(firstIndex + 1, result.size - 1)
        } else {
            // split last block
            result.subList(0, firstIndex) +
                    Data(firstEmpty.start, last.id, firstEmpty.n) +
                    result.subList(firstIndex + 1, result.size - 1) +
                    Data(last.start, last.id, last.n - firstEmpty.n)
        }
        result = result.dropLastWhile { it.id == -1 }
    }
}

// part 2

fun List<String>.part2(): Long {
    return first().parse().compactify2().sumOf { it.checksum }
}

fun List<Data>.compactify2(): List<Data> {
    var id = last().id
    var result = this.filter { it.n > 0 }
    while (id >= 0) {
        val lastIndex = result.indexOfLast { it.id == id }
        val last = result[lastIndex]
        val firstEmptySpace = result.indexOfFirst { it.id == -1 && it.n >= last.n }
        if (firstEmptySpace != -1 && firstEmptySpace < lastIndex) {
            // move last block entirely into the empty space
            // keep empty space no matter how small
            val empty = result[firstEmptySpace]
            result = result.subList(0, firstEmptySpace) +
                    Data(empty.start, id, last.n) +
                    Data(empty.start + last.n, -1, empty.n - last.n) +
                    result.subList(firstEmptySpace + 1, lastIndex) +
                    result.subList(lastIndex + 1, result.size)
        }
        id--
    }
    return result
}

// calculate answers

val day = 9
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
