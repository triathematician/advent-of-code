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

fun List<Int>.triangle(): Boolean {
    require(size == 3) { "Expected 3 sides, got $size" }
    return this[0] + this[1] > this[2]
            && this[0] + this[2] > this[1]
            && this[1] + this[2] > this[0]
}

//region IntRange UTILS

fun List<IntRange>.fullyOverlapping() = this[0].containsAll(this[1]) || this[1].containsAll(this[0])
fun List<IntRange>.fullyDisjoint() = this[0].last < this[1].first || this[1].last < this[0].first

fun IntRange.containsAll(other: IntRange) = contains(other.first) && contains(other.last)

//endregion