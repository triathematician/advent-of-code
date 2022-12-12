package aoc

/** Find way from start to target, given set of adjacencies. */
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