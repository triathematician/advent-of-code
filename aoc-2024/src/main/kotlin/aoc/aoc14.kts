import aoc.util.AocParser.Companion.parselines
import aoc.*
import aoc.getDayInput
import aoc.util.*

val testInput = """
p=0,4 v=3,-3
p=6,3 v=-1,-3
p=10,3 v=-1,2
p=2,0 v=2,-1
p=0,0 v=1,3
p=3,0 v=-2,-2
p=7,6 v=-1,-3
p=3,0 v=-1,-2
p=9,3 v=2,3
p=7,3 v=-1,2
p=2,4 v=2,-3
p=9,5 v=-3,-3
""".parselines

// part 1

class Bot(var p: Coord, val v: Coord) {
    fun step(wid: Int, ht: Int) {
        val x = (p.x + v.x + wid) % wid
        val y = (p.y + v.y + ht) % ht
        p = x to y
    }
}

fun List<String>.parse() = map {
    it.split(" ").map { it.substringAfter("=").split(",") }.let {
        Bot(it[0][0].toInt() to it[0][1].toInt(), it[1][0].toInt() to it[1][1].toInt())
    }
}

fun List<String>.part1(): Int {
    val WID = if (size < 20) 11 else 101
    val HT = if (size < 20) 7 else 103
    val bots = parse()
    repeat(100) { bots.onEach { it.step(WID, HT) } }
    return bots.score(WID, HT)
}

fun List<Bot>.score(WID: Int, HT: Int): Int {
    val countA = count { it.p.x < WID / 2 && it.p.y < HT / 2 }
    val countB = count { it.p.x < WID / 2 && it.p.y > HT / 2 }
    val countC = count { it.p.x > WID / 2 && it.p.y < HT / 2 }
    val countD = count { it.p.x > WID / 2 && it.p.y > HT / 2 }
    return countA * countB * countC * countD
}

// part 2

val listA = (1..1000).map { 38 + 101*it }
val listB = (1..1000).map { 88 + 103*it }
val min = listA.toSet().intersect(listB).minOrNull()!!
println("Min in both sets: $min")

fun List<String>.part2(): Int {
    val WID = if (size < 20) 11 else 101
    val HT = if (size < 20) 7 else 103
    val bots = parse()
    var min = Int.MAX_VALUE
    var minI = -1
    (1..10000).forEach {
        bots.onEach { it.step(WID, HT) }
        val countA = bots.count { it.p.x < WID / 2 && it.p.y < HT / 2 }
        val countB = bots.count { it.p.x < WID / 2 && it.p.y > HT / 2 }
        val countC = bots.count { it.p.x > WID / 2 && it.p.y < HT / 2 }
        val countD = bots.count { it.p.x > WID / 2 && it.p.y > HT / 2 }
        val prod = countA * countB * countC * countD
        if (prod < min) {
            min = prod
            minI = it
            println("------- $it minimized $prod -------")
            val grid = Array(HT) { ".".repeat(WID) }
            bots.forEach { grid[it.p.y] = grid[it.p.y].replaceRange(it.p.x, it.p.x + 1, "#") }
            grid.take(50).forEach { println(it) }
        }
//        if (countA*countB*countC*countD == 0) {
//        if (WID > 20 && (countA+countB>countC+countD+100) || (countA+countC>countB+countD+100)) {
//            val grid = Array(HT) { ".".repeat(WID) }
//            bots.forEach { grid[it.p.y] = grid[it.p.y].replaceRange(it.p.x, it.p.x + 1, "#") }
//            println("------- $it $countA $countB $countC $countD -------")
//            grid.take(50).forEach { println(it) }
//            Thread.sleep(50)
//        }
    }
    return minI
}

// calculate answers

val day = 14
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
