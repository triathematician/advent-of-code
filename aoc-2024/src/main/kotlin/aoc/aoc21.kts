import aoc.util.AocParser.Companion.parselines
import aoc.*
import aoc.getDayInput
import aoc.util.*
import kotlin.math.pow

val testInput = """
029A
980A
179A
456A
379A
""".parselines

//region grid setup

val gridN = """
789
456
123
 0A
""".trimIndent().lines()
val adjN = "0123456789A".map { e ->
    val c = gridN.allIndices2().find { gridN[it] == e }!!
    e to DIRS4.filter { c + it in gridN.allIndices2() && gridN[c + it] != ' ' }
        .associateWith { gridN[c + it] }
}.toMap()
val gridD = """
 ^A
<v>
""".trimIndent().lines()
val adjD = "<>^vA".map { e ->
    val c = gridD.allIndices2().find { gridD[it] == e }!!
    e to DIRS4.filter { c + it in gridD.allIndices2() && gridD[c + it] != ' ' }
        .associateWith { gridD[c + it] }
}.toMap()

//endregion

/** Tracks the current position of you and the three bots, and the sequences that have been typed so far. */
data class BotButtonState(var bot1: Char, var bot2: Char, var bot3: Char,
    var seq3: String = "") {

    fun press(c: Char): BotButtonState? {
        val newState = copy()
        when (c) {
            'A' -> return newState.press1(bot1)
            '^' -> newState.bot1 = adjD[bot1]!![UP] ?: return null
            'v' -> newState.bot1 = adjD[bot1]!![DOWN] ?: return null
            '<' -> newState.bot1 = adjD[bot1]!![LEFT] ?: return null
            '>' -> newState.bot1 = adjD[bot1]!![RIGHT] ?: return null
        }
        return newState
    }

    fun press1(c: Char): BotButtonState? {
        val newState = copy()
        when (c) {
            'A' -> return newState.press2(bot2)
            '^' -> newState.bot2 = adjD[bot2]!![UP] ?: return null
            'v' -> newState.bot2 = adjD[bot2]!![DOWN] ?: return null
            '<' -> newState.bot2 = adjD[bot2]!![LEFT] ?: return null
            '>' -> newState.bot2 = adjD[bot2]!![RIGHT] ?: return null
        }
        return newState
    }

    fun press2(c: Char): BotButtonState? {
        val newState = copy()
        when (c) {
            'A' -> return newState.press3(bot3)
            '^' -> newState.bot3 = adjN[bot3]!![UP] ?: return null
            'v' -> newState.bot3 = adjN[bot3]!![DOWN] ?: return null
            '<' -> newState.bot3 = adjN[bot3]!![LEFT] ?: return null
            '>' -> newState.bot3 = adjN[bot3]!![RIGHT] ?: return null
        }
        return newState
    }

    fun press3(c: Char): BotButtonState {
        seq3 += c
        return this
    }
}

/** Tracks the current position of you and the three bots, and the sequences that have been typed so far. */
data class BotButtonState2(var bots: String, var seq: String = "") {

    fun setBot(i: Int, c: Char): BotButtonState2 {
        bots = bots.substring(0, i) + c + bots.substring(i + 1)
        return this
    }

    fun press(c: Char): BotButtonState2? {
        val res = press(-1, c)
        return res
    }
    fun press(i: Int, c: Char): BotButtonState2? {
        if (i == bots.length - 1) {
            seq += c
            return this
        }
        val newState = copy()
        val adj = if (i == bots.length - 2) adjN[bots[i + 1]]!! else adjD[bots[i + 1]]!!
        when (c) {
            'A' -> return newState.press(i + 1, bots[i + 1])
            '^' -> newState.setBot(i + 1, adj[UP] ?: return null)
            'v' -> newState.setBot(i + 1, adj[DOWN] ?: return null)
            '<' -> newState.setBot(i + 1, adj[LEFT] ?: return null)
            '>' -> newState.setBot(i + 1, adj[RIGHT] ?: return null)
        }
        return newState
    }
}

// part 1

