import aoc.AocParser.Companion.parselines
import aoc.*
import aoc.util.*

val testInput = """
#.#####################
#.......#########...###
#######.#########.#.###
###.....#.>.>.###.#.###
###v#####.#v#.###.#.###
###.>...#.#.#.....#...#
###v###.#.#.#########.#
###...#.#.#.......#...#
#####.#.#.#######.#.###
#.....#.#.#.......#...#
#.#####.#.#.#########v#
#.#...#...#...###...>.#
#.#.#v#######v###.###v#
#...#.>.#...>.>.#.###.#
#####v#.#.###v#.#.###.#
#.....#...#...#.#.#...#
#.#########.###.#.#.###
#...###...#...#...#.###
###.###.#.###v#####v###
#...#...#.#.>.>.#.>.###
#.###.###.#.###.#.#v###
#.....###...###...#...#
#####################.#
""".parselines

class MapGrid(val grid: CharGrid) {
    val start = Coord(grid[0].indexOf('.'), 0)
    val end = Coord(grid.last().indexOf('.'), grid.size - 1)

    fun contains(c: Coord) = c.x in grid[0].indices && c.y in grid.indices
    fun path(c: Coord) = grid.at(c) == '.'
    fun slope(c: Coord) = grid.at(c) in "v^<>"
    fun forest(c: Coord) = grid.at(c) == '#'

    /** Get possible locations from [cur]. */
    fun next(prev: Coord, cur: Coord): List<Coord> {
        return if (slope(cur)) {
            // must go in the same direction
            listOf(cur + cur - prev)
        } else cur.adj2(grid)
            .filter { it != prev && !forest(it) }
            .filter { !slope(it) || it-cur == dir(grid.at(it)) } // must go in downhill direction
    }

    private fun dir(c: Char) = when (c) {
        'v' -> DOWN
        '^' -> UP
        '<' -> LEFT
        '>' -> RIGHT
        else -> error("invalid direction $c")
    }

    /** Get all intersections in grid. */
    fun intersections() = grid.allIndices2().filter { it.adj2(grid).count { slope(it) } >= 3 }.toSet()

    /** Get graph between intersections in grid, including the start and end. */
    fun graph(): Map<Coord, Map<Coord, Int>> {
        val result = mutableMapOf<Coord, MutableMap<Coord, Int>>()
        val xx = intersections()
        (listOf(start) + xx).forEach { pos ->
            result[pos] = mutableMapOf()
            val step1 = if (pos == start)
                listOf(start.bottom)
            else
                listOf(pos.right, pos.bottom).filter { contains(it) && slope(it) }
            step1.forEach {
                pathsToNextIntersection(listOf(pos, it), xx).forEach { path ->
                    result[pos]!![path.last()] = path.size - 1
                }
            }
        }
        return result
    }

    /** Get paths from a given starting point to next intersection. */
    fun pathsToNextIntersection(steps: List<Coord>, intersections: Set<Coord>): List<List<Coord>> {
        if (steps.last() in intersections || steps.last() == end)
            return listOf(steps)
        val next = next(steps[steps.size - 2], steps.last()).filter {
            it !in steps
        }
        return next.flatMap { pathsToNextIntersection(steps + it, intersections) }
    }

    /** Get all possible hikes in the grid, starting with given list of steps. */
    fun hike(steps: List<Coord>): List<List<Coord>> {
        if (steps.last() == end)
            return listOf(steps)
        val next = next(steps[steps.size - 2], steps.last()).filter {
            it !in steps
        }
        return next.flatMap { hike(steps + it) }
    }

}

// part 1

fun List<String>.part1(): Int {
    val map = MapGrid(this)
    val hikes = map.hike(listOf(map.start.top, map.start))
    println(hikes.map { it.size }.joinToString(" "))
    return hikes.maxOf { it.size - 2 }
}

// part 2

