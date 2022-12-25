package aoc.util

/** divide into [n] lists, giving one to each in order. */
fun <E> List<E>.splitInto(n: Int): List<List<E>> = (0 until n).map { i ->
    indices.filter { it % n == i }.map { get(it) }
}

/** looks up content in a list of strings, mapping to first result object found. */
fun <X> List<String>.lookup(vararg lookup: Pair<String, X>) =
    lookup.firstOrNull { it.first in this }?.second
        ?: throw IllegalStateException("None of ${lookup.map { it.first } } found in $this")