fun List<String>.part1() = sumOf {
    println("Calculating score for $it")
    val sumScore = score1(it[0], 'A') +
            score1(it[1], it[0]) +
            score1(it[2], it[1]) +
            score1(it[3], it[2])
    println("  sum score: $sumScore, total: ${sumScore * it.take(3).toInt()}")
    sumScore * it.take(3).toInt()
}

/** Calculate minimum number of button presses for last bot to generate the target sequence. */
fun score1(tgt: Char, st3: Char): Int {
    val search = AstarSearch(BotButtonState( 'A', 'A', st3),
        { it.seq3 == tgt.toString() },
        { s -> adjD.keys.mapNotNull { s.press(it) }.filter { it.seq3 == "" || it.seq3 == tgt.toString() }.associateWith { 1 } },
        { 8L * (1 - it.seq3.length) }
    )
    val res = search.minimizeCost().second
//    println("  min cost from $st3 to $tgt: $res")
    return res.toInt()
}

/** Calculate minimum number of button presses for last bot to generate the target sequence. */
fun score1b(tgt: Char, st3: Char, depth: Int): Long {
    val search = AstarSearch(BotButtonState2( "A".repeat(depth) + st3),
        { it.seq == tgt.toString() },
        { s -> adjD.keys.mapNotNull { s.press(it) }.filter { it.seq == "" || it.seq == tgt.toString() }.associateWith { 1 } },
        { (2.0.pow(depth+1) * (1 - it.seq.length)).toLong() }
    )
    val res = search.minimizeCost().second
//    println("  min cost from $st3 to $tgt: $res")
    return res
}

// part 2

fun List<String>.part2a(depth: Int) = sumOf {
    println("Calculating score for $it")
    val sumScore = score1b(it[0], 'A', depth) +
            score1b(it[1], it[0], depth) +
            score1b(it[2], it[1], depth) +
            score1b(it[3], it[2], depth)
    println("  sum score: $sumScore, total: ${sumScore * it.take(3).toInt()}")
    sumScore * it.take(3).toInt()
}

fun List<String>.part2(depth: Int) = sumOf {
    println("Calculating score for $it at depth $depth")
    val sumScore = score2('A', it[0], depth) +
            score2(it[0], it[1], depth) +
            score2(it[1], it[2], depth) +
            score2(it[2], it[3], depth)
    println("  sum score: $sumScore * ${it.take(3).toInt()}")
    sumScore * it.take(3).toInt()
}

/** Calculate minimum number of button presses for last bot to generate the target sequence. */
fun score2(startChar: Char, tgtChar: Char, depth: Int): Long {
    val p0 = gridN.allIndices2().find { gridN[it] == startChar }!!
    val p1 = gridN.allIndices2().find { gridN[it] == tgtChar }!!
    val horiz = when(p1.x - p0.x) {
        -2 -> "<<"
        -1 -> "<"
        0 -> ""
        1 -> ">"
        2 -> ">>"
        else -> error("Invalid horizontal distance: ${p1.x - p0.x}")
    }
    val vert = when(p1.y - p0.y) {
        -3 -> "^^^"
        -2 -> "^^"
        -1 -> "^"
        0 -> ""
        1 -> "v"
        2 -> "vv"
        3 -> "vvv"
        else -> error("Invalid vertical distance: ${p1.y - p0.y}")
    }
    val options = mutableSetOf("A" + horiz + vert + "A", "A" + vert + horiz + "A")
    when {
        startChar == 'A' -> options.removeIf { it.startsWith("A<<") }
        tgtChar == 'A' -> options.removeIf { it.endsWith(">>A") }
        startChar == '0' -> options.removeIf { it.startsWith("A<") }
        tgtChar == '0' -> options.removeIf { it.endsWith(">A") }
    }
    val res = options.minOf {
        (0..it.length-2).sumOf { i ->
            score2B(depth, it.substring(i, i + 2))
        }
    }
//    println("  min cost from $startChar to $tgtChar: $res")
    return res
}

