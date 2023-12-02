package aoc.util

// assume this is a list of rows, so y is first
operator fun <X> List<List<X>>.get(it: Loc) = this[it.y][it.x]
operator fun <X> Array<Array<X>>.get(it: Loc) = this[it.y][it.x]
operator fun <X> List<MutableList<X>>.set(it: Loc, value: X) { this[it.y][it.x] = value }
operator fun <X> Array<Array<X>>.set(it: Loc, value: X) { this[it.y][it.x] = value }

// Utilities for working with a rectangular grid of values

typealias IntGrid = Array<Array<Int>>

fun intgrid(xsize: Int, ysize: Int) =
    Array(ysize) { Array(xsize) { 0 } }