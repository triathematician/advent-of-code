package aoc.util

/** Count of each element in a list. */
fun <E> List<E>.eachCount() = groupBy { it }.mapValues { it.value.size }

/** get second item in a collection. */
fun <E> Collection<E>.second(): E = drop(1).first()

/** get all pairs of elements from a list. */
fun <E> List<E>.pairwise(): List<Set<E>> = flatMapIndexed { i, e -> drop(i + 1).map { setOf(e, it) } }

/** get all triples of elements from a list. */
fun <E> List<E>.triples(): Sequence<Set<E>> = indices.asSequence().flatMap { i1 ->
    indices.drop(i1 + 1).flatMap { i2 ->
        indices.drop(i2 + 1).map { i3 ->
            setOf(get(i1), get(i2), get(i3))
        }
    }
}

/** split list of strings by empty/blank lines. */
fun List<String>.splitOnEmptyLines(): List<List<String>> =
    fold(mutableListOf(mutableListOf<String>())) { acc, line ->
        if (line.isBlank()) acc.add(mutableListOf())
        else acc.last().add(line)
        acc
    }

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