/** Stores minimum paths for all depths and pairs of characters. */
val score2BCache = mutableMapOf<Int, MutableMap<String, Long>>()

/**
 * Cost to move from the first character to the second character and push the button.
 * Depth = 0 indicates the human (no move required other than pushing the button).
 * Depth = 1 is the first robot, etc.
 */
fun score2B(depth: Int, join: String): Long {
    val cacheAtDepth = score2BCache.getOrPut(depth) { mutableMapOf() }
    fun String.score() = (0..length-2).sumOf { score2B(depth - 1, substring(it, it + 2)) }
    return cacheAtDepth.getOrPut(join) {
        if (depth == 0)
            1
        else if (join[0] == join[1])
            "AA".score()
        else {
            when (join) {
                "A^", ">v", "v<" -> "A<A".score()
                "^A", "v>", "<v" -> "A>A".score()
                "^v", "A>" -> "AvA".score()
                "v^", ">A" -> "A^A".score()
                "><" -> "A<<A".score()
                "<>" -> "A>>A".score()
                ">^" -> minOf("A<^A".score(), "A^<A".score())
                "^>" -> minOf("A>vA".score(), "Av>A".score())
                "^<" -> "Av<A".score()
                "<^" -> "A>^A".score()
                "Av" -> minOf("Av<A".score(), "A<vA".score())
                "vA" -> minOf("A>^A".score(), "A^>A".score())
                "A<" -> "Av<<A".score()
                "<A" -> "A>>^A".score()
                else -> error("Invalid join: $join")
            }
        }
    }
}

var s1 = BotButtonState('A', 'A', 'A')
"<vA<AA>>^AvAA<^A>A<v<A>>^AvA^A<vA>^A<v<A>^A>AAvA^A<v<A>A>^AAAvA<^A>A".forEach { s1 = s1.press(it)!! }
println("Test done: ${s1.seq3}")

var s2a = BotButtonState2("A")
"<A^A>^^AvvvA".forEach { s2a = s2a.press(it)!! }
println("Test done: ${s2a.seq}")

var s2b = BotButtonState2("AA")
"v<<A>>^A<A>AvA<^AA>A<vAAA>^A".forEach { s2b = s2b.press(it)!! }
println("Test done: ${s2b.seq}")

var s2c = BotButtonState2("AAA")
"<vA<AA>>^AvAA<^A>A<v<A>>^AvA^A<vA>^A<v<A>^A>AAvA^A<v<A>A>^AAAvA<^A>A".forEach { s2c = s2c.press(it)!! }
println("Test done: ${s2c.seq}")

adjN.keys.forEach { key1 ->
    adjN.keys.forEach { key2 ->
        val score1 = score1(key2, key1)
        val score1b = score1b(key2, key1, 2)
        val score2 = score2(key1, key2, 2)
        if (score1.toLong() != score2 || score1b != score2 || score1.toLong() != score1b) {
            println("Mismatch for $key1 $key2: $score1 vs $score1b vs $score2")
        }
    }
}

adjN.keys.forEach { key1 ->
    adjN.keys.forEach { key2 ->
        val score1b = score1b(key2, key1, 3)
        val score2 = score2(key1, key2, 3)
        if (score1b != score2) {
            println("Mismatch for $key1 $key2 at depth 3: $score1b vs $score2")
        }
    }
}

(0..25).forEach {
    println("--- Calculating at depth $it ---")
    listOf("029A").part2(it)
//    listOf("029A").part2a(it)
}

// calculate answers

val day = 21
val input = getDayInput(day, 2024)
val testResult = testInput.part1()
val testResultB = testInput.part2(depth = 2)
//val testResult2A = testInput.part2a(25)
val testResult2 = testInput.part2(depth = 25)
val answer1 = input.part1().also { it.print }
val answer1B = input.part2(depth = 2).also { it.print }
val answer2 = input.part2(depth = 25).also { it.print }

// print results

AocRunner(day,
    test = { "$testResult $testResultB, $testResult2" },
    part1 = { "$answer1 $answer1B" },
    part2 = { answer2 }
).run()
