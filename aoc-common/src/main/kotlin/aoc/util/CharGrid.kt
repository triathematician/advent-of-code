package aoc.util

/**
 * 2D grid of characters. Stored as a list of strings, where each string is a row (fixed y).
 */
class CharGrid(_ch: List<String>) {

    val ch = _ch.toMutableList()

    val xindices = ch.maxOf { it.length }.let { 0 until it }
    val yindices = ch.indices
    val xsize = xindices.last
    val ysize = yindices.last

    fun contains(c: Coord) = c.x in 0..xsize && c.y in 0..ysize
    fun at(c: Coord) = get(c.x, c.y)
    fun get(x: Int, y: Int) = ch[y][x]
    fun getOrNull(c: Coord) = ch.getOrNull(c.y)?.getOrNull(c.x)
    fun getOrNull(x: Int, y: Int) = ch.getOrNull(y)?.getOrNull(x)
    fun put(x: Int, y: Int, char: Char) {
        ch[y] = ch[y].replaceRange(x, x+1, char.toString())
    }

    fun allIndices() =
        yindices.flatMap { y -> ch[y].indices.map { x -> x to y }}

    fun count(op: (Char) -> Boolean) = ch.sumOf { it.count(op) }

    fun find(ch: Char) = allIndices().first { at(it) == ch }
    fun find(pred: (Char) -> Boolean): Map<Char, List<Coord>> =
        allIndices().filter { pred(ch[it]) }.groupBy { ch[it] }
}

fun List<String>.grid() = CharGrid(this)

fun chargrid(xsize: Int, ysize: Int, op: (Coord) -> Char) =
    (0 until ysize).map { y ->
        (0 until xsize).joinToString("") { x -> op(Coord(x, y)).toString() }
    }
/** Nearest 4 neighbors to coordinate. Must be in grid. */
fun Coord.neighbors4(input: CharGrid) = neighbors4().filter { input.contains(it) }

/** Nearest 8 neighbors to coordinate. Must be in grid. */
fun Coord.neighbors8(input: CharGrid) = neighbors8().filter { input.contains(it) }