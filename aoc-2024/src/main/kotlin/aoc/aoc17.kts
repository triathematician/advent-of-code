import aoc.util.AocParser.Companion.parselines
import aoc.*
import aoc.getDayInput
import aoc.util.*
import kotlin.math.pow

val testInput = """
Register A: 117440
Register B: 0
Register C: 0

Program: 0,3,5,4,3,0
""".parselines

class Program {
    var a = 0L
    var b = 0L
    var c = 0L
    var program = mutableListOf<Int>()
    var output = mutableListOf<Int>()
    var ip = 0

    fun runAll() {
//        println("-".repeat(40))
//        println("A,B,C: $a,$b,$c, PROGRAM: ${program.joinToString(",")}")
        var done = false
        while (!done) {
            done = runOne()
        }
//        println("FINAL A,B,C: $a,$b,$c, IP: $ip, OUTPUT: ${output.joinToString(",")}")
    }

    /** Run one instance, return true if done. */
    fun runOne(): Boolean {
        val op = program.getOrNull(ip) ?: return true
        val literal = program.getOrNull(ip + 1)
        val combo: Long = when (literal) {
            0, 1, 2, 3 -> literal.toLong()
            4 -> a
            5 -> b
            6 -> c
            7 -> if (op in listOf(0, 2, 5, 6, 7)) error("Invalid combo operand") else -1
            else -> error("Invalid combo operand")
        }
//        println("A,B,C: $a,$b,$c, IP: $ip ($op,$literal,$combo), OUTPUT: ${output.joinToString(",")}")
        when (op) {
            // adv - divide A by 2^combo
            0 -> {
//                println("  adv: a <- $a / 2^$combo")
                a = a.shr(combo.toInt())
                ip += 2
            }
            // bxl - bitwise xor b, literal
            1 -> {
//                println("  bxl: b <- $b xor $literal")
                b = b xor literal.toLong()
                ip += 2
            }
            // bst - write combo modulo 8 to B
            2 -> {
//                println("  bst: b <- $combo % 8")
                b = combo % 8L
                ip += 2
            }
            // jnz - nothing if A is 0, else jump by setting instruction pointer to value of literal operand (and not incrementing)
            3 -> {
                when (a) {
                    0L -> {
                        ip += 2
//                        println("  jnz: $a")
                    }
                    else -> {
                        ip = literal
//                        println("  jnz: $literal")
                    }
                }
            }
            // bxc - bitwise xor of B and C, stored in B
            4 -> {
//                println("  bxc: b <- $b xor $c")
                b = b xor c
                ip += 2
            }
            // out - output value of combo % 8
            5 -> {
//                println("  out: $combo % 8")
                output.add((combo % 8).toInt())
                ip += 2
            }
            // bdv - divide B by 2^combo
            6 -> {
//                println("  bdv: b <- $a / 2^$combo")
                b = a.shr(combo.toInt())
                ip += 2
            }
            // cdv - divide C by 2^combo
            7 -> {
//                println("  cdv: c <- $a / 2^$combo")
                c = a.shr(combo.toInt())
                ip += 2
            }
        }
        return false
    }
}

// part 1

fun List<String>.parse(): Program {
    val p = Program()
    p.a = this[0].substringAfter(":").trim().toLong()
    p.b = this[1].substringAfter(":").trim().toLong()
    p.c = this[2].substringAfter(":").trim().toLong()
    p.program = this[4].substringAfter(":").trim().parseInts().toMutableList()
    return p
}

fun List<String>.part1(): String {
    parse().apply {
        runAll()
        return output.joinToString(",")
    }
}

fun solve(a0: Long): String {
    if (a0 == 0L)
        return "0"
    var a = a0
    val res = mutableListOf<Int>()
    while (a != 0L) {
        val a3 = (a % 8).toInt()
        val b = (a3 xor 3) xor (a shr (a3 xor 5)).toInt()
        a = a shr 3
        res += b % 8
    }
    return res.joinToString("")
}

println(solve(34615120))
println("-")

// part 2

fun List<Int>.base10() = reversed().withIndex().sumOf { (i, v) -> v * 8.0.pow(i).toLong() }

fun solve2(a0: Long): String {
    val useInput = listOf("Register A: $a0") + input.drop(1)
    return useInput.part1()
}

fun part2c(): Long {
    val tgt = "2,4,1,5,7,5,1,6,0,3,4,3,5,5,3,0"
    // track base 8 representation of a
    var aa = listOf(listOf<Int>())
    var n = 0
    while (aa.none { solve2(it.base10()) == tgt }) {
        val nextAA = mutableListOf<List<Int>>()
        n++
        (0..7).forEach {
            aa.forEach { list ->
                if (tgt.takeLast(2 * n - 1) == solve2(list.base10() * 8 + it)) {
                    nextAA.add(list + it)
                }
            }
        }
        println("$nextAA -> ${solve2(nextAA.first().base10())}")
        aa = nextAA
    }
    println(aa.map { it.base10() })
    return aa.map { it.base10() }.min()
}

fun part2b(): Int {
    (0..Int.MAX_VALUE).forEach {
        if (it % 10000000 == 0)
            println("Checking $it")
        if (solve(it.toLong()) == "2415751603435530")
            return it
    }
    return -1
}

fun List<String>.part2(): Int {
    (0..Int.MAX_VALUE).forEach {
        val program = parse()
        program.a = it.toLong()
        program.runAll()
        if (it % 1000000 == 0) {
            println("Checking $it, PL = ${program.program.size}, OL = ${program.output.size}")
        }
        if (program.output == program.program) {
            println("Found at $it")
            return it
        }
    }
    return -1
}

// calculate answers

val day = 17
val input = getDayInput(day, 2024)
val testResult = testInput.part1().also { println(it) }
val testResult2 = testInput.part2().also { println(it) }
val answer1 = input.part1().also { println(it) }
val answer2 = 0 // part2c()

// print results
// 3,1,6,4,1,3,7,3,1
// 105706277661082
// 105706278693274 (too high)
// 105706278758810
// 105706278857114
// 110104325204378
// 110104325269914
// 110104325368218

// 2,4,1,5,7,5,1,6,0,3,4,3,5,5,3,0
// 2,4,1,5,7,5,1,6,0,3,4,3,5,5,3,0

println("--")
val p2c = part2c()
println(p2c)
println(solve2(p2c))
println("""
Register A: 105706277661082
Register B: 0
Register C: 0

Program: 2,4,1,5,7,5,1,6,0,3,4,3,5,5,3,0
""".trimIndent().parselines.part1()
)

AocRunner(day,
    test = { "$testResult, $testResult2" },
    part1 = { answer1 },
    part2 = { answer2 }
).run()