fun List<String>.part2(): Int {
    val map = MapGrid(this)
    val graph = map.graph()
    val coords = graph.keys.toSet() + graph.values.flatMap { it.keys }.toSet()
    val coordLabel = coords.sortedBy { it.x + it.y }.mapIndexed { i, c -> c to ('A'+i)  }.toMap()
    graph.keys.sortedBy { it.x + it.y }.forEach {
        print("From ${coordLabel[it]}: ")
        graph[it]!!.forEach { (c, d) -> print("${coordLabel[c]}=$d, ") }
        println()
    }
    val allCoords = graph.keys.toSet() + map.end
    val labGraph = allCoords.associateWith { c -> (graph[c]?.keys ?: setOf()) + graph.keys.filter { c in graph[it]!!.keys } }
        .entries.associate { (c0, adjs) ->
            coordLabel[c0]!! to adjs.associate {
                coordLabel[it]!! to (graph[c0]?.get(it) ?: graph[it]!![c0]!!)
            }
        }
    println(labGraph)
//    return longestPath(labGraph, 'A', coordLabel[map.end]!!)
//    return longestPath2(labGraph, LongestPathIndex(coordLabel.values.toSet(), 'A', coordLabel[map.end]!!))
    return longestPath2(labGraph, LongestPathIndex(coordLabel.values.toSet() - setOf('A', 'B', 'D'), 'E', coordLabel[map.end]!!))
}

/** Index for computed values of longest paths for a given restricted set of characters. */
data class LongestPathIndex(val chars: Set<Char>, val start: Char, val end: Char)

val longestPathCache = mutableMapOf<LongestPathIndex, Int>()

/**
 * Find the longest possible path in the given adjacency graph from [start] to [end] without repeating nodes.
 * Paths are restricted to the given [chars].
 * Uses the [longestPathCache] to avoid re-computing paths.
 * Returns the sum of the weights of the edges in the path.
 */
fun longestPath2(graph: Map<Char, Map<Char, Int>>, index: LongestPathIndex): Int {
    val nextOptions = graph[index.start]!!.keys.filter { it in index.chars }.map {
        LongestPathIndex(index.chars - index.start, it, index.end)
    }
    if (index.start == index.end)
        return 0

    return if (nextOptions.isEmpty())
        Int.MIN_VALUE
    else
        nextOptions.maxOf { next ->
        longestPathCache.getOrPut(next) {
            longestPath2(graph, next).also {
                if (it < 0) {
//                    println("No path from ${next.start} to ${next.end} with chars ${next.chars}")
                } else if (graph.keys.size - next.chars.size <= 4) {
                    println("Longest path from ${(graph.keys - next.chars).sorted()} and then ${next.start}: $it")
                } else {
                    // too many to print
                }
            }
        } + graph[index.start]!![next.start]!!
    }
}

/**
 * Find the longest possible path in the given adjacency graph from [start] to [end] without repeating nodes.
 * Returns the sum of the weights of the edges in the path.
 */
fun longestPath(graph: Map<Char, Map<Char, Int>>, start: Char, end: Char): Int {
    val paths = mutableListOf(listOf(start))

    fun pathLength(path: List<Char>) = path.mapIndexed { i, c -> graph[c]!!.getOrDefault(path.getOrNull(i+1) ?: 'z', 0) }.sum()

    var longest = 0
    while (paths.isNotEmpty()) {
        val path = paths.removeAt(0)
        val last = path.last()
        if (last == end) {
            val length = pathLength(path)
            if (length > longest) {
                longest = length
                println("new longest path: $path $length")
            }
        } else {
            val next = graph[last]!!.keys.filter { it !in path }
            next.forEach { paths.add(path + it) }
        }
    }

    return longest
}

// calculate answers

val day = 23
val input = getDayInput(day, 2023)
val testResult = testInput.part1().also { it.print }
val testResult2 = testInput.part2().also { it.print }
val answer1 = input.part1().also { it.print }
val answer2 = input.part2().also { it.print }

// print results

AocRunner(day,
    test = { "$testResult, $testResult2" },
    part1 = { answer1 },
    part2 = { answer2 }
).run()
