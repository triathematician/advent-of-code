import aoc.AocParser.Companion.parselines
import aoc.*
import aoc.util.*

val testInput = """
R 6 (#70c710)
D 5 (#0dc571)
L 2 (#5713f0)
D 2 (#d2c081)
R 2 (#59c680)
D 2 (#411b91)
L 5 (#8ceee2)
U 2 (#caa173)
L 1 (#1b58a2)
U 2 (#caa171)
R 2 (#7807d2)
U 3 (#a77fa3)
L 2 (#015232)
U 2 (#7a21e3)
""".parselines

class Direction(val dir: Char, val n: Int, val col: String)

fun String.parseDirection(): Direction {
    val dir = first()
    val n = chunkint(1)
    val col = chunk(2).drop(1).dropLast(1)
    return Direction(dir, n, col)
}
fun String.parseDirection2(): Direction {
    val col = chunk(2).drop(2).dropLast(1)
    val dir = when (col.last()) {
        '0' -> 'R'
        '1' -> 'D'
        '2' -> 'L'
        '3' -> 'U'
        else -> error("Unknown direction $col")
    }
    val dist = col.dropLast(1).toInt(radix = 16)
    return Direction(dir, dist, col)
}

// part 1

fun List<String>.part1(): Int {
    val dirs = map { it.parseDirection() }
    var grid = Array(1000) { Array(1000) { '.' } }
    fun at(c: Coord) = grid[c.y][c.x]
    fun dig(c: Coord) { grid[c.y][c.x] = '#' }
    var pos = Coord(200, 200)
    dig(pos)
    dirs.forEach {
        when (it.dir) {
            'R' -> {
                repeat(it.n) { grid[pos.y][pos.x + it + 1] = '#' }
                pos = Coord(pos.x + it.n, pos.y)
            }
            'L' -> {
                repeat(it.n) { grid[pos.y][pos.x - it - 1] = '#' }
                pos = Coord(pos.x - it.n, pos.y)
            }
            'U' -> {
                repeat(it.n) { grid[pos.y - it - 1][pos.x] = '#' }
                pos = Coord(pos.x, pos.y - it.n)
            }
            'D' -> {
                repeat(it.n) { grid[pos.y + it + 1][pos.x] = '#' }
                pos = Coord(pos.x, pos.y + it.n)
            }
        }
    }

    // check grid
    val minX = grid.minOfOrNull { it.indexOfFirst { it == '#' }.let { if (it < 0) Int.MAX_VALUE else it } }!!
    val maxX = grid.maxOfOrNull { it.indexOfLast { it == '#' } }!!
    val minY = grid.indexOfFirst { it.indexOfFirst { it == '#' } >= 0 }
    val maxY = grid.indexOfLast { it.indexOfLast { it == '#' } >= 0 }
    val countHash = grid.sumOf { it.count { it == '#' } }
    println("Dug path in ${maxX-minX+1} x ${maxY-minY+1} grid, path length $countHash")

    // trim grid
    grid = grid.drop(minY).take(maxY-minY+1).map { it.drop(minX).take(maxX-minX+1).toTypedArray() }.toTypedArray()
    println(grid.joinToString("\n") { it.joinToString("") })

    // fill in grid
    val maxX2 = grid[0].size
    val maxY2 = grid.size
    grid.indices.forEach {
        if (grid[it][0] == '.') grid[it][0] = 'o'
        if (grid[it][maxX2-1] == '.') grid[it][maxX2-1] = 'o'
    }
    grid[0].indices.forEach {
        if (grid[0][it] == '.') grid[0][it] = 'o'
        if (grid[maxY2-1][it] == '.') grid[maxY2-1][it] = 'o'
    }
    var countFill = 1
    while (countFill > 0) {
        countFill = 0
        grid.allIndices().filter { at(it) == '.' }.forEach {
            if (at(it.left) == 'o' || at(it.right) == 'o' || at(it.top) == 'o' || at(it.bottom) == 'o') {
                grid[it.y][it.x] = 'o'
                countFill++
            }
        }
    }
    // replace all non o's with hashes
    grid.allIndices().filter { at(it) != 'o' }.forEach { dig(it) }

    // check grid a second time
    val countHash2 = grid.sumOf { it.count { it == '#' } }
    val countOs = grid.sumOf { it.count { it == 'o' } }
    println("I filled everything in, there are now $countHash2 square meters dug, and $countOs squares not dug")
    println(grid.joinToString("\n") { it.joinToString("") })
    return countHash2
}


// part 2

