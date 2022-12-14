package aoc.util

// Utilities for working with a rectangular grid of values

typealias CharGrid = List<String>
typealias Coord = Pair<Int, Int>

val Coord.x
    get() = first
val Coord.y
    get() = second
val CharGrid.xrange
    get() = get(0).indices
val CharGrid.yrange
    get() = indices

fun CharGrid.at(c: Coord) = get(c.y).get(c.x)
fun CharGrid.get(x: Int, y: Int) = get(y).get(x)

fun Coord.adj(input: List<List<*>>) =
    listOf(left, right, top, bottom).filter {
        it.first in input.indices && it.second in input[0].indices
    }

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