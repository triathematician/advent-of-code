import aoc.AocParser.Companion.parselines
import aoc.*
import aoc.util.*

val testInput = """
...........
.....###.#.
.###.##..#.
..#.#...#..
....#.#....
.##..S####.
.##..#...#.
.......##..
.##.#.####.
.##..##.##.
...........
""".parselines

// part 1

fun List<String>.part1(): Int {
    val grid = GridBlinker(this, findCoords2 { it == 'S' }.values.first().first())
    repeat(if (size < 20) 6 else 64) {
        grid.step()
    }
    return grid.sizes.last()
}

// part 2

fun CharGrid.open(c: Coord) = getOrNull(c.y)?.getOrNull(c.x)?.let { it == '.' || it == 'S' } ?: false
fun CharGrid.offMap(c: Coord) = getOrNull(c.y)?.getOrNull(c.x) == null

class GridBlinker(val map: CharGrid, val start: Coord) {
    var posn = setOf<Coord>(start)
    val sizes = mutableListOf(1)
    val mayGoOff = mutableListOf(UP, DOWN, LEFT, RIGHT)
    /** Take one step in the grid, updating positions and sizes. Return a list of steps that would go off the map. */
    fun step(): List<Coord> {
        val off = posn.flatMap { p ->
            mayGoOff.toList().mapNotNull { dir ->
                val c = p + dir
                if (map.offMap(c)) {
                    mayGoOff.remove(dir)
                    c
                } else null
            }
        }
        posn = posn.flatMap {
            listOf(it.top, it.bottom, it.left, it.right).filter { map.open(it) }
        }.toSet()
        sizes += posn.size
        return off
    }

    /**
     * Determine # of positions after [iters] iterations, given starting step #s (keys in [start]) and copies (values in [start]).
     * Assume the grid has already been iterated at least (map.size*2) times.
     */
    fun after(iters: Int, start: Map<Int, Int>): Long {
        return start.entries.sumOf { (stepNo, copies) ->
            val sample = if (iters - stepNo < map.size * 2)
                sizes[iters - stepNo]
            else
                sizes[map.size * 2 + (iters - stepNo) % 2]
            sample * copies.toLong()
        }
    }
}

fun List<String>.part2(ITERS: Int): Long {
    val sz = size
    val md = sz / 2
    // create instance of each type of grid, based on where the starting position is
    val gridUL = GridBlinker(this, Coord(0, 0))
    val gridU = GridBlinker(this, Coord(md, 0))
    val gridUR = GridBlinker(this, Coord(sz-1, 0))
    val gridL = GridBlinker(this, Coord(0, md))
    val gridC = GridBlinker(this, Coord(md, md))
    val gridR = GridBlinker(this, Coord(sz-1, md))
    val gridDL = GridBlinker(this, Coord(0, sz-1))
    val gridD = GridBlinker(this, Coord(md, sz-1))
    val gridDR = GridBlinker(this, Coord(sz-1, sz-1))
    // gather sequences for each type of grid
    listOf(gridUL, gridU, gridUR, gridL, gridC, gridR, gridDL, gridD, gridDR).forEach { grid ->
        repeat(size * 2 + 2) { grid.step() }
    }
    // determine entry points in # of steps for each type of grid
    val centerEntry = 0
    // generate sequence with 66, 197, ... for each of the four compass directions
    val edgeEntry = generateSequence(md + 1) { it + sz }.takeWhile { it <= ITERS }.toList()
    // generate sequence with 132, 263, 263, 394, 394, 394, etc. for each of the four corners of the grid
    val cornerEntry = generateSequence(sz + 1) { it + sz }.takeWhile { it <= ITERS }.toList()
        .map { it to it/sz }
    return gridC.after(ITERS, start = mapOf(centerEntry to 1)) +
            listOf(gridU, gridD, gridL, gridR).sumOf { grid ->
                edgeEntry.sumOf { grid.after(ITERS, start = mapOf(it to 1)) }
            } +
            listOf(gridUL, gridUR, gridDL, gridDR).sumOf { grid ->
                cornerEntry.sumOf { grid.after(ITERS, start = mapOf(it.first to it.second)) }
            }
}

fun List<String>.part2b(): Int {
    val grid = GridBlinker(this, findCoords2 { it == 'S' }.values.first().first())
    val wid = size
    /** Keep track of where you first enter each grid, since a unique starting location will yield a unique pattern. */
    val gridsByStart = mutableMapOf(grid.start to grid)
    /** Track the entry times to each grid. */
    val grids = mutableMapOf(grid to mutableListOf(0))
    (1..200).forEach { step ->
        grids.keys.toList().forEach {
            val offMap = it.step()
            offMap.forEach {
                val newStart = (it.x + wid) % wid to (it.y + wid) % wid
                print("Leaving map after $step steps at $it, will reenter at $newStart, ")
                val existingGrid = gridsByStart[newStart]
                if (existingGrid != null) {
                    println("this start has already been seen ${grids[existingGrid]!!.size} times.")
                    grids[existingGrid]!!.add(step)
                } else {
                    println("this starting position is new.")
                    val newGrid = GridBlinker(this, newStart)
                    gridsByStart[newStart] = newGrid
                    grids[newGrid] = mutableListOf(step)
                }
            }
        }
    }
    return 0
}

/** Attempt to create a bigger maze and see if there's a pattern. */
fun List<String>.part2a(iters: Int): Int {
    val COPIES = 2 * (iters/size) + 3
    val map0 = this as CharGrid
    val map101a = map0.map { it.repeat(COPIES).replace("S", ".") }
    val map = generateSequence { map101a }.take(COPIES).flatten().toMutableList()
    val xr = map.xrange
    val yr = map.yrange
    val start = xr.last/2 to yr.last/2
    println(start)
    var posn = setOf<Coord>(start)
    val sizes = mutableListOf(1)
    repeat(iters) { i ->
        posn = posn.flatMap {
            listOf(it.top, it.bottom, it.left, it.right).filter { map.at(it) == '.' || map.at(it) == 'S' }
        }.toSet()
        sizes += posn.size
        if (i in listOf(6, 10, 50, 64, 100, 500, 1000, 5000)) print(" " + sizes[i])
    }
    println()
//    val diffs = sizes.drop(wid).mapIndexed { i, v -> v - sizes[i] }
//    diffs.chunked(wid).forEach {
//        println(it.joinToString(" ") { "%5d".format(it) } )
//    }
    return sizes.last()
}

// calculate answers

val day = 21
val input = getDayInput(day, 2023)
val testResult = testInput.part1().also { it.print }
testInput.part2a(101)

val answer1 = input.part1().also { it.print }
val answer2a = input.part2a(101).also { it.print }
listOf(6, 10, 50, 64, 100, 500, 1000, 5000).forEach {
    print("$it $ANSI_BLUE${input.part2(it)}$ANSI_RESET ")
}
println()
val answer2 = input.part2(26501365).also { it.print }

// print results

AocRunner(day,
    test = { "$testResult" },
    part1 = { answer1 },
    part2 = { answer2 }
).run()
