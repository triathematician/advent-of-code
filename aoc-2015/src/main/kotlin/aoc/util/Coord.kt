package aoc.util

import kotlin.math.absoluteValue

//region coordinates

typealias Coord = Pair<Int, Int>

val Coord.x
    get() = first
val Coord.y
    get() = second

operator fun Coord.plus(other: Coord) = Coord(first + other.first, second + other.second)
fun Coord.plus(x: Int, y: Int) = Coord(first + x, second + y)

/** Nearest 4 neighbors to coordinate. */
fun Coord.neighbors4() = listOf(left, right, top, bottom)
/** Nearest 8 neighbors to coordinate. */
fun Coord.neighbors8() = listOf(left, right, top, bottom, left.top, right.top, left.bottom, right.bottom)

/** Nearest 4 neighbors to coordinate. Must be in grid. */
fun Coord.neighbors4(input: List<List<*>>) = neighbors4().filter {
        it.first in input.indices && it.second in input[0].indices
    }

/** Nearest 8 neighbors to coordinate. Must be in grid. */
fun Coord.neighbors8(input: List<List<*>>) = neighbors8().filter {
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

fun Coord.adjacentTo(other: Coord, diagonal: Boolean) = when {
    diagonal -> (other.x - x).absoluteValue <= 1 && (other.y - y).absoluteValue <= 1
    else -> (other.x == x && (other.y - y).absoluteValue <= 1) ||
            (other.y == y && (other.x - x).absoluteValue <= 1)
}

//endregion