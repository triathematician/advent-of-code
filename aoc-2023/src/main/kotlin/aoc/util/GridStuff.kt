package aoc.util

// assume this is a list of rows, so y is first
operator fun <X> List<List<X>>.get(it: Loc) = this[it.y][it.x]
operator fun <X> Array<Array<X>>.get(it: Loc) = this[it.y][it.x]
operator fun <X> List<MutableList<X>>.set(it: Loc, value: X) { this[it.y][it.x] = value }
operator fun <X> Array<Array<X>>.set(it: Loc, value: X) { this[it.y][it.x] = value }

// Utilities for working with a rectangular grid of values

typealias IntGrid = Array<Array<Int>>

fun <X> grid(xsize: Int, ysize: Int, init: (Loc) -> X) =
    List(ysize) { y -> List(xsize) { x -> init(Loc(x, y)) } }
fun intgrid(xsize: Int, ysize: Int) = grid(xsize, ysize) { 0 }

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