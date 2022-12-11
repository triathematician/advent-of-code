import aoc.*
import aoc.AocParser.Companion.parselines

val day = 11

val testInput = """
    Monkey 0:
  Starting items: 79, 98
  Operation: new = old * 19
  Test: divisible by 23
    If true: throw to monkey 2
    If false: throw to monkey 3

Monkey 1:
  Starting items: 54, 65, 75, 74
  Operation: new = old + 6
  Test: divisible by 19
    If true: throw to monkey 2
    If false: throw to monkey 0

Monkey 2:
  Starting items: 79, 60, 97
  Operation: new = old * old
  Test: divisible by 13
    If true: throw to monkey 1
    If false: throw to monkey 3

Monkey 3:
  Starting items: 74
  Operation: new = old + 3
  Test: divisible by 17
    If true: throw to monkey 0
    If false: throw to monkey 1
""".parseInput()

val input = """
    Monkey 0:
  Starting items: 63, 57
  Operation: new = old * 11
  Test: divisible by 7
    If true: throw to monkey 6
    If false: throw to monkey 2

Monkey 1:
  Starting items: 82, 66, 87, 78, 77, 92, 83
  Operation: new = old + 1
  Test: divisible by 11
    If true: throw to monkey 5
    If false: throw to monkey 0

Monkey 2:
  Starting items: 97, 53, 53, 85, 58, 54
  Operation: new = old * 7
  Test: divisible by 13
    If true: throw to monkey 4
    If false: throw to monkey 3

Monkey 3:
  Starting items: 50
  Operation: new = old + 3
  Test: divisible by 3
    If true: throw to monkey 1
    If false: throw to monkey 7

Monkey 4:
  Starting items: 64, 69, 52, 65, 73
  Operation: new = old + 6
  Test: divisible by 17
    If true: throw to monkey 3
    If false: throw to monkey 7

Monkey 5:
  Starting items: 57, 91, 65
  Operation: new = old + 5
  Test: divisible by 2
    If true: throw to monkey 0
    If false: throw to monkey 6

Monkey 6:
  Starting items: 67, 91, 84, 78, 60, 69, 99, 83
  Operation: new = old * old
  Test: divisible by 5
    If true: throw to monkey 2
    If false: throw to monkey 4

Monkey 7:
  Starting items: 58, 78, 69, 65
  Operation: new = old + 7
  Test: divisible by 19
    If true: throw to monkey 5
    If false: throw to monkey 1
""".parseInput()

fun String.parseInput() = groupedOnBlankLines {
    val lines = it.parselines
    val num = lines[0].between(" ",":").toInt()
    Monkey(num,
        lines[1].substringAfter(": ").longs().toMutableList(),
        lines.chunk(2, 4),
        lines.chunk(2, 5),
        lines.chunkint(3, 3),
        lines.chunkint(4, 5),
        lines.chunkint(5, 5)
    )
}

data class Monkey(
    val num: Int, val items: MutableList<Long>,
    val op: String, // +,*,-,/
    val opVal: String, // old or an int
    val testDiv: Int, val trueNum: Int, val falseNum: Int,
    var inspected: Long = 0) {

    fun copy() = copy(items = items.toMutableList())

    fun updateWorry(w: Long, div3: Long): Long {
        val ov = when (opVal) {
            "old" -> w
            else -> opVal.toInt()
        }.toLong()
        val res = when (op) {
            "+" -> w + ov
            "-" -> w - ov
            "*" -> w * ov
            "/" -> w / ov
            else -> TODO()
        }
        return if (div3 == 3L) res/3 else res % div3
    }

    fun testThrow(w: Long): Int {
        return if (w % testDiv == 0L) trueNum else falseNum
    }
}

// test case

val t1 = testInput.map { it.copy() }
t1.doInspections(20, div3 = 3)

class Summary(monkeys: List<Monkey>) {
    val inspectionCounts = monkeys.map { it.inspected }
    val monkeyBusiness = inspectionCounts.sortedDescending().take(2).let { it[0] * it[1] }

    override fun toString() = "[${inspectionCounts.joinToString(",")}] mb=$monkeyBusiness"
}

val t2 = testInput.map { it.copy() }
val tmprod = t2.map { it.testDiv.toLong() }.reduce { a, b -> a * b }
t2.doInspections(10000, div3 = tmprod)

fun List<Monkey>.doInspections(count: Int, div3: Long) {
    repeat(count) {
        forEach { m ->
            m.items.toList().forEach { w ->
                val newWorry = m.updateWorry(w, div3)
                val newMonkey = m.testThrow(newWorry)
                this[newMonkey].items += newWorry
                m.inspected++
            }
            m.items.clear()
        }
    }
}

// part 1

val in1 = input.map { it.copy() }
in1.doInspections(20, div3 = 3)

// part 2

val in2 = input.map { it.copy() }
val mprod = in2.map { it.testDiv.toLong() }.reduce { a, b -> a * b }
in2.doInspections(10000, div3 = mprod)

// print results

AocRunner(day,
    info = { listOf("Leaderboard: 13:07/18:05", "Answers: 107822/27267163742") },
    test = { "Inspection 1: ${Summary(t1)}\nInspection 2: ${Summary(t2)}" },
    part1 = { Summary(in1) },
    part2 = { Summary(in2) }
).run()
