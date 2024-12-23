import aoc.util.AocParser.Companion.parselines
import aoc.*
import aoc.getDayInput
import aoc.util.*

val testInput = """
kh-tc
qp-kh
de-cg
ka-co
yn-aq
qp-ub
cg-tb
vc-aq
tb-ka
wh-tc
yn-cg
kh-ub
ta-co
de-co
tc-td
tb-wq
wh-td
ta-ka
td-qp
aq-cg
wq-ub
ub-vc
de-ta
wq-aq
wq-vc
wh-yn
ka-de
kh-ta
co-tc
wh-qp
tb-vc
td-yn
""".parselines

class Graph(val edges: List<List<String>>) {
    val nodes = edges.flatten().toSet()
    val adj = nodes.associateWith {
        edges.filter { e -> it in e }.map { e -> if (e[0] == it) e[1] else e[0] }
    }
    fun neighbors(n: String) = adj[n]!!
    fun adj(n1: String, n2: String) = n2 in neighbors(n1)
    fun triples() = nodes.filter { neighbors(it).size >= 2 }
        .flatMap { node ->
            neighbors(node).pairwise().filter { adj(it.first(), it.last()) }.map { nbrs ->
                nbrs + node
            }
        }.toSet()

    /** Generate all sets that include the given set plus one more node and are fully connected. */
    fun growFullyConnectedSet(s: Set<String>) =
        (neighbors(s.first()).toSet() - s)
            .filter { n -> s.all { adj(it, n) } }
            .map { n -> s + n }
            .toSet()
}

// part 1

fun List<String>.part1(): Int {
    val gr = Graph(map { it.split("-") })
    println("Constructed graph with ${gr.edges.size} edges and ${gr.nodes.size} nodes.")
    val triples = gr.triples()
    println("Found ${triples.size} triples in the graph.")
    return triples.count { it.any { it.startsWith('t') } }
}

// part 2

fun List<String>.part2(): String {
    val gr = Graph(map { it.split("-") })
    println("Constructed graph with ${gr.edges.size} edges and ${gr.nodes.size} nodes.")
    var nbhds: Set<Set<String>> = gr.edges.map { it.toSet() }.toSet()
    while (nbhds.size > 1) {
        println("  there are ${nbhds.size} fully connected neighborhoods of size ${nbhds.first().size}.")
        nbhds = nbhds.flatMap { gr.growFullyConnectedSet(it) }.toSet()
    }
    return nbhds.first().sorted().joinToString(",")
}

// calculate answers

val day = 23
val input = getDayInput(day, 2024)
val testResult = testInput.part1()
val testResult2 = testInput.part2()
val answer1 = input.part1().also { it.print }
val answer2 = input.part2().also { it.print }

// print results

AocRunner(day,
    test = { "$testResult, $testResult2" },
    part1 = { answer1 },
    part2 = { answer2 }
).run()
