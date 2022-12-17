import aoc.AocParser.Companion.parselines
import aoc.AocRunner
import aoc.print

val day = 16

val testInput = """
Valve AA has flow rate=0; tunnels lead to valves DD, II, BB
Valve BB has flow rate=13; tunnels lead to valves CC, AA
Valve CC has flow rate=2; tunnels lead to valves DD, BB
Valve DD has flow rate=20; tunnels lead to valves CC, AA, EE
Valve EE has flow rate=3; tunnels lead to valves FF, DD
Valve FF has flow rate=0; tunnels lead to valves EE, GG
Valve GG has flow rate=0; tunnels lead to valves FF, HH
Valve HH has flow rate=22; tunnel leads to valve GG
Valve II has flow rate=0; tunnels lead to valves AA, JJ
Valve JJ has flow rate=21; tunnel leads to valve II
""".parselines.parseValves()

val input = """
Valve QE has flow rate=3; tunnels lead to valves OU, ME, UX, AX, TW
Valve TN has flow rate=16; tunnels lead to valves UW, CG, WB
Valve UX has flow rate=0; tunnels lead to valves AA, QE
Valve HK has flow rate=5; tunnels lead to valves HT, QU, TW, WV, OK
Valve SK has flow rate=14; tunnels lead to valves GH, GA, XM
Valve HY has flow rate=0; tunnels lead to valves LG, AA
Valve BK has flow rate=0; tunnels lead to valves SZ, AA
Valve BY has flow rate=11; tunnels lead to valves SP, HS, DN, KD, TK
Valve GR has flow rate=0; tunnels lead to valves FE, OK
Valve OH has flow rate=0; tunnels lead to valves BM, KE
Valve DC has flow rate=0; tunnels lead to valves AX, XH
Valve YS has flow rate=0; tunnels lead to valves XH, EU
Valve KP has flow rate=0; tunnels lead to valves KI, OF
Valve LG has flow rate=0; tunnels lead to valves FE, HY
Valve FE has flow rate=4; tunnels lead to valves RU, GR, YI, LG, ME
Valve NK has flow rate=0; tunnels lead to valves SD, BM
Valve EU has flow rate=0; tunnels lead to valves NS, YS
Valve OF has flow rate=0; tunnels lead to valves CJ, KP
Valve TW has flow rate=0; tunnels lead to valves HK, QE
Valve GL has flow rate=0; tunnels lead to valves AF, CQ
Valve OU has flow rate=0; tunnels lead to valves KN, QE
Valve BM has flow rate=24; tunnels lead to valves GH, NK, YH, OH
Valve GA has flow rate=0; tunnels lead to valves SK, SZ
Valve EI has flow rate=17; tunnels lead to valves ZX, AF
Valve QN has flow rate=25; tunnel leads to valve SD
Valve ZX has flow rate=0; tunnels lead to valves EI, WB
Valve ME has flow rate=0; tunnels lead to valves QE, FE
Valve CJ has flow rate=21; tunnels lead to valves OF, YI, KD
Valve AX has flow rate=0; tunnels lead to valves DC, QE
Valve LW has flow rate=0; tunnels lead to valves AA, HT
Valve CQ has flow rate=18; tunnels lead to valves GL, XM
Valve KN has flow rate=0; tunnels lead to valves SZ, OU
Valve HS has flow rate=0; tunnels lead to valves UZ, BY
Valve RU has flow rate=0; tunnels lead to valves TK, FE
Valve SZ has flow rate=6; tunnels lead to valves WV, GA, BK, KE, KN
Valve AF has flow rate=0; tunnels lead to valves GL, EI
Valve YI has flow rate=0; tunnels lead to valves FE, CJ
Valve HT has flow rate=0; tunnels lead to valves LW, HK
Valve WV has flow rate=0; tunnels lead to valves SZ, HK
Valve TK has flow rate=0; tunnels lead to valves BY, RU
Valve GH has flow rate=0; tunnels lead to valves BM, SK
Valve CG has flow rate=0; tunnels lead to valves TN, SP
Valve AA has flow rate=0; tunnels lead to valves HY, UX, VQ, LW, BK
Valve SP has flow rate=0; tunnels lead to valves BY, CG
Valve XM has flow rate=0; tunnels lead to valves SK, CQ
Valve DN has flow rate=0; tunnels lead to valves NS, BY
Valve XH has flow rate=22; tunnels lead to valves YS, QU, UZ, DC
Valve KI has flow rate=20; tunnels lead to valves UW, KP
Valve OK has flow rate=0; tunnels lead to valves HK, GR
Valve YH has flow rate=0; tunnels lead to valves VQ, BM
Valve UZ has flow rate=0; tunnels lead to valves XH, HS
Valve KE has flow rate=0; tunnels lead to valves OH, SZ
Valve VQ has flow rate=0; tunnels lead to valves AA, YH
Valve QU has flow rate=0; tunnels lead to valves HK, XH
Valve WB has flow rate=0; tunnels lead to valves TN, ZX
Valve UW has flow rate=0; tunnels lead to valves KI, TN
Valve SD has flow rate=0; tunnels lead to valves NK, QN
Valve NS has flow rate=23; tunnels lead to valves EU, DN
Valve KD has flow rate=0; tunnels lead to valves BY, CJ
""".parselines.parseValves()

