import aoc.AocParser.Companion.parse
import aoc.AocRunner
import aoc.ints
import aoc.print
import java.lang.IllegalArgumentException

val day = 19

val testInput = """
Blueprint 1: Each ore robot costs 4 ore. Each clay robot costs 2 ore. Each obsidian robot costs 3 ore and 14 clay. Each geode robot costs 2 ore and 7 obsidian.
Blueprint 2: Each ore robot costs 2 ore. Each clay robot costs 3 ore. Each obsidian robot costs 3 ore and 8 clay. Each geode robot costs 3 ore and 12 obsidian.
""".parse.mapLines { it.replace(":"," ").ints(" ").let { Blueprint(it[0], it[1], it[2], it[3], it[4], it[5], it[6]) } }

val input = """
Blueprint 1: Each ore robot costs 3 ore. Each clay robot costs 4 ore. Each obsidian robot costs 4 ore and 18 clay. Each geode robot costs 3 ore and 8 obsidian.
Blueprint 2: Each ore robot costs 4 ore. Each clay robot costs 4 ore. Each obsidian robot costs 2 ore and 18 clay. Each geode robot costs 4 ore and 20 obsidian.
Blueprint 3: Each ore robot costs 3 ore. Each clay robot costs 4 ore. Each obsidian robot costs 4 ore and 18 clay. Each geode robot costs 4 ore and 12 obsidian.
Blueprint 4: Each ore robot costs 4 ore. Each clay robot costs 4 ore. Each obsidian robot costs 4 ore and 20 clay. Each geode robot costs 2 ore and 8 obsidian.
Blueprint 5: Each ore robot costs 2 ore. Each clay robot costs 3 ore. Each obsidian robot costs 2 ore and 14 clay. Each geode robot costs 3 ore and 8 obsidian.
Blueprint 6: Each ore robot costs 3 ore. Each clay robot costs 3 ore. Each obsidian robot costs 3 ore and 17 clay. Each geode robot costs 4 ore and 8 obsidian.
Blueprint 7: Each ore robot costs 4 ore. Each clay robot costs 3 ore. Each obsidian robot costs 4 ore and 6 clay. Each geode robot costs 3 ore and 11 obsidian.
Blueprint 8: Each ore robot costs 2 ore. Each clay robot costs 4 ore. Each obsidian robot costs 4 ore and 20 clay. Each geode robot costs 4 ore and 18 obsidian.
Blueprint 9: Each ore robot costs 3 ore. Each clay robot costs 4 ore. Each obsidian robot costs 4 ore and 16 clay. Each geode robot costs 3 ore and 15 obsidian.
Blueprint 10: Each ore robot costs 4 ore. Each clay robot costs 4 ore. Each obsidian robot costs 3 ore and 7 clay. Each geode robot costs 4 ore and 11 obsidian.
Blueprint 11: Each ore robot costs 4 ore. Each clay robot costs 4 ore. Each obsidian robot costs 2 ore and 9 clay. Each geode robot costs 3 ore and 19 obsidian.
Blueprint 12: Each ore robot costs 4 ore. Each clay robot costs 3 ore. Each obsidian robot costs 2 ore and 17 clay. Each geode robot costs 3 ore and 16 obsidian.
Blueprint 13: Each ore robot costs 3 ore. Each clay robot costs 4 ore. Each obsidian robot costs 4 ore and 5 clay. Each geode robot costs 3 ore and 12 obsidian.
Blueprint 14: Each ore robot costs 3 ore. Each clay robot costs 3 ore. Each obsidian robot costs 3 ore and 8 clay. Each geode robot costs 2 ore and 12 obsidian.
Blueprint 15: Each ore robot costs 4 ore. Each clay robot costs 4 ore. Each obsidian robot costs 3 ore and 7 clay. Each geode robot costs 3 ore and 20 obsidian.
Blueprint 16: Each ore robot costs 3 ore. Each clay robot costs 3 ore. Each obsidian robot costs 2 ore and 11 clay. Each geode robot costs 2 ore and 19 obsidian.
Blueprint 17: Each ore robot costs 4 ore. Each clay robot costs 4 ore. Each obsidian robot costs 2 ore and 10 clay. Each geode robot costs 3 ore and 14 obsidian.
Blueprint 18: Each ore robot costs 3 ore. Each clay robot costs 4 ore. Each obsidian robot costs 3 ore and 12 clay. Each geode robot costs 3 ore and 17 obsidian.
Blueprint 19: Each ore robot costs 4 ore. Each clay robot costs 4 ore. Each obsidian robot costs 4 ore and 7 clay. Each geode robot costs 2 ore and 19 obsidian.
Blueprint 20: Each ore robot costs 2 ore. Each clay robot costs 3 ore. Each obsidian robot costs 2 ore and 17 clay. Each geode robot costs 3 ore and 19 obsidian.
Blueprint 21: Each ore robot costs 4 ore. Each clay robot costs 3 ore. Each obsidian robot costs 2 ore and 14 clay. Each geode robot costs 4 ore and 11 obsidian.
Blueprint 22: Each ore robot costs 4 ore. Each clay robot costs 3 ore. Each obsidian robot costs 4 ore and 15 clay. Each geode robot costs 4 ore and 9 obsidian.
Blueprint 23: Each ore robot costs 4 ore. Each clay robot costs 4 ore. Each obsidian robot costs 3 ore and 9 clay. Each geode robot costs 3 ore and 7 obsidian.
Blueprint 24: Each ore robot costs 4 ore. Each clay robot costs 4 ore. Each obsidian robot costs 4 ore and 15 clay. Each geode robot costs 4 ore and 17 obsidian.
Blueprint 25: Each ore robot costs 4 ore. Each clay robot costs 4 ore. Each obsidian robot costs 4 ore and 9 clay. Each geode robot costs 2 ore and 20 obsidian.
Blueprint 26: Each ore robot costs 3 ore. Each clay robot costs 4 ore. Each obsidian robot costs 3 ore and 20 clay. Each geode robot costs 3 ore and 14 obsidian.
Blueprint 27: Each ore robot costs 2 ore. Each clay robot costs 4 ore. Each obsidian robot costs 2 ore and 15 clay. Each geode robot costs 3 ore and 16 obsidian.
Blueprint 28: Each ore robot costs 2 ore. Each clay robot costs 3 ore. Each obsidian robot costs 3 ore and 8 clay. Each geode robot costs 3 ore and 20 obsidian.
Blueprint 29: Each ore robot costs 3 ore. Each clay robot costs 4 ore. Each obsidian robot costs 2 ore and 14 clay. Each geode robot costs 3 ore and 14 obsidian.
Blueprint 30: Each ore robot costs 4 ore. Each clay robot costs 3 ore. Each obsidian robot costs 4 ore and 18 clay. Each geode robot costs 3 ore and 13 obsidian.
""".parse.mapLines { it.replace(":"," ").ints(" ").let { Blueprint(it[0], it[1], it[2], it[3], it[4], it[5], it[6]) } }

