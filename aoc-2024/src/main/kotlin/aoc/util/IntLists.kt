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