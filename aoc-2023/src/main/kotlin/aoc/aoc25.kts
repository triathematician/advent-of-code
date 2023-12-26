import aoc.AocParser.Companion.parselines
import aoc.*
import aoc.util.getDayInput
import aoc.util.pairwise
import aoc.util.second
import aoc.util.triples

val testInput = """
jqt: rhn xhk nvd
rsh: frs pzl lsr
xhk: hfx
cmg: qnr nvd lhk bvb
rhn: xhk bvb hfx
bvb: xhk hfx
pzl: lsr hfx nvd
qnr: nvd
ntq: jqt hfx bvb xhk
nvd: lhk
lsr: lhk
rzs: qnr cmg lsr rsh
frs: qnr lhk lsr
""".parselines

fun Map<String, Set<String>>.symmetric(): Map<String, Set<String>> {
    val sym = mutableMapOf<String, MutableSet<String>>()
    forEach { (k, v) ->
        sym.getOrPut(k) { mutableSetOf() }.addAll(v)
        v.forEach { vv ->
            sym.getOrPut(vv) { mutableSetOf() }.add(k)
        }
    }
    return sym
}

class Graph(val links: Map<String, Set<String>>) {
    val vertices
        get() = links.keys + links.values.flatten().toSet()
    val wires
        get() = links.entries.flatMap { (k, v) -> v.map { setOf(k, it) } }.toSet()

    fun neighborhood(v: String, dist: Int, disallow: String? = null): Set<String> {
        if (dist == 0)
            return setOf(v)
        val adj = links[v]!! - setOfNotNull(disallow)
        return adj + adj.flatMap { neighborhood(it, dist - 1, disallow) }
    }

    fun components(): Set<Set<String>> {
        var comps = vertices.map { setOf(it) }.toSet()
        links.forEach { (v, adj) ->
            val joinComps = comps.filter { it.contains(v) || it.intersect(adj).isNotEmpty() }.toSet()
            val joined = joinComps.flatten().toSet()
            comps = (comps - joinComps) + setOf(joined)
        }
        return comps.toSet()
    }

    fun cutWires(wires: Set<Set<String>>): Graph {
        val newLinks = links.mapValues { (v, adj) ->
            adj - wires.filter { v in it }.flatten().toSet()
        }
        return Graph(newLinks)
    }

    fun triangles(): Set<Set<String>> {
        return vertices.flatMap { v ->
            links[v]!!.toList().pairwise().filter { it.first() in links[it.second()]!! }.map {
                setOf(v, it.first(), it.second())
            }
        }.toSet()
    }
}

// part 1

fun String.parse() = substringBefore(":") to substringAfter(":").trim().split(" ").map { it.trim() }.toSet()

var i = 0

fun List<String>.part1(): Int {
    val links = associate { it.parse() }.symmetric()
    val graph = Graph(links)

    // do some stats with neighborhood sizes to see if something stands out
//    (2..5).forEach { dist ->
//        val nbhds = graph.vertices.associateWith { graph.neighborhood(it, dist) }
//        println("Vertex neighborhoods of distance $dist: " + nbhds.mapValues { it.value.size }.entries.sortedByDescending { it.value })
//    }
    // do some stats with wire neighborhoods (both ends) to see if something stands out
//    (2..5).forEach { dist ->
//        val nbhds = graph.wires.associateWith { graph.neighborhood(it.first(), dist) + graph.neighborhood(it.second(), dist) }
//        println("Wire neighborhoods of distance $dist: " + nbhds.mapValues { it.value.size }.entries.sortedByDescending { it.value })
//    }
    // do some stats with wire neighborhoods (intersection of nbhds if we remove the wire) to see if something stands out
    (2..4).forEach { dist ->
        val nbhds = graph.wires.associateWith {
            val n1 = graph.neighborhood(it.first(), dist, disallow = it.second())
            val n2 = graph.neighborhood(it.second(), dist, disallow = it.first())
            n1.intersect(n2)
        }
        println("Wire intersecting neighborhoods of distance $dist: " + nbhds.mapValues { it.value.size }.entries.sortedBy { it.value }.take(50) + "...")
    }

    val wiresBySize = graph.wires.associateWith {
        val n1 = graph.neighborhood(it.first(), dist = 4, disallow = it.second())
        val n2 = graph.neighborhood(it.second(), dist = 4, disallow = it.first())
        n1.intersect(n2)
    }.entries.sortedBy { it.value.size }.map { it.key }
    var indexSum = 3
    var check = 0
    while (true) {
        (0..minOf(indexSum, wiresBySize.size - 1)).forEach { i1 ->
            val wire1 = wiresBySize[i1]
            val graph1 = graph.cutWires(setOf(wire1))
            (i1+1..minOf(indexSum, wiresBySize.size - 1)).forEach { i2 ->
                val i3 = indexSum - i1 - i2
                if (i3 > i2 && i3 < wiresBySize.size) {
                    val wire2 = wiresBySize[i2]
                    val wire3 = wiresBySize[i3]
                    val toCut = setOf(wire1, wire2, wire3)
                    if (toCut.flatten().size == 6) {
                        check++
                        val redGraph = graph1.cutWires(setOf(wire2, wire3))
                        val comps = redGraph.components()
                        if (comps.size == 2) {
                            println("After $check iterations, cutting $toCut leaves ${comps.size} components")
                            if (comps.first().size < 100) {
                                println("  First component: ${comps.first()}")
                                println("  Second component: ${comps.second()}")
                            }
                            return comps.first().size * comps.second().size
                        }
                    }
                }
            }
        }
        indexSum++
    }
}

fun List<String>.part1b(): Int {
    val links = associate { it.parse() }.symmetric()
    val graph = Graph(links)
    graph.wires.toList().triples().filter {
        it.flatten().toSet().size == 6
    }.forEach {
        i++
        val redGraph = graph.cutWires(it)
        val comps = redGraph.components()
        if (comps.size == 2) {
            println("Cutting $it leaves ${comps.size} components: $comps")
            return comps.first().size * comps.second().size
        }
    }
    return -1
}

// part 2

fun List<String>.part2() = "Merry Christmas!"

// calculate answers

val day = 25
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
