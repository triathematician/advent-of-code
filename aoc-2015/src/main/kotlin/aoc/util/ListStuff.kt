package aoc.util

/** divide into [n] lists, giving one to each in order. */
fun <E> List<E>.divideInto(n: Int): List<List<E>> = (0 until n).map { i ->
    indices.filter { it % n == i }.map { get(it) }
}