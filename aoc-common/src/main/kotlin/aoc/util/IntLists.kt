package aoc.util

fun List<String>.justTheInts() = mapNotNull { it.toIntOrNull() }

fun List<Int>.cusum() = mutableListOf<Int>().also { res ->
    var sum = 0
    forEach {
        sum += it
        res += sum
    }
}

fun List<Int>.indexOfFirstNegative() = indexOfFirst { it < 0 }

fun List<Int>.eachCount() = groupBy { it }.mapValues { it.value.size }

fun List<Int>.triangle(): Boolean {
    require(size == 3) { "Expected 3 sides, got $size" }
    return this[0] + this[1] > this[2]
            && this[0] + this[2] > this[1]
            && this[1] + this[2] > this[0]
}