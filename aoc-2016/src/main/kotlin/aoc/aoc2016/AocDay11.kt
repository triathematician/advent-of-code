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
            generators = part1.generators + mapOf("EG" to "F1"),//, "DG" to "F1"),
            microchips = part1.microchips + mapOf("EM" to "F1"),//, "DM" to "F1")
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
        val types = generators.keys.map { it.first() }

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
            val floorSum = generators.values.map { it.drop(1).toInt() } +
                    microchips.values.map { it.drop(1).toInt() }
            return floorSum.sumOf { 4 - it } / 2
        }
    }

    override fun calc1(input: List<String>): Int {
        val state = input.parse()
        if (!state.isSafe()) println("Initial state not safe: $state")
//        val path = Pathfinder.shortestPath(state, state.finalState()) { it.moves() }
//        return path.size - 1
        val (path, cost) = AstarSearch.toFinish(state, state.finalState(), { it.moves().toSet() }) { it.h() }
            .minimizeCost()
        return cost
    }

    override fun calc2(input: List<String>): Int {
        val state = input.parse2()
        if (!state.isSafe()) {
            println("Initial state not safe: $state")
            return -1
        }
        val path = Pathfinder.shortestPath(state, state.finalState()) { it.moves() }
        return path.size - 1
//        val (path, cost) = AstarSearch.toFinish(state, state.finalState(), { it.moves().toSet() }) { it.h() }
//            .minimizeCost()
//        return cost
    }
}