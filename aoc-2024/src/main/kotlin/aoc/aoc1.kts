import aoc.AocRunner
import aoc.util.eachCount
import aoc.util.getDayInput
import kotlin.math.abs

val input = getDayInput(1, 2024)
val list1 = input.map { it.substringBefore(" ").toInt() }
val list2 = input.map { it.substringAfter(" ").trim().toInt() }

fun dist(l1: List<Int>, l2: List<Int>): Int {
    val s1 = l1.sorted()
    val s2 = l2.sorted()
    return s1.indices.sumOf { abs(s1[it] - s2[it]) }
}

fun freq(l1: List<Int>, l2: List<Int>): Int {
    val s2counts = l2.eachCount()
    return l1.sumOf { it * (s2counts[it] ?: 0) }
}

println(freq(listOf(3,4,2,1,3,3), listOf(4,3,5,3,9,3)))

AocRunner(1,
    part1 = dist(list1, list2),
    part2 = freq(list1, list2)
).run()