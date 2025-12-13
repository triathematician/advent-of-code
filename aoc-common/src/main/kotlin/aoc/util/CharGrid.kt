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

    override fun toString() = ch.joinToString("\n")

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

    fun print() {
        ch.forEach { println(it) }
    }

    /** Prints in pretty form, with each pair of rows combined into a single character. */
    fun printPretty(solid: Char = '#') {
        ch.chunked(2).forEach {
            val row1 = it[0]
            val row2 = if (it.size == 2) it[1] else " ".repeat(row1.length)
            println(row1.zip(row2).joinToString("") { twoGridCellsCombined(it, solid).toString() })
        }
    }

    private fun twoGridCellsCombined(pair: Pair<Char, Char>, solid: Char) = when {
        pair.first == solid && pair.second == solid -> '█'
        pair.first == solid -> '▀'
        pair.second == solid -> '▄'
        else -> ' '
    }
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