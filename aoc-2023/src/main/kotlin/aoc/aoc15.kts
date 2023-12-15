import aoc.AocParser.Companion.parselines
import aoc.*
import aoc.util.getDayInput

val testInput = """
rn=1,cm-,qp=3,cm=2,qp-,pc=4,ot=9,ab=5,pc-,pc=6,ot=7
""".parselines

fun String.hash(): Int {
    var hash = 0
    for (i in 0 until length) {
        hash = (17 * (hash + get(i).code)) % 256
    }
    return hash
}

println("HASH".hash())

// part 1

fun List<String>.part1(): Int = first().split(",").sumOf { it.hash() }

// part 2

fun List<String>.part2(): Int {
    val boxes = (0..255).associateWith { mutableMapOf<String, Int>() }
    first().split(",").onEach {
        val eq = it.indexOf("=")
        val min = it.indexOf("-")
        val label = it.substring(0, maxOf(eq, min))
        val value = if (eq > 0) it.substring(eq + 1).toInt() else 0
        val hash = label.hash()
        val box = boxes[hash]!!
        if (eq > 0) {
            // this will overwrite current value or add another on the end
            box[label] = value
        } else {
            box.remove(label)
        }
    }
    return boxes.entries.sumOf { (num, content) ->
        content.entries.withIndex().sumOf { (index, value) ->
            (num + 1) * (index + 1) * value.value
        }
    }
}

// calculate answers

val day = 15
val input = getDayInput(day, 2023)
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
