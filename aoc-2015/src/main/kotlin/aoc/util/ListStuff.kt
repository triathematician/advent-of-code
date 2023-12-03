package aoc.util

/** divide into [n] lists, giving one to each in order. */
fun <E> List<E>.splitInto(n: Int): List<List<E>> = (0 until n).map { i ->
    indices.filter { it % n == i }.map { get(it) }
}

/** get permutations of a list. */
fun <E> List<E>.permutations(): List<List<E>> = when (size) {
    0 -> listOf(emptyList())
    1 -> listOf(this)
    else -> flatMap { e -> (this - e).permutations().map { listOf(e) + it } }
}

/** looks up content in a list of strings, mapping to first result object found. */
fun <X> List<String>.lookup(vararg lookup: Pair<String, X>) =
    lookup.firstOrNull { it.first in this }?.second
        ?: throw IllegalStateException("None of ${lookup.map { it.first } } found in $this")