class Blueprint(val num: Int, val oreOreCost: Int, val clayOreCost: Int, val obsOreCost: Int, val obsClayCost: Int, val geoOreCost: Int, val geoObsCost: Int)

val ORE = 1
val CLAY = 2
val OBS = 3
val GEO = 4

data class Robots(
    val blueprint: Blueprint,
    val bots: List<Int> = listOf(1, 0, 0, 0),
    val ores: MutableList<Int> = mutableListOf(0, 0, 0, 0),
    val path: List<Int> = listOf()
) {

    val geodes: Int
        get() = ores[3]
    val rockScore: Int
        get() = ores[0] + 2*ores[1] + 4*ores[2] + 8*ores[3]

    override fun toString() = "["+bots.joinToString(":")+"] " + ores.joinToString(":")

    fun legalMove(rock: Int) = when(rock) {
        0 -> true
        ORE -> ores[0] >= blueprint.oreOreCost && bots[0] < 4 // fix no more than 4 ore bots
        CLAY -> ores[0] >= blueprint.clayOreCost
        OBS -> ores[0] >= blueprint.obsOreCost && ores[1] >= blueprint.obsClayCost
        GEO -> ores[0] >= blueprint.geoOreCost && ores[2] >= blueprint.geoObsCost
        else -> TODO()
    }

    fun applyMove(m: Int): Robots {
        if (m != 0 && !legalMove(m))
            throw IllegalArgumentException("Cannot create $m. $this")
        return copy(
            path = path + m,
            bots = if (m == 0) bots.toList() else bots.toMutableList().also { it[m-1]++ },
            ores = (1..4).map {
                val base = ores[it-1] + bots[it-1]
                when {
                    m == ORE && it == ORE -> base - blueprint.oreOreCost

                    m == CLAY && it == ORE -> base - blueprint.clayOreCost

                    m == OBS && it == ORE -> base - blueprint.obsOreCost
                    m == OBS && it == CLAY -> base - blueprint.obsClayCost

                    m == GEO && it == ORE -> base - blueprint.geoOreCost
                    m == GEO && it == OBS -> base - blueprint.geoObsCost
                    else -> base
                }
            }.toMutableList()
        )
    }

    // test if this state is strictly better than another state: all ores better, all robots better
    fun strictlyBetterThan(other: Robots): Boolean {
        if (this == other || (ores == other.ores && bots == other.bots))
            return false

        // heuristic - may need to adjust for 32
        if (ores[2] > other.ores[2] + 1 && bots[2] > other.bots[2] + 1
            && ores[3] >= other.ores[3] && bots[3] >= other.bots[3])
            return true

        if ((0..3).any { ores[it] < other.ores[it] || bots[it] < other.bots[it] })
            return false

        return true
    }
}

