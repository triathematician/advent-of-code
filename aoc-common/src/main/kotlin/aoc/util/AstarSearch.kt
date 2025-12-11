package aoc.util

import java.util.*

/**
 * Use A* algorithm to find shortest path from start to target.
 * The heuristic function is the estimated distance from current node to goal, and should be smaller or equal to the actual minimal cost.
 */
class AstarSearch<X>(val start: X, val goal: (X) -> Boolean, val adj: (X) -> Map<X, Int>, val heuristicToFinish: (X) -> Int) {

    companion object {
        fun <X> toFinish(start: X, goal: X, adj: (X) -> Set<X>, heuristicToFinish: (X) -> Int) =
            AstarSearch(start, { it == goal }, { adj(it).associateWith { 1 } }, heuristicToFinish)
    }

    /** Generate path from start to target that minimizes cost, returning path with scores for each step. */
    fun minimizeCost(): Pair<List<X>, Int> {
        // track nodes to be expanded - can be priority queue by f() value
        val openSet = PriorityQueue<Pair<X, Int>>(compareBy { it.second })
        // track nodes preceding each node on cheapest path from start
        val cameFrom = mutableMapOf<X, X>()
        // track currently known cost of cheapest path from start to node
        val gScore = mutableMapOf(start to 0)
        // track g(n) + h(n), current best guess for cheapest path through node
        val fScore = mutableMapOf(start to heuristicToFinish(start))

        // track minimum f,g,h
        val visited = mutableSetOf(start)
        var iter = 0
        var minH = fScore.values.first()

        openSet.add(start to fScore[start]!!)
        while (openSet.isNotEmpty()) {
            val current = openSet.poll()
//            printCheapestPath(cameFrom, current.first)
            if (goal(current.first))
                return reconstructPath(cameFrom, current.first)

            val tryNext = adj(current.first) - visited
            tryNext.forEach { (neighbor, dist) ->
                val tentativeGScore = gScore[current.first]!! + dist
                if (tentativeGScore < gScore.getOrDefault(neighbor, Int.MAX_VALUE)) {
                    cameFrom[neighbor] = current.first
                    val h = heuristicToFinish(neighbor)
                    gScore[neighbor] = tentativeGScore
                    fScore[neighbor] = tentativeGScore + h
                    minH = minH.coerceAtMost(h)
                    openSet.add(neighbor to fScore[neighbor]!!)
                }
            }
            visited.addAll(tryNext.keys)

            if (++iter % 1000000 == 0) {
                println("Iteration $iter: min h=${minH}, states searched=${visited.size}")
            }
        }

        throw IllegalStateException("No path found")
    }

    /** Generate list of all paths from start to target that minimizes cost, returning path with scores for each step. */
    fun minimizeCostAll(): Pair<List<List<X>>, Int> {
        // track nodes to be expanded - can be priority queue by f() value
        val openSet = PriorityQueue<Pair<X, Int>>(compareBy { it.second })
        // track nodes preceding each node on cheapest path from start
        val cameFrom = mutableMapOf<X, MutableSet<X>>()
        // track currently known cost of cheapest path from start to node
        val gScore = mutableMapOf(start to 0)
        // track g(n) + h(n), current best guess for cheapest path through node
        val fScore = mutableMapOf(start to heuristicToFinish(start))
        // minimum cost to finish
        var minCost = Int.MAX_VALUE
        // all paths that minimize cost from start to finish
        val minCostPaths = mutableListOf<List<X>>()

        openSet.add(start to fScore[start]!!)
        while (openSet.isNotEmpty()) {
            val current = openSet.poll()
            if (current.second >= minCost)
                break
//            printCheapestPath(cameFrom.mapValues { it.value.first() }, current.first)

            if (goal(current.first)) {
                val paths = reconstructPathsRec(cameFrom, current.first)
                if (paths.second < minCost) {
                    minCostPaths.clear()
                }
                minCost = paths.second
                minCostPaths.addAll(paths.first)
            }

            adj(current.first).forEach { (neighbor, dist) ->
                val tentativeGScore = gScore[current.first]!! + dist
                val prevGScore = gScore.getOrDefault(neighbor, Int.MAX_VALUE)
                if (tentativeGScore < prevGScore) {
                    cameFrom[neighbor] = mutableSetOf(current.first)
                    gScore[neighbor] = tentativeGScore
                    fScore[neighbor] = tentativeGScore + heuristicToFinish(neighbor)
                    openSet.add(neighbor to fScore[neighbor]!!)
                } else if (tentativeGScore == prevGScore) {
                    cameFrom.getOrPut(neighbor) { mutableSetOf() }.add(current.first)
                }
            }
        }

        return minCostPaths to minCost
    }

    /** Reconstruct path from start to current node, returning list of nodes and total cost. */
    fun reconstructPath(cameFrom: Map<X, X>, current: X): Pair<List<X>, Int> {
        val path = mutableListOf(current)
        var totalCost = 0
        var curr = current
        while (curr in cameFrom) {
            val prev = cameFrom[curr]!!
            totalCost += adj(prev)[curr]!!
            path.add(0, prev)
            curr = prev
        }
        return path to totalCost
    }

    /** Reconstruct multiple paths from start to current node, returning list of nodes and total cost. */
    // construct paths recursively
    fun reconstructPathsRec(cameFrom: Map<X, Set<X>>, current: X): Pair<List<List<X>>, Int> {
        if (current !in cameFrom) return listOf(listOf(current)) to 0
        val paths = mutableListOf<List<X>>()
        for (prev in cameFrom[current]!!) {
            val (subPaths, _) = reconstructPathsRec(cameFrom, prev)
            paths.addAll(subPaths.map { it + current })
        }
        val cost = paths.first().fold(0) { acc, node -> acc + (adj(node)[current] ?: 0) }
        return paths to cost
    }

    /** Print a representation of the cheapest path. */
    fun printCheapestPath(cameFrom: Map<X, X>, current: X) {
        val goal = goal(current)
        val path = reconstructPath(cameFrom, current)
        print("Current cheapest path score: ${path.second}")
        if (goal) println(" (goal reached)") else println()
        println(path.first.joinToString("") {
            if (it is Loc2Dir)
                "${it.dir.c}"
            else
                it.toString()
        })
    }
}