fun List<String>.parseValves(): Map<String,Valve> {
    val res = mutableMapOf<String, Valve>()
    forEach {
        val spl = it.split(' ', '=', ';')
        val name = spl[1]
        val rate = spl[5].toInt()
        val valves = it.substringAfter("valves").substringAfter("valve").split(",").map { it.trim() }
        val v = res.getOrPut(name) { Valve(name) }
        v.flowRate = rate
        v.adj = (valves.map { res.getOrPut(it) { Valve(it) } }).map { it to 1 }.toMap()
    }
    println(res.values)
    return res
}

class Valve(val name: String, var flowRate: Int = 0, var adj: Map<Valve, Int> = mutableMapOf()) {
    override fun toString() = "$name (rate=$flowRate) leads to ${adj.map {it.key.name}}"

    // min distance to target, which should have nonzero flow rate. must travel only through intermediate flowrate 0 nodes
    fun distanceTo(v2: Valve, visited: Set<Valve>): Int? {
        if (v2 in adj) return adj[v2]
        val newVisited = visited + this
        return adj.keys
            .filter { it !in visited }
            .mapNotNull { it.distanceTo(v2, newVisited) }
            .minOrNull()?.let { it + 1 }
    }
}

fun buildValveGraph(input: List<Valve>): Map<Valve, Map<Valve, Int>> {
    val pos = input.filter { it.flowRate > 0 }
    val distances = mutableMapOf<Valve, MutableMap<Valve, Int>>()
    pos.forEach { distances[it] = mutableMapOf() }
    pos.map { v1 ->
        (pos - v1).map { v2 ->
            v1.distanceTo(v2, setOf())?.let {
                distances[v1]!![v2] = it
            }
        }
    }
    return distances
}

fun optimalRelease(start: Map<Valve, Int>, maxTime: Int, print: Boolean): Int {
    val paths = start.map { bestPathFrom(ValvePath(it.key, time = it.value + 1), maxTime, print, setOf()) }
    val best = paths.maxBy { it.pressureReleased(maxTime) }
    return best.let {
        println(it)
        it.printRelease(maxTime)
        it.pressureReleased(maxTime)
    }
}

fun optimalReleaseTwice(start: Map<Valve, Int>, maxTime: Int, print: Boolean): Int {
    val subsets: List<Set<Valve>> = start.keys.toList().subsetsAtLeastOne().filter { it.size in 1 until start.keys.size }.map { it.toSet() }
    println("Checking ${subsets.size} subsets")
    val dualPaths = subsets.mapIndexed { i, set ->
        if (i % 50 == 0) println("..$i")
        val yourPath = start.filterKeys { k -> k in set }
            .map { bestPathFrom(ValvePath(it.key, time = it.value + 1), maxTime, print, restrictTo = set) }
            .maxBy { it.pressureReleased(maxTime) }
        val elePath = (start - set)
            .map { bestPathFrom(ValvePath(it.key, time = it.value + 1), maxTime, print, restrictTo = start.keys - set) }
            .maxBy { it.pressureReleased(maxTime) }
        yourPath to elePath
    }
    val best = dualPaths.maxBy { it.first.pressureReleased(maxTime) + it.second.pressureReleased(maxTime) }
    return best.let {
        println("Your path: " + it.first)
        println("Elephant path: " + it.second)
        it.first.pressureReleased(maxTime) + it.second.pressureReleased(maxTime)
    }
}

fun <X> List<X>.subsetsAtLeastOne(): List<List<X>> {
    return if (size == 2)
        listOf(listOf(), listOf(get(0)), listOf(get(1)), this)
    else {
        val first = get(0)
        drop(1).subsetsAtLeastOne().flatMap { listOf(it, it + first) }
    }
}