fun Blueprint.geodes(time: Int): Int {
    println("Blueprint $num...")
    val res = listOf(Robots(this)).allTheGeodes(time)[0]
    println("...best # of geodes=${res.geodes} (Blueprint $num) path=${res.path.joinToString("")}")
    return res.geodes
}

fun List<Robots>.allTheGeodes(timeLeft: Int): List<Robots> {
    if (timeLeft == 0)
        return listOf(maxBy { it.geodes })

    val nextLevel = flatMap { r ->
        (0..4).filter { r.legalMove(it) }.map { r.applyMove(it) }
    }

    return if (timeLeft == 1)
        listOf(nextLevel.maxBy { it.geodes })
    else
        nextLevel.prune(timeLeft).allTheGeodes(timeLeft - 1)
}

fun List<Robots>.prune(time: Int): List<Robots> {
    if (size <= 10)
        return this
    print("  . t=$time, $size paths")

    val maxGeo = maxOf { it.geodes + it.bots[3]*time }
    val remove = filter { k ->
        (k.geodes + k.bots[3]*time + bestCaseGeodes(time) < maxGeo) || any { it.strictlyBetterThan(k) }
    }
    println(", pruned ${remove.size}")
    return (this - remove.toSet())
}

fun bestCaseGeodes(t: Int) = (t*(t-1))/2

// test case

//println("---")
//var r = Robots(testInput[0])
//listOf(0,0,2,0,2,0,2,0,0,0,3,2,0,0,3,0,0,4,0,0,4,0,0,0).forEach {
//    r = r.applyMove(it)
//    println(r)
//}
//println("---")
//r = Robots(testInput[1])
//listOf(0,0,1,0,1,2,2,2,2,2,3,2,3,3,3,2,3,4,3,4,3,4,3,4,0).forEach {
//    r = r.applyMove(it)
//    println("     " + ALL_MOVES.filter { r.legalMoveSequence(it) })
//    println("$it >> $r")
//}

println("---")
val testResult = testInput.map { it.geodes(24) }.also {
    println("Max geodes: $it")
}.mapIndexed { i, it -> it * (i+1) }.sum()

println("---")
val testResult2 = testInput.map { it.geodes(32) }.also {
    println("Max geodes (2): $it")
}.reduce { a, b -> a * b }

// part 1

println("---")
val answer1 = input.map { it.geodes(24) }.also {
    println("Max geodes: $it")
}.mapIndexed { i, it -> it * (i+1) }.sum()
answer1.print

// part 2

println("---")
//val answer2 = 0
val answer2 = input.take(3).map { it.geodes(32) }.also {
    println("Max geodes (2): $it")
}.reduce { a, b -> a * b }
answer2.print

// print results

AocRunner(day,
    info = { listOf("Leaderboard: 48:27/57:45", "Answers: 960/2040") },
    test = { "$testResult, $testResult2" },
    part1 = { answer1 },
    part2 = { answer2 }
).run()
