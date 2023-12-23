import aoc.AocParser.Companion.parselines
import aoc.*
import aoc.util.Loc3
import aoc.util.getDayInput

val testInput = """
1,0,1~1,2,1
0,0,2~2,0,2
0,2,3~2,2,3
0,0,4~0,2,4
2,0,5~2,2,5
0,1,6~2,1,6
1,1,8~1,1,9
""".parselines

class Brick(val p1: Loc3, val p2: Loc3) {
    val locs = when {
        p1.x == p2.x && p1.y == p2.y -> (p1.z..p2.z).map { Loc3(p1.x, p1.y, it) }
        p1.x == p2.x && p1.z == p2.z -> (p1.y..p2.y).map { Loc3(p1.x, it, p1.z) }
        p1.y == p2.y && p1.z == p2.z -> (p1.x..p2.x).map { Loc3(it, p1.y, p1.z) }
        else -> error("Invalid brick")
    }.toSet()

    fun fall() = Brick(p1.copy(z = p1.z-1), p2.copy(z = p2.z-1))
}

/** Return true if the bottom if this brick is touching the top of the other brick. */
fun Brick.onTopOf(other: Brick) = locs.any { Loc3(it.x, it.y, it.z-1) in other.locs }

class BrickConfig(val bricks: List<Brick>) {
    /** Which bricks are on the ground. */
    val grounded = bricks.filter { it.p1.z == 1 || it.p2.z == 1 }.toSet()

    /** Generate graph of which bricks are touching the top of other bricks. A given key is "on top of" each of its values. */
    val graphOnTop: Map<Brick, Set<Brick>>
    /** Generate graph of which bricks are supporting other bricks. A given key is "supporting" each of its values. */
    val graphSupporting: Map<Brick, Set<Brick>>

    init {
        val graph = mutableSetOf<Pair<Brick, Brick>>()
        bricks.indices.forEach { a ->
            bricks.indices.forEach { b ->
                if (a != b && bricks[a].onTopOf(bricks[b])) {
                    graph.add(bricks[a] to bricks[b])
                }
            }
        }
        graphOnTop = graph.groupBy { it.first }.mapValues { it.value.map { it.second }.toSet() }
        graphSupporting = graph.groupBy { it.second }.mapValues { it.value.map { it.first }.toSet() }
    }

    /** Which bricks are stacked on other bricks connected to the ground. */
    val stacked: Set<Brick> = run {
        var layer = grounded.toSet()
        val remainingBricks = bricks.toMutableSet()
        val result = mutableSetOf<Brick>()
        while (layer.isNotEmpty()) {
            result.addAll(layer)
            remainingBricks.removeAll(layer)
            layer = remainingBricks.filter { brick -> layer.any { brick.onTopOf(it) } }.toSet()
        }
        result
    }
    /** Which bricks are free and could fall. */
    val free = bricks - stacked

    /** Return a new [BrickConfig] with all free bricks fallen. */
    fun fall() = BrickConfig(bricks.map { if (it in free) it.fall() else it })

    fun disintegrable() = bricks.count {
        val supp = graphSupporting[it] ?: emptyList()
        supp.all { graphOnTop[it]!!.size > 1 }
    }

    fun howManyWouldFall() = bricks.sumOf { solelySupportedBy(it).size - 1 }

    /** How many bricks are solely supported by [brick], all the way up the chain, including the brick itself. */
    fun solelySupportedBy(brick: Brick): Set<Brick> {
        val supportingGroup = mutableSetOf(brick)
        var toCheck = mutableSetOf(brick)
        while (toCheck.isNotEmpty()) {
            val supps = toCheck.flatMap { graphSupporting[it] ?: emptyList() }.toSet()
            val wouldFall = supps.filter { it !in supportingGroup && graphOnTop[it]!!.all { it in supportingGroup } }
            supportingGroup.addAll(wouldFall)
            toCheck = wouldFall.toMutableSet()
        }
        return supportingGroup
    }
}

fun String.parse() = split("~").map { it.split(",").map { it.toInt() } }.let {
    Brick(Loc3(it[0][0], it[0][1], it[0][2]), Loc3(it[1][0], it[1][1], it[1][2]))
}

// part 1

fun List<String>.part1(): Int {
    var bricks = BrickConfig(map { it.parse() })
    while (bricks.free.isNotEmpty()) {
        bricks = bricks.fall()
    }
    return bricks.disintegrable()
}

// part 2

fun List<String>.part2(): Int {
    var bricks = BrickConfig(map { it.parse() })
    while (bricks.free.isNotEmpty()) {
        bricks = bricks.fall()
    }
    return bricks.howManyWouldFall()
}

// calculate answers

val day = 22
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
