package aoc.util

import java.util.PriorityQueue

/** Find BFS path from start to target, given set of adjacencies. */
class Pathfinder<X>(val start: X, val adj: (X) -> Iterable<X>) {

    /** Generates path from start to target (standard BFS). */
    fun findWayTo(target: X): List<X> {
        val visited = mutableSetOf(start)
        val queue = mutableListOf(start)
        val prev = mutableMapOf<X, X>()

        while (queue.isNotEmpty()) {
            val current = queue.removeAt(0)
            if (current == target) break

            for (adjacent in adj(current)) {
                if (visited.contains(adjacent)) continue
                visited.add(adjacent)
                queue.add(adjacent)
                prev[adjacent] = current
            }
        }

        val path = mutableListOf<X>()
        var curr = target
        while (curr in prev) {
            path.add(0, curr)
            curr = prev[curr]!!
        }
        path.add(0, curr)
        return path
    }

    /** Generates path from start to terminal condition provided (standard BFS). */
    fun findWayToGoal(goal: (X) -> Boolean): List<X> {
        val visited = mutableSetOf(start)
        val queue = mutableListOf(start)
        val prev = mutableMapOf<X, X>()
        var tempTarget: X? = null

        while (queue.isNotEmpty()) {
            val current = queue.removeAt(0)
            if (goal(current)) {
                tempTarget = current
                break
            }

            for (adjacent in adj(current)) {
                if (visited.contains(adjacent)) continue
                visited.add(adjacent)
                queue.add(adjacent)
                prev[adjacent] = current
            }
        }

        val path = mutableListOf<X>()
        var curr = tempTarget!!
        while (curr in prev) {
            path.add(0, curr)
            curr = prev[curr]!!
        }
        path.add(0, curr)
        return path
    }

    companion object {
        fun <X> shortestPath(start: X, goal: X, adj: (X) -> Iterable<X>) =
            Pathfinder(start, adj).findWayTo(goal)
        fun <X> shortestPathTo(start: X, goal: (X) -> Boolean, adj: (X) -> Iterable<X>) =
            Pathfinder(start, adj).findWayToGoal(goal)
    }

}

/** Use A* algorithm to find shortest path from start to target. */
class AstarSearch<X>(val start: X, val goal: (X) -> Boolean, val adj: (X) -> Map<X, Long>, val heuristicToFinish: (X) -> Long) {
    /** Generate path from start to target that minimizes cost, returning path with scores for each step. */
    fun minimizeCost(): Pair<List<X>, Long> {
        // track nodes to be expanded - can be priority queue by f() value
        val openSet = PriorityQueue<Pair<X, Long>>(compareBy { it.second })
        // track nodes preceding each node on cheapest path from start
        val cameFrom = mutableMapOf<X, X>()
        // track currently known cost of cheapest path from start to node
        val gScore = mutableMapOf(start to 0L)
        // track g(n) + h(n), current best guess for cheapest path through node
        val fScore = mutableMapOf(start to heuristicToFinish(start))

        openSet.add(start to fScore[start]!!)
        while (openSet.isNotEmpty()) {
            val current = openSet.poll()
//            printCheapestPath(cameFrom, current.first)

            if (goal(current.first))
                return reconstructPath(cameFrom, current.first)

            adj(current.first).forEach { (neighbor, dist) ->
                val tentativeGScore = gScore[current.first]!! + dist
                if (tentativeGScore < gScore.getOrDefault(neighbor, Long.MAX_VALUE)) {
                    cameFrom[neighbor] = current.first
                    gScore[neighbor] = tentativeGScore
                    fScore[neighbor] = tentativeGScore + heuristicToFinish(neighbor)
                    openSet.add(neighbor to fScore[neighbor]!!)
                }
            }
        }

        throw IllegalStateException("No path found")
    }

    /** Generate list of all paths from start to target that minimizes cost, returning path with scores for each step. */
    fun minimizeCostAll(): Pair<List<List<X>>, Long> {
        // track nodes to be expanded - can be priority queue by f() value
        val openSet = PriorityQueue<Pair<X, Long>>(compareBy { it.second })
        // track nodes preceding each node on cheapest path from start
        val cameFrom = mutableMapOf<X, MutableSet<X>>()
        // track currently known cost of cheapest path from start to node
        val gScore = mutableMapOf(start to 0L)
        // track g(n) + h(n), current best guess for cheapest path through node
        val fScore = mutableMapOf(start to heuristicToFinish(start))
        // minimum cost to finish
        var minCost = Long.MAX_VALUE
        // all paths that minimize cost from start to finish
        val minCostPaths = mutableListOf<List<X>>()

        openSet.add(start to fScore[start]!!)
        while (openSet.isNotEmpty()) {
            val current = openSet.poll()
            if (current.second >= minCost)
                break
            printCheapestPath(cameFrom.mapValues { it.value.first() }, current.first)

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
                val prevGScore = gScore.getOrDefault(neighbor, Long.MAX_VALUE)
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
    fun reconstructPath(cameFrom: Map<X, X>, current: X): Pair<List<X>, Long> {
        val path = mutableListOf(current)
        var totalCost = 0L
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
    fun reconstructPathsRec(cameFrom: Map<X, Set<X>>, current: X): Pair<List<List<X>>, Long> {
        if (current !in cameFrom) return listOf(listOf(current)) to 0
        val paths = mutableListOf<List<X>>()
        for (prev in cameFrom[current]!!) {
            val (subPaths, _) = reconstructPathsRec(cameFrom, prev)
            paths.addAll(subPaths.map { it + current })
        }
        val cost = paths.first().fold(0L) { acc, node -> acc + (adj(node)[current] ?: 0L) }
        return paths to cost
    }

    /** Print a representation of the cheapest path. */
    fun printCheapestPath(cameFrom: Map<X, X>, current: X) {
        val goal = goal(current)
        val path = reconstructPath(cameFrom, current)
        print("Current cheapest path score: ${path.second}")
        if (goal) println(" (goal reached)") else println()
        println(path.first.joinToString("") {
            if (it is PosDir)
                "${it.dir.dirChar()}"
            else
                it.toString()
        })
    }
}