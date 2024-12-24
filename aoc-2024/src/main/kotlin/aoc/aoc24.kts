import aoc.util.AocParser.Companion.parselines
import aoc.*
import aoc.getDayInput
import aoc.util.*

val testInput = """
x00: 1
x01: 1
x02: 1
y00: 0
y01: 1
y02: 0

x00 AND y00 -> z00
x01 XOR y01 -> z01
x02 OR y02 -> z02
""".parselines

val testInput2 = """
x00: 1
x01: 0
x02: 1
x03: 1
x04: 0
y00: 1
y01: 1
y02: 1
y03: 1
y04: 1

ntg XOR fgs -> mjb
y02 OR x01 -> tnw
kwq OR kpj -> z05
x00 OR x03 -> fst
tgd XOR rvg -> z01
vdt OR tnw -> bfw
bfw AND frj -> z10
ffh OR nrd -> bqk
y00 AND y03 -> djm
y03 OR y00 -> psh
bqk OR frj -> z08
tnw OR fst -> frj
gnj AND tgd -> z11
bfw XOR mjb -> z00
x03 OR x00 -> vdt
gnj AND wpb -> z02
x04 AND y00 -> kjc
djm OR pbm -> qhw
nrd AND vdt -> hwm
kjc AND fst -> rvg
y04 OR y02 -> fgs
y01 AND x02 -> pbm
ntg OR kjc -> kwq
psh XOR fgs -> tgd
qhw XOR tgd -> z09
pbm OR djm -> kpj
x03 XOR y03 -> ffh
x00 XOR y04 -> ntg
bfw OR bqk -> z06
nrd XOR fgs -> wpb
frj XOR qhw -> z04
bqk OR frj -> z07
y03 OR x01 -> nrd
hwm AND bqk -> z03
tgd XOR rvg -> z12
tnw OR pbm -> gnj
""".parselines

// part 1

class WireRules(val init: Map<String, Boolean>, var ops: List<WireOp>) {
    val swapped = mutableSetOf<Pair<String, String>>()
    fun swapOutputs(s: String, s1: String) {
        ops = ops.map {
            when (it.out) {
                s -> it.copy(out = s1)
                s1 -> it.copy(out = s)
                else -> it
            }
        }
        swapped.add(s to s1)
    }
    fun solve(): MutableMap<String, Boolean> {
        val values = init.toMutableMap()
        val opsApplied = mutableSetOf<WireOp>()
        while (true) {
            val opsToApply = (ops - opsApplied).filter { it.in1 in values && it.in2 in values }
            if (opsToApply.isEmpty()) break
            opsToApply.forEach { op ->
                values[op.out] = op.apply(values)
                opsApplied.add(op)
            }
        }
        return values
    }
}

data class WireOp(val in1: String, val in2: String, val op: String, val out: String) {
    override fun toString() = "$in1 $op $in2 -> $out"
    fun apply(wires: Map<String, Boolean>): Boolean {
        val w1 = wires[in1]
        val w2 = wires[in2]
        return when (op) {
            "AND" -> w1!! && w2!!
            "OR" -> w1!! || w2!!
            "XOR" -> w1!! xor w2!!
            else -> error("Unknown op: $op")
        }
    }
}

fun List<String>.parse(): WireRules {
    val blank = indexOf("")
    val inits = subList(0, blank).associate { line ->
        val (name, value) = line.split(": ")
        name to (value == "1")
    }
    val rules = subList(blank + 1, size).map { line ->
        val (in1, op, in2, arr, out) = line.split(" ")
        WireOp(in1, in2, op, out)
    }
    return WireRules(inits, rules)
}

fun List<String>.part1(): String {
    val rules = parse()
    val values = rules.solve()
    val x = values.resolveNum('x')
    val y = values.resolveNum('y')
    val z = values.resolveNum('z')
    return """
        $x $y $z (sum expected ${x+y} off by ${x+y-z})
        ${x.toString(2)}
        ${y.toString(2)}
        ${(x+y).toString(2)}
        ${z.toString(2)}
    """.trimIndent()
}

fun Map<String, Boolean>.resolveNum(char: Char): Long {
    val outputs = filterKeys { it.startsWith(char) }
    return outputs.entries.sumOf {
        val place = it.key.drop(1).toInt()
        val on = it.value
        if (on) 1L shl place else 0
    }
}

fun Map<String, Boolean>.resolveBinary(char: Char): String =
    filterKeys { it.startsWith(char) }.toSortedMap().values
        .joinToString("") { if (it) "1" else "0" }

// part 2

fun List<String>.part2(): String {
    if (size < 50) return ""
    val rules = parse()
    rules.swapOutputs("vdc","z12")
    rules.swapOutputs("nhn","z21")
    rules.swapOutputs("tvb","khg")
    rules.swapOutputs("z33","gst")
    var lastUsed = setOf<String>()
    (0..45).forEach {
        val z = "z%02d".format(it)
        var target = setOf(z)
        val outputsUsed = mutableSetOf<String>()
        while (target.isNotEmpty()) {
            val next = target.flatMap {
                rules.ops.filter { op -> op.out == it }
                    .map { op -> listOf(op.in1, op.in2) }
            }.flatten().toSet() - outputsUsed
            outputsUsed.addAll(next)
            target = next
        }
        val diff = outputsUsed - lastUsed - setOf("x%02d".format(it), "y%02d".format(it))
        println("New wires used for $z: $diff")
        println("  ops: ${rules.ops.filter { it.out in diff || it.out == z }}")
        println("  ops: ${rules.ops.filter { it.out in diff || it.out == z }.map { it.op }.sorted()}")
        lastUsed = outputsUsed
    }
    val values = rules.solve()
    val x = values.resolveNum('x')
    val y = values.resolveNum('y')
    val z = values.resolveNum('z')
    println("""
        $x $y $z (sum expected ${x+y} off by ${x+y-z})
        ${x.toString(2)}
        ${y.toString(2)}
        ${(x+y).toString(2)}
        ${z.toString(2)}
    """.trimIndent())
    return rules.swapped.flatMap { it.toList() }.sorted().joinToString(",")
}

// calculate answers

val day = 24
val input = getDayInput(day, 2024)
val testResult = testInput.part1()
val testResultB = testInput2.part1()
val testResult2 = testInput.part2()
val answer1 = input.part1().also { it.print }
val answer2 = input.part2().also { it.print }

// print results

AocRunner(day,
    test = { "$testResult $testResultB, $testResult2" },
    part1 = { answer1 },
    part2 = { answer2 }
).run()
