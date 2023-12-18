import aoc.AocParser.Companion.parselines
import aoc.*
import aoc.util.*

val testInput = """
2413432311323
3215453535623
3255245654254
3446585845452
4546657867536
1438598798454
4457876987766
3637877979653
4654967986887
4564679986453
1224686865563
2546548887735
4322674655533
""".parselines

fun List<String>.parse() = map { it.map { it.digitToInt() } }

class SearchPath(val grid: List<List<Int>>, val dirs: List<Coord>, val pos: List<Coord>) {
    val heatLoss = pos.drop(1).sumOf { grid[it] }
    val distToEnd = (grid.size - 1 - pos.last().y) + (grid[0].size - 1 - pos.last().x)
    val pathScore = heatLoss + 16 * distToEnd / 7
    private val validDirections: List<Coord>
    init {
        if (dirs.isEmpty()) {
            validDirections = listOf(RIGHT, DOWN)
        } else {
            val last = dirs.last()
            val last3 = dirs.takeLast(3)
            val turnDirs = if (last == UP || last == DOWN) listOf(LEFT, RIGHT) else listOf(UP, DOWN)
            val dirs = if (last3.size == 3 && last3.toSet().size == 1) {
                turnDirs
            } else {
                turnDirs + last
            }
            validDirections = dirs.filter { dir ->
                val nextPos = pos.last() + dir
                nextPos !in pos && nextPos.x in grid[0].indices && nextPos.y in grid.indices
            }
        }
    }
    /** Get set of next paths for part 1. */
    fun nextPaths() = validDirections.map { dir ->
        val nextPos = pos.last() + dir
        SearchPath(grid, dirs + dir, pos + nextPos)
    }
    /** Get set of next paths for part 2, where you have to go 4-10 blocks in one direction before changing. */
    fun nextPathsUltra(): List<SearchPath> {
        return when {
            dirs.isEmpty() -> nextPathsUltraRight() + nextPathsUltraDown()
            dirs.last() in setOf(UP, DOWN) -> nextPathsUltraLeft() + nextPathsUltraRight()
            else -> nextPathsUltraUp() + nextPathsUltraDown()
        }
    }
    private fun nextPathsUltraLeft(): List<SearchPath> {
        val lastPos = pos.last()
        val searchLeft = lastPos.x - 4 >= 0
        return if (searchLeft) {
            (4..10).filter { lastPos.x - it >= 0 }.map {
                val newDirs = (1..it).map { LEFT }
                val newPos = (1..it).map { Coord(lastPos.x - it, lastPos.y) }
                SearchPath(grid, dirs + newDirs, pos + newPos)
            }
        } else {
            listOf()
        }
    }
    private fun nextPathsUltraRight(): List<SearchPath> {
        val lastPos = pos.last()
        val gridCols = grid[0].size
        val searchRight = lastPos.x + 4 <= gridCols - 1
        return if (searchRight) {
            (4..10).filter { lastPos.x + it <= gridCols - 1 }.map {
                val newDirs = (1..it).map { RIGHT }
                val newPos = (1..it).map { Coord(lastPos.x + it, lastPos.y) }
                SearchPath(grid, dirs + newDirs, pos + newPos)
            }
        } else {
            listOf()
        }
    }
    private fun nextPathsUltraUp(): List<SearchPath> {
        val lastPos = pos.last()
        val searchUp = lastPos.y - 4 >= 0
        return if (searchUp) {
            (4..10).filter { lastPos.y - it >= 0 }.map {
                val newDirs = (1..it).map { UP }
                val newPos = (1..it).map { Coord(lastPos.x, lastPos.y - it) }
                SearchPath(grid, dirs + newDirs, pos + newPos)
            }
        } else {
            listOf()
        }
    }
    private fun nextPathsUltraDown(): List<SearchPath> {
        val lastPos = pos.last()
        val searchDown = lastPos.y + 4 <= grid.size - 1
        return if (searchDown) {
            (4..10).filter { lastPos.y + it <= grid.size - 1 }.map {
                val newDirs = (1..it).map { DOWN }
                val newPos = (1..it).map { Coord(lastPos.x, lastPos.y + it) }
                SearchPath(grid, dirs + newDirs, pos + newPos)
            }
        } else {
            listOf()
        }.filter {
            it.pos.last() !in it.pos.dropLast(1)
        }
    }
}