class Corner(val pos: Coord, val dir1: Char, val dir2: Char) {
    val x = pos.x
    val y = pos.y
    override fun toString() = "${pos.x} $dir1$dir2"
}

fun List<String>.part2(): Long {
    val dirs = map { it.parseDirection2() }
    /** Track all corner positions and type. */
    val corners = mutableListOf<Corner>()
    var pos = Coord(0, 0)
    var lastDir: Char? = null
    dirs.forEach {
        if (lastDir != null) {
            corners += Corner(pos, lastDir!!, it.dir)
        }
        lastDir = it.dir
        when (it.dir) {
            'R' -> pos = Coord(pos.x + it.n, pos.y)
            'L' -> pos = Coord(pos.x - it.n, pos.y)
            'U' -> pos = Coord(pos.x, pos.y - it.n)
            'D' -> pos = Coord(pos.x, pos.y + it.n)
        }
    }
    corners += Corner(pos, lastDir!!, dirs[0].dir)

    // check grid
    val minX = corners.minOf { it.x }
    val maxX = corners.maxOf { it.x }
    val minY = corners.minOf { it.y }
    val maxY = corners.maxOf { it.y }
    val countHash = corners.count()
    println("Dug path in ${maxX-minX+1} x ${maxY-minY+1} grid, # of corners = $countHash")

    // count dugout areas for each row
    val cornerPairs = (corners + corners[0]).zipWithNext()
    val res = (minY..maxY).sumOf {
        countDugRow(cornerPairs, it)
    }
    println("Completed the dig with $res square meters of dugout area")
    return res
}

/** Find all times the pattern intersects with the given row. */
fun countDugRow(cornerPairs: List<Pair<Corner, Corner>>, y: Int): Long {
    val res = mutableMapOf<Int, Char>()
    cornerPairs.filter { y in it.first.y..it.second.y || y in it.second.y..it.first.y }.forEach {
        if (it.first.y != y && it.second.y != y) {
            // path crosses here
            res[it.first.x] = '+'
        } else if (it.first.y == y && it.second.y == y) {
            val dirFirst = if (it.first.dir1 in "LR") it.first.dir2 else it.first.dir1
            val dirSecond = if (it.second.dir1 in "LR") it.second.dir2 else it.second.dir1
            if (dirFirst == dirSecond) {
                // path temporarily on this row but doesn't cross
                res[it.first.x] = 'Z'
                res[it.second.x] = 'Z'
            } else {
                // path temporarily on this row and does cross
                res[it.first.x] = 'U'
                res[it.second.x] = 'U'
            }
        } else {
            // ignore this case, since it's covered with other lines
        }
    }
    var side = Inside.OUTSIDE
    var prevSide: Inside? = null
    var count = 0L
    var pos = Int.MIN_VALUE
    res.entries.sortedBy { it.key }.forEach { (x, type) ->
        // add up number of dugout cells in this row from last position up to and including x
        if (type == '+') {
            when (side) {
                Inside.INSIDE -> {
                    count += x - pos
                    side = Inside.OUTSIDE
                }
                Inside.OUTSIDE -> {
                    count++
                    side = Inside.INSIDE
                }
                else -> error("Invalid state $side")
            }
        } else if (type == 'U') {
            when (side) {
                Inside.ON_LINE -> {
                    count += x - pos
                    side = prevSide!!
                }
                else -> {
                    if (side == Inside.INSIDE)
                        count += x - pos
                    else
                        count++
                    prevSide = side
                    side = Inside.ON_LINE
                }
            }
        } else if (type == 'Z') {
            when (side) {
                Inside.ON_LINE -> {
                    count += x - pos
                    side = if (prevSide == Inside.INSIDE) Inside.OUTSIDE else if (prevSide == Inside.OUTSIDE) Inside.INSIDE else error("Invalid state $prevSide")
                }
                else -> {
                    if (side == Inside.INSIDE)
                        count += x - pos
                    else
                        count++
                    prevSide = side
                    side = Inside.ON_LINE
                }
            }
        } else {
            error("Invalid type $type")
        }
        pos = x
    }
    return count
}

enum class Inside { OUTSIDE, INSIDE, ON_LINE }

// calculate answers

val day = 18
val input = getDayInput(day, 2023)
val testResult = testInput.part1()
val testResult2 = testInput.part2()
val answer1 = input.part1().also { it.print }
val answer2 = input.part2().also { it.print }

// print results

AocRunner(day,
    test = { "$testResult, $testResult2" },
    part1 = { answer1 },
    part2 = { answer2 }
).run()
