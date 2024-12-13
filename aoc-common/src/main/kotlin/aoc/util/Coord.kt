package aoc.util

import kotlin.math.absoluteValue

//region coordinates

typealias Coord = Pair<Int, Int>

val ORIGIN = Coord(0, 0)
val UP = Coord(0, -1)
val DOWN = Coord(0, 1)
val LEFT = Coord(-1, 0)
val RIGHT = Coord(1, 0)
val NORTH = UP
val SOUTH = DOWN
val WEST = LEFT
val EAST = RIGHT
val NE = Coord(1, -1)
val NW = Coord(-1, -1)
val SE = Coord(1, 1)
val SW = Coord(-1, 1)
val DIRS4 = listOf(NORTH, SOUTH, EAST, WEST)
val DIRS8 = listOf(NORTH, SOUTH, EAST, WEST, NE, NW, SE, SW)

fun Coord.turnRight() = when (this) {
    UP -> RIGHT
    RIGHT -> DOWN
    DOWN -> LEFT
    LEFT -> UP
    else -> throw IllegalArgumentException("Invalid direction")
}
fun Coord.turnLeft() = when (this) {
    UP -> LEFT
    LEFT -> DOWN
    DOWN -> RIGHT
    RIGHT -> UP
    else -> throw IllegalArgumentException("Invalid direction")
}

val Coord.x
    get() = first
val Coord.y
    get() = second

operator fun Int.times(c: Coord) = Coord(c.first * this, c.second * this)
operator fun Coord.times(i: Int) = i * this
operator fun Coord.plus(other: Coord) = Coord(first + other.first, second + other.second)
operator fun Coord.minus(other: Coord) = Coord(first - other.first, second - other.second)
fun Coord.plus(x: Int, y: Int) = Coord(first + x, second + y)
fun Coord.adj() = listOf(left, right, top, bottom)

fun Coord.adj(input: List<List<*>>) = adj().filter {
    it.first in input[0].indices && it.second in input.indices
}
fun Coord.adj2(input: List<String>) = adj().filter {
    it.first in input[0].indices && it.second in input.indices
}

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

fun Coord.taxicab(other: Coord) = (x - other.x).absoluteValue + (y - other.y).absoluteValue

//endregion