// part 1

val TAKE_N = 1000
val REPORT_ITERS = 1000
val REPORT_ITERS_2 = 50
val START_HEAT_LOSS = 1000
val START_HEAT_LOSS_2 = 1000
val LAST_PATH_2 = 15
val bef = ANSI_CYAN
val aft = ANSI_RESET

fun List<String>.part1(): Int {
    return 0
    val grid = parse()
    val coord0 = Coord(0, 0)
    val coordN = Coord(grid[0].size - 1, grid.size - 1)
    val path = SearchPath(grid, listOf(), listOf(coord0))
    // store best paths by direction entering
    val bestPaths = mutableMapOf(
        coord0 to mutableMapOf(listOf<Coord>() to path)
    )
    println("Target: $bef$coordN$aft EstimatedHeatLoss: $bef${path.pathScore}$aft StartHeatLoss: $bef$START_HEAT_LOSS$aft")
    val paths = mutableSetOf(path)
    var i = 0
    var minHeatLoss = START_HEAT_LOSS
    while (paths.isNotEmpty()) {
        val sorted = paths.sortedBy { it.pathScore }
        val first100 = sorted.take(TAKE_N)
        val pathsToSearchNow = first100.flatMap { it.nextPaths() }
        val nextPaths = pathsToSearchNow.filter { newPath ->
            val last = newPath.pos.last()
            val last3 = newPath.dirs.takeLast(4)
            val curBest = bestPaths[last]?.get(last3)
            if (last == coordN) {
                bestPaths.putIfAbsent(last, mutableMapOf())
                bestPaths[last]!![last3] = newPath
                false
            } else if (newPath.heatLoss > minHeatLoss) {
                false
            } else if (curBest == null || newPath.pathScore < curBest.pathScore) {
                bestPaths.putIfAbsent(last, mutableMapOf())
                bestPaths[last]!![last3] = newPath
                true
            } else {
                false
            }
        }

        paths.removeAll(first100)
        paths.removeAll { it.heatLoss > minHeatLoss }
        paths.addAll(nextPaths)

        i++
        val heatloss = bestPaths[coordN]?.values?.minOfOrNull { it.heatLoss }
        if (i % REPORT_ITERS == 0 || paths.isEmpty() || (heatloss != null && heatloss < minHeatLoss)) {
            if (heatloss != null) minHeatLoss = heatloss
            printProgress(paths, pathsToSearchNow, nextPaths, bestPaths, coordN, i)
        }
    }
    println("Total search iterations: $i")
    return bestPaths[coordN]!!.values.minOf { it.heatLoss }
}

