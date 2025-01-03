package aoc.util

import kotlin.math.absoluteValue

// assume grids are all arranged by row first, so getting values out requires getting the y value first

//region [Loc] methods

operator fun <X> List<List<X>>.get(it: Loc) = this[it.y][it.x]
operator fun <X> Array<Array<X>>.get(it: Loc) = this[it.y][it.x]
operator fun <X> List<MutableList<X>>.set(it: Loc, value: X) { this[it.y][it.x] = value }
operator fun <X> Array<Array<X>>.set(it: Loc, value: X) { this[it.y][it.x] = value }

//endregion

//region grid definitions and creation

typealias IntGrid = Array<Array<Int>>
typealias CharGrid = List<String>

fun <X> grid(xsize: Int, ysize: Int, init: (Loc) -> X) =
    List(ysize) { y -> List(xsize) { x -> init(Loc(x, y)) } }

fun intgrid(xsize: Int, ysize: Int) = grid(xsize, ysize) { 0 }

//endregion

//region get values in grids

fun <X> Array<Array<X>>.allIndices(): List<Coord> =
    indices.flatMap { y -> get(y).indices.map { x -> x to y }}

fun <X> List<List<X>>.allIndices(): List<Coord> =
    indices.flatMap { y -> get(y).indices.map { x -> x to y }}

fun CharGrid.allIndices2(): List<Coord> =
    indices.flatMap { y -> get(y).indices.map { x -> x to y }}

val CharGrid.xrange
    get() = get(0).indices
val CharGrid.yrange
    get() = indices

fun CharGrid.at(c: Coord) = get(c.y).get(c.x)
fun CharGrid.at(x: Int, y: Int) = get(y).get(x)
fun CharGrid.get(x: Int, y: Int) = get(y).get(x)

fun CharGrid.matchAt(s: String, c: Coord, dir: Coord): Boolean {
    return (c + (s.length - 1) * dir) in this &&
            s.withIndex().all { (i, ch) ->
                at(c + i * dir) == ch
            }
}

operator fun CharGrid.contains(c: Coord) = c.x in get(0).indices && c.y in indices

operator fun <X> List<List<X>>.get(coord: Coord) = this[coord.second][coord.first]
operator fun List<String>.get(coord: Coord) = this[coord.second][coord.first]

//endregion

//region coordinates

typealias Coord = Pair<Int, Int>
data class PosDir(val pos: Coord, val dir: Coord)

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

val Coord.x
    get() = first
val Coord.y
    get() = second

fun Coord.dirChar() = when (this) {
    UP -> '^'
    DOWN -> 'v'
    LEFT -> '<'
    RIGHT -> '>'
    else -> '?'
}

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

operator fun Int.times(c: Coord) = Coord(c.first * this, c.second * this)
operator fun Coord.times(i: Int) = i * this
operator fun Coord.plus(other: Coord) = Coord(first + other.first, second + other.second)
operator fun Coord.minus(other: Coord) = Coord(first - other.first, second - other.second)
fun Coord.plus(x: Int, y: Int) = Coord(first + x, second + y)
fun Coord.adj() = listOf(left, right, top, bottom)

fun Coord.adj(input: List<List<*>>) = adj().filter {
    it.first in input[0].indices && it.second in input.indices
}
fun Coord.adj2(input: CharGrid) = adj().filter {
    it.first in input[0].indices && it.second in input.indices
}
fun Coord.taxicab(other: Coord) = (x - other.x).absoluteValue + (y - other.y).absoluteValue

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

//region finders

fun <X> List<List<X>>.find(ch: X): Coord {
    val i = indexOfFirst { it.contains(ch) }
    val j = this[i].indexOf(ch)
    return i to j
}

fun <X> List<List<X>>.findCoords(pred: (X) -> Boolean): Map<X, List<Coord>> =
    allIndices().filter { pred(this[it]) }.groupBy { get(it.y)[it.x] }

fun CharGrid.findCoords2(pred: (Char) -> Boolean): Map<Char, List<Coord>> =
    allIndices2().filter { pred(this[it]) }.groupBy { this[it] }

//endregion