package aoc.util

// Utilities for working with a rectangular grid of values

typealias Coord = Pair<Int, Int>

fun Coord.adj(input: List<List<*>>) =
    listOf(left, right, top, bottom).filter {
        it.first in input.indices && it.second in input[0].indices
    }

val Coord.x
    get() = first
val Coord.y
    get() = second

val Coord.left
    get() = first-1 to second
val Coord.right
    get() = first+1 to second
val Coord.top
    get() = first to second-1
val Coord.bottom
    get() = first to second+1

operator fun <X> List<List<X>>.get(coord: Coord) = this[coord.first][coord.second]

fun <X> List<List<X>>.allIndices() =
    indices.flatMap { i -> get(i).indices.map { i to it }}

fun <X> List<List<X>>.find(ch: X): Coord {
    val i = indexOfFirst { it.contains(ch) }
    val j = this[i].indexOf(ch)
    return i to j
}