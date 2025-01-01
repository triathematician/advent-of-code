package aoc.aoc2016

import aoc.AocDay
import aoc.util.AstarSearch
import aoc.util.Pathfinder
import aoc.util.pairwise

class AocDay11: AocDay(11, 2016) {
    companion object { @JvmStatic fun main(args: Array<String>) { AocDay11().run() } }

    override val testinput = """
        The first floor contains a hydrogen-compatible microchip and a lithium-compatible microchip.
        The second floor contains a hydrogen generator.
        The third floor contains a lithium generator.
        The fourth floor contains nothing relevant.
    """.trimIndent().lines()

    fun List<String>.parse(): State {
        return if (this == testinput)
            State(
                generators = mapOf("HG" to "F2", "LG" to "F3"),
                microchips = mapOf("HM" to "F1", "LM" to "F1")
            )
        else
            State(
                generators = mapOf("LG" to "F1", "TG" to "F1", "PG" to "F1", "RG" to "F1", "CG" to "F1"),
                microchips = mapOf("TM" to "F1", "RM" to "F1", "CM" to "F1", "LM" to "F2", "PM" to "F2")
            )
    }

    fun List<String>.parse2(): State {
        val part1 = parse()
        return State(
            generators = part1.generators + mapOf("EG" to "F1", "DG" to "F1"),
            microchips = part1.microchips + mapOf("EM" to "F1", "DM" to "F1")
        )
    }

    /**
     * Track locations of generators, microchips, and elevator.
     * Generator keys are HG, LG, ...
     * Microchip keys are HM, LM, ...
     */
    data class State(
        val generators: Map<String, String> = mapOf(),
        val microchips: Map<String, String> = mapOf(),
        val elevator: String = "F1"
    ) {
        val FLOORS: List<String> = listOf("F1","F2","F3","F4")
        val types = generators.keys.map { it.first() }.sorted()

        /** Get mapping of items per floor. */
        fun itemsPerFloor(): Map<String, Set<String>> {
            val res = FLOORS.associateWith { mutableSetOf<String>() }
            generators.forEach { (item, floor) -> res[floor]!!.add(item) }
            microchips.forEach { (item, floor) -> res[floor]!!.add(item) }
            res[elevator]!!.add("-E-")
            return res
        }

        /** Safe state requires microchips to either have no generators on their floor, or have their own generator. */
        fun isSafe() = types.all {
            val floor = microchips["${it}M"]!!
            val gensOnFloor = generators.filterValues { it == floor }.keys
            gensOnFloor.isEmpty() || "${it}G" in gensOnFloor
        }

        /** Possible moves from this state - elevator can take 1-2 items on its floor to one floor away. */
        fun moves(): List<State> {
            val floor = elevator
            val floorUp = FLOORS.getOrNull(FLOORS.indexOf(floor) + 1)
            val floorDown = FLOORS.getOrNull(FLOORS.indexOf(floor) - 1)
            val itemsOnFloor = (generators.keys + microchips.keys).filter { (generators[it] == floor) || (microchips[it] == floor) }
            val moveOneSets = itemsOnFloor.map { setOf(it) }
            val moveTwoSets = itemsOnFloor.pairwise()

            val moves = listOfNotNull(floorUp, floorDown).flatMap { newFloor ->
                (moveOneSets + moveTwoSets).map { whatToMove ->
                    State(
                        generators = generators + whatToMove.filter { it[1] == 'G' }.associateWith { newFloor },
                        microchips = microchips + whatToMove.filter { it[1] == 'M' }.associateWith { newFloor },
                        elevator = newFloor
                    )
                }
            }
            return moves.filter { it.isSafe() }
        }

        /** Final target state from this one. */
        fun finalState() = State(
            generators = generators.keys.associateWith { "F4" },
            microchips = microchips.keys.associateWith { "F4" },
            elevator = "F4"
        )

        /** Heuristic for number of steps to complete. */
        fun h(): Int {
            return (types.withIndex().sumOf { (i, gen) ->
                2 * (4 - generators["${gen}G"]!!.drop(1).toInt()) * (i + 1) +
                (4 - microchips["${gen}M"]!!.drop(1).toInt()) * (i + 1)
            } + 3) / 4
        }

        /** Print state. */
        fun print() {
            println("${"*".repeat(20)}  heuristic = ${h()}")
            val allItems = ((generators.keys + microchips.keys) + "-E-").sorted()
            FLOORS.reversed().forEach { floor ->
                print("$floor ")
                allItems.forEach { item ->
                    val itemFloor = generators[item] ?: microchips[item] ?: elevator
                    print("%3s".format(if (itemFloor == floor) item else "."))
                }
                println()
            }
        }
    }

    override fun calc1(input: List<String>): Int {
        println("-".repeat(40))
        val state = input.parse()
        if (!state.isSafe()) println("Initial state not safe: $state")
        println("Finding paths for part 1...")
//        val pathA = Pathfinder.shortestPath(state, state.finalState()) { it.moves() }
//        println("  cost of path A: ${pathA.size - 1}")
        val (pathB, cost) = AstarSearch.toFinish(state, state.finalState(), { it.moves().toSet() }) { it.h() }
            .minimizeCost()
        println("  cost of path B: $cost")
//        println(if (pathA == pathB) "  Paths are identical" else "  Paths differ")
        println("-".repeat(40))
        pathB.forEach {
            it.print()
        }
        println("-".repeat(40))
        return cost
    }

    override fun calc2(input: List<String>): Int {
        val state = input.parse2()
        if (!state.isSafe()) {
            println("Initial state not safe: $state")
            return -1
        }
//        val path = Pathfinder.shortestPath(state, state.finalState()) { it.moves() }
//        return path.size - 1
        val (path, cost) = AstarSearch.toFinish(state, state.finalState(), { it.moves().toSet() }) { it.h() }
            .minimizeCost()
        // result is not 73
        return cost
    }
}