fun bestPathFrom(subpath: ValvePath, maxTime: Int, print: Boolean = false, restrictTo: Set<Valve>): ValvePath {
    with (subpath) {
        if (print) println(" ".repeat(time*2)+"Options from $subpath")

        val options = mutableListOf<ValvePath>()
        if (time <= maxTime && at !in openAt.keys) {
            if (print) println(" ".repeat(time*2)+"-open valve ${at.name}")
            options += openValve()
        } else if (time <= maxTime) {
            at.adj.keys
                .filter { restrictTo.isEmpty() || it in restrictTo }
                .filter { subpath.canTravelTo(it) }
                .forEach {
                    if (print) println(" ".repeat(time*2)+"-travel to ${it.name}")
                    options += travelTo(it)
                }
        }

        if (options.isEmpty()) {
            if (print) println(" ".repeat(time*2)+"Nowhere to go: $subpath with pressure ${subpath.pressureReleased(maxTime)}")
            return subpath
        } else {
            if (print) println(" ".repeat(time*2)+"-building paths for these options")
            val res = options.map { bestPathFrom(it, maxTime, print, restrictTo) }
            return res.maxBy { it.pressureReleased(maxTime) }
        }
    }
}

class ValvePath(
    val at: Valve,
    val path: List<Valve>,
    val times: List<Int>,
    val openAt: Map<Valve, Int>, // always open when first arriving to the valve
    val time: Int
) {
    constructor(at: Valve, time: Int): this(at, listOf(at), listOf(1), mapOf(), time)

    override fun toString() = path.mapIndexed { i, v ->
        if (v in openAt.keys && path.indexOf(v) == i) "(${v.name})" else v.name
    }.joinToString("-")

    fun openValve() = ValvePath(at, path, times, (openAt.map { it.key to it.value } + (at to time)).toMap(), time + 1)

    // only worry about valves that haven't been opened
    fun canTravelTo(other: Valve) = openAt[other] == null

    fun travelTo(it: Valve) = ValvePath(it, path + it, times + time, openAt, time + at.adj[it]!!)

    fun pressureReleased(maxTime: Int): Int {
        return openAt.entries.sumOf { (v, t) ->
            (maxTime - t) * v.flowRate
        }
    }

    fun printRelease(maxTime: Int) {
        openAt.entries.forEach { (v, t) ->
            val total = (maxTime - t) * v.flowRate
            println(" - ${v.name} opened at time $t, releasing a total of $total across ${maxTime-t} steps")
        }
    }
}

// test case

println("---")
val testDists = buildValveGraph(testInput.values.toList())
testDists.forEach {
    println("${it.key.name}: " + it.value.entries.joinToString(", ") { it.key.name+"="+it.value})
}

// update to a new valve structure, with reduced set of mappings
val revisedInput = testDists.map { (it, _) -> Valve(it.name, it.flowRate) }.associateBy { it.name }
testDists.forEach { (it, value) ->
    revisedInput[it.name]!!.adj = value.mapKeys { revisedInput[it.key.name]!! }.toMap()
}

println("---")
println(revisedInput)
val start = testInput["AA"]!!
val startPos = revisedInput.values.associateWith { v ->
    start.distanceTo(testInput[v.name]!!, setOf())!!
}
println("---")
val testResult = optimalRelease(startPos, 30, false)
val testResult2 = optimalReleaseTwice(startPos, 26, false)
println("---")

// part 1

println("---")
val dists = buildValveGraph(input.values.toList())
dists.forEach {
    println("${it.key.name}: " + it.value.entries.joinToString(", ") { it.key.name+"="+it.value})
}

// update to a new valve structure, with reduced set of mappings
val input2 = dists.map { (it, _) -> Valve(it.name, it.flowRate) }.associateBy { it.name }
dists.forEach { (it, value) ->
    input2[it.name]!!.adj = value.mapKeys { input2[it.key.name]!! }.toMap()
}

println("---")
println(input2)
val start2 = input["AA"]!!
val startPos2 = input2.values.associateWith { v ->
    start2.distanceTo(input[v.name]!!, setOf())!!
}
println("---")
val answer1 = optimalRelease(startPos2, 30, false)

// part 2

val answer2 = optimalReleaseTwice(startPos2, 26, false)

// print results

AocRunner(day,
    info = { listOf("Leaderboard: 23:53/64:17", "Answers: 2077/2741") },
    test = { "$testResult, $testResult2" },
    part1 = { answer1 },
    part2 = { answer2 }
).run()
