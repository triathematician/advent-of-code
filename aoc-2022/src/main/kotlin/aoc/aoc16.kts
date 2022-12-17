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
        v.pressure = rate
        v.others.addAll(valves.map { res.getOrPut(it) { Valve(it) } })
    }
    println(res.values)
    return res
}

class Valve(val name: String, var pressure: Int = 0, val others: MutableList<Valve> = mutableListOf()) {
    override fun toString() = "$name (rate=$pressure) leads to ${others.map {it.name}}"
}

fun Map<String,Valve>.optimalRelease(start: Valve, maxTime: Int): Int {
    val paths = bestPathFrom(ValvePath(start), maxTime)
    return paths.let {
        println(it)
        it.printRelease(maxTime)
        it.pressureReleased(maxTime)
    }
}

fun Map<String,Valve>.bestPathFrom(subpath: ValvePath, maxTime: Int, print: Boolean = false): ValvePath {
    with (subpath) {
        if (print) println(" ".repeat(time)+"Options from $subpath")
        val options = mutableListOf<ValvePath>()
        if (time <= maxTime && at.pressure > 0 && at !in openAt.keys) {
            if (print) println(" ".repeat(time)+"-open valve ${at.name}")
            options += openValve()
        }
        if (time <= maxTime) {
            at.others.filter { subpath.shouldTravelTo(it) }.forEach {
                if (print) println(" ".repeat(time)+"-travel to ${it.name}")
                options += travelTo(it)
            }
        }
        if (options.isEmpty()) {
            if (print) println(" ".repeat(time)+"Nowhere to go: $subpath with pressure ${subpath.pressureReleased(maxTime)}")
            return subpath
        } else {
            if (print) println(" ".repeat(time)+"Building paths: $subpath")
            val res = options.map { bestPathFrom(it, maxTime) }
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
    constructor(at: Valve): this(at, listOf(at), listOf(1), mapOf(), 1)

    override fun toString() = path.mapIndexed { i, v ->
        if (v in openAt.keys && path.indexOf(v) == i) "(${v.name})" else v.name
    }.joinToString("-")

    fun openValve() = ValvePath(at, path, times, (openAt.map { it.key to it.value } + (at to time)).toMap(), time + 1)

    fun shouldTravelTo(other: Valve): Boolean {
        if (other !in path) return true
        val lastSeen = path.lastIndexOf(other)
        val timeLastSeen = times[lastSeen]
        val loop = path.subList(lastSeen, path.size)
        // must have opened something in the loop for this to be relevant
        return loop.any {
            openAt[it]?.let { it > timeLastSeen } ?: false
        }
    }

    fun travelTo(it: Valve): ValvePath {
        val path = ValvePath(it, path + it, times + time, openAt, time + 1)
        return if (it.pressure == 0 && it.others.size == 2) {
            path.travelTo((it.others - at).first())
        } else {
            path
        }
    }

    fun pressureReleased(maxTime: Int): Int {
        return openAt.entries.sumOf { (v, t) ->
            (maxTime - t) * v.pressure
        }
    }

    fun printRelease(maxTime: Int) {
        openAt.entries.forEach { (v, t) ->
            val total = (maxTime - t) * v.pressure
            println(" - ${v.name} opened at time $t, releasing a total of $total across ${maxTime-t} steps")
        }
    }
}

// test case

val testResult = testInput.optimalRelease(testInput["AA"]!!, 30)
val testResult2 = 0

// part 1

val answer1 = input.optimalRelease(input["AA"]!!, 30)
answer1.print

// part 2

val answer2 = 0
answer2.print

// print results

AocRunner(day,
    info = { listOf("Leaderboard: 2077/", "Answers: ") },
    test = { "$testResult, $testResult2" },
    part1 = { answer1 },
    part2 = { answer2 }
).run()
