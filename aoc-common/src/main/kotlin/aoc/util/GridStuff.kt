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

fun <X> grid(xsize: Int, ysize: Int, init: (Loc) -> X) =
    List(ysize) { y -> List(xsize) { x -> init(Loc(x, y)) } }

fun intgrid(xsize: Int, ysize: Int): IntGrid =
    Array(ysize) { Array(xsize) { 0 } }

//endregion

//region get values in grids

fun <X> List<List<X>>.allIndices() =
    indices.flatMap { y -> get(y).indices.map { x -> x to y }}

operator fun <X> List<List<X>>.get(coord: Coord) = this[coord.second][coord.first]
operator fun List<String>.get(coord: Coord) = this[coord.second][coord.first]

//endregion

//region finders

fun <X> List<List<X>>.find(ch: X): Coord {
    val i = indexOfFirst { it.contains(ch) }
    val j = this[i].indexOf(ch)
    return i to j
}

fun <X> List<List<X>>.findCoords(pred: (X) -> Boolean): Map<X, List<Coord>> =
    allIndices().filter { pred(this[it]) }.groupBy { this[it] }

//endregion