fun printProgress(paths: Set<SearchPath>, pathsToSearchNow: List<SearchPath>, nextPaths: List<SearchPath>, bestPaths: MutableMap<Coord, MutableMap<List<Coord>, SearchPath>>, coordN: Coord, i: Int) {
    val minXY = paths.minOfOrNull { it.pos.last().x + it.pos.last().y }
    print("Search iteration: $bef$i$aft")
    print(" PathsToSearch: $bef${paths.size}$aft")
    print(" MinX+Y_Remaining: $bef$minXY$aft")
    print(" HeatLoss: $bef" + bestPaths[coordN]?.values?.let { "${it.minOf { it.heatLoss }}..${it.maxOf { it.heatLoss }}$aft" })
    print(" Lengths: $bef" + bestPaths[coordN]?.values?.let { "${it.minOf { it.pos.size }}..${it.maxOf { it.pos.size }}$aft" })
    print(" CasesCovered: $bef${bestPaths.entries.sumOf { it.value.size }}$aft")
    print(" $ANSI_BRIGHT_GREEN|||$ANSI_RESET")
    print(" PathsAdded: $bef${nextPaths.size} of ${pathsToSearchNow.size}$aft")
    print(" HeatLoss: $bef${nextPaths.minOfOrNull { it.heatLoss }}..${nextPaths.maxOfOrNull { it.heatLoss }}$aft")
    print(" Lengths: $bef${nextPaths.minOfOrNull { it.pos.size }}..${nextPaths.maxOfOrNull { it.pos.size }}$aft")
    print(" DistToGo: $bef${nextPaths.minOfOrNull { it.distToEnd }}..${nextPaths.maxOfOrNull { it.distToEnd }}$aft")
    print(" Scores: $bef${nextPaths.minOfOrNull { it.pathScore }}..${nextPaths.maxOfOrNull { it.pathScore }}$aft")
    print(" BestPath: ")

    val bestPath = bestPaths[coordN]?.values?.minByOrNull { it.pathScore }
    if (bestPath != null) {
        val bestPathStr = bestPath.dirs.joinToString("") { when(it) {
            UP -> "^"
            DOWN -> "v"
            LEFT -> "<"
            RIGHT -> ">"
            else -> throw IllegalStateException()
        } }
        print("$ANSI_GREEN${bestPathStr}$aft")
        if (bestPath.pos.toSet().size != bestPath.pos.toList().size) {
            print(" $ANSI_RED(some positions duplicated)$aft")
        }
    }
    println()
}

// part 2

fun List<String>.part2(): Int {
    val grid = parse()
    val coord0 = Coord(0, 0)
    val coordN = Coord(grid[0].size - 1, grid.size - 1)
    val path = SearchPath(grid, listOf(), listOf(coord0))
    // store best paths by direction entering
    val bestPaths = mutableMapOf(
        coord0 to mutableMapOf(listOf<Coord>() to path)
    )
    println("Target: $bef$coordN$aft EstimatedHeatLoss: $bef${path.pathScore}$aft StartHeatLoss: $bef$START_HEAT_LOSS_2$aft")
    val paths = mutableSetOf(path)
    var i = 0
    var minHeatLoss = START_HEAT_LOSS_2
    while (paths.isNotEmpty()) {
        val sorted = paths.sortedBy { it.pathScore }
        val first100 = sorted.take(TAKE_N)
        val pathsToSearchNow = first100.flatMap { it.nextPathsUltra() }
        val nextPaths = pathsToSearchNow.filter { newPath ->
            val last = newPath.pos.last()
            // keep track of the best paths entering a coordinate from each direction
            val lastChange = newPath.dirs.indices.drop(1).lastOrNull { newPath.dirs[it] != newPath.dirs[it - 1] }
            val last3 = if (lastChange == null) newPath.dirs.toList() else newPath.dirs.takeLast(newPath.dirs.size - lastChange)

            val curBest = bestPaths[last]?.get(last3)
            if (last == coordN) {
                bestPaths.putIfAbsent(last, mutableMapOf())
                if (curBest == null || newPath.heatLoss < curBest.heatLoss)
                    bestPaths[last]!![last3] = newPath
                false
            } else if (newPath.heatLoss > minHeatLoss) {
                false
            } else if (curBest == null || newPath.pathScore < curBest.pathScore) {
                bestPaths.putIfAbsent(last, mutableMapOf())
                bestPaths[last]!![last3] = newPath
                true
            } else {
                false
            }
        }

        paths.removeAll(first100)
        paths.removeAll { it.heatLoss > minHeatLoss }
        paths.addAll(nextPaths)

        i++
        val heatloss = bestPaths[coordN]?.values?.minOfOrNull { it.heatLoss }
        if (i % REPORT_ITERS_2 == 0 || paths.isEmpty() || (heatloss != null && heatloss < minHeatLoss)) {
            if (heatloss != null) minHeatLoss = heatloss
            printProgress(paths, pathsToSearchNow, nextPaths, bestPaths, coordN, i)
        }
    }
    println("Total search iterations: $i")
    return bestPaths[coordN]!!.values.minOf { it.heatLoss }
}

// calculate answers

val day = 17
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
