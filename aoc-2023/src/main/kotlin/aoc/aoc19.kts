import aoc.AocParser.Companion.parselines
import aoc.*
import aoc.util.getDayInput

val testInput = """
px{a<2006:qkq,m>2090:A,rfg}
pv{a>1716:R,A}
lnx{m>1548:A,A}
rfg{s<537:gd,x>2440:R,A}
qs{s>3448:A,lnx}
qkq{x<1416:A,crn}
crn{x>2662:A,R}
in{s<1351:px,qqz}
qqz{s>2770:qs,m<1801:hdj,R}
gd{a>3333:R,R}
hdj{m>838:A,pv}

{x=787,m=2655,a=1222,s=2876}
{x=1679,m=44,a=2067,s=496}
{x=2036,m=264,a=79,s=2244}
{x=2461,m=1339,a=466,s=291}
{x=2127,m=1623,a=2188,s=1013}
""".parselines

fun String.parseWorkflow(): Workflow {
    val id = substringBefore("{")
    val rules = substringAfter("{").substringBefore("}").split(",")
    return Workflow(id, rules.dropLast(1).map { it.parseRule() }, rules.last())
}
fun String.parseRule() = Rule(get(0), get(1), drop(2).substringBefore(":").toInt(), substringAfter(":"))
fun String.parseRating(): Rating {
    val parts = drop(1).dropLast(1).split(",").map {
        it.split("=").let { it[0] to it[1].toInt() }
    }.toMap()
    return Rating(parts["x"]!!, parts["m"]!!, parts["a"]!!, parts["s"]!!)
}

class Rating(val x: Int, val m: Int, val a: Int, val s: Int)

class RatingRange(val xr: IntRange, val mr: IntRange, val ar: IntRange, val sr: IntRange) {
    fun size(): Long =
        xr.count().toLong() * mr.count().toLong() * ar.count().toLong() * sr.count().toLong()

    fun splitOnX(trueRange: IntRange, falseRange: IntRange, result: String): Map<RatingRange, String?> {
        val trueX = xr.intersectRange(trueRange)?.let { RatingRange(it, mr, ar, sr) to result }
        val falseX = xr.intersectRange(falseRange)?.let { RatingRange(it, mr, ar, sr) to null }
        return listOfNotNull(trueX, falseX).toMap()
    }
    fun splitOnM(trueRange: IntRange, falseRange: IntRange, result: String): Map<RatingRange, String?> {
        val trueM = mr.intersectRange(trueRange)?.let { RatingRange(xr, it, ar, sr) to result }
        val falseM = mr.intersectRange(falseRange)?.let { RatingRange(xr, it, ar, sr) to null }
        return listOfNotNull(trueM, falseM).toMap()
    }
    fun splitOnA(trueRange: IntRange, falseRange: IntRange, result: String): Map<RatingRange, String?> {
        val trueA = ar.intersectRange(trueRange)?.let { RatingRange(xr, mr, it, sr) to result }
        val falseA = ar.intersectRange(falseRange)?.let { RatingRange(xr, mr, it, sr) to null }
        return listOfNotNull(trueA, falseA).toMap()
    }
    fun splitOnS(trueRange: IntRange, falseRange: IntRange, result: String): Map<RatingRange, String?> {
        val trueS = sr.intersectRange(trueRange)?.let { RatingRange(xr, mr, ar, it) to result }
        val falseS = sr.intersectRange(falseRange)?.let { RatingRange(xr, mr, ar, it) to null }
        return listOfNotNull(trueS, falseS).toMap()
    }
}

fun IntRange.intersectRange(other: IntRange): IntRange? = when {
    last < other.first || other.last < first -> null
    first <= other.first && other.last <= last -> other
    other.first <= first && last <= other.last -> this
    first <= other.first && last <= other.last -> other.first..last
    other.first <= first && other.last <= last -> first..other.last
    else -> error("Unknown intersection of $this and $other")
}

class Workflow(val id: String, val rules: List<Rule>, val defResult: String) {
    fun test(rating: Rating): String {
        var result: String?
        rules.forEach {
            result = it.test(rating)
            if (result != null) return result!!
        }
        return defResult
    }
    fun test(ratingRange: RatingRange): Map<RatingRange, String> {
        val result = mutableMapOf<RatingRange, String>()
        var processing = listOf(ratingRange)
        rules.forEach { r ->
            val processNext = mutableListOf<RatingRange>()
            processing.forEach {
                r.test(it).forEach { (range, label) ->
                    if (label == null)
                        processNext += range
                    else
                        result[range] = label
                }
            }
            processing = processNext
        }
        processing.forEach {
            result[it] = defResult
        }
        return result
    }
}

class Rule(val ratingVar: Char, val testOp: Char, val testVal: Int, val result: String) {
    fun Rating.value() = when (ratingVar) {
        'x' -> x
        'm' -> m
        'a' -> a
        's' -> s
        else -> error("Unknown test variable $ratingVar")
    }

    val test: (Int) -> Boolean = when (testOp) {
        '>' -> { it -> it > testVal }
        '<' -> { it -> it < testVal }
        else -> error("Unknown test operator $testOp")
    }
    val trueRange = when (testOp) {
        '>' -> testVal+1..4000
        '<' -> 1..testVal-1
        else -> error("Unknown test operator $testOp")
    }
    val falseRange = when (testOp) {
        '>' -> 1..testVal
        '<' -> testVal..4000
        else -> error("Unknown test operator $testOp")
    }

    fun Rating.test(): Boolean = test(value())

    fun test(rating: Rating) = if (rating.test()) result else null

    fun test(ratingRange: RatingRange): Map<RatingRange, String?> {
        return when (ratingVar) {
            'x' -> ratingRange.splitOnX(trueRange, falseRange, result)
            'm' -> ratingRange.splitOnM(trueRange, falseRange, result)
            'a' -> ratingRange.splitOnA(trueRange, falseRange, result)
            's' -> ratingRange.splitOnS(trueRange, falseRange, result)
            else -> error("Unknown test variable $ratingVar")
        }
    }
}

fun Map<String, Workflow>.test(rating: Rating): String {
    val start = get("in")!!
    var result: String = start.test(rating)
    while (result != "A" && result != "R") {
        result = get(result)!!.test(rating)
    }
    return result
}

// part 1

fun List<String>.part1(): Int {
    val str = joinToString("\n").split("\n\n")
    val workflows = str[0].split("\n").map { it.parseWorkflow() }.associateBy { it.id }
    val ratings = str[1].split("\n").map { it.parseRating() }
    return ratings.filter { workflows.test(it) == "A" }.sumOf {
        it.x + it.m + it.a + it.s
    }
}

// part 2

fun List<String>.part2(): Long {
    val str = joinToString("\n").split("\n\n")
    val workflows = str[0].split("\n").map { it.parseWorkflow() }.associateBy { it.id }
    val ranges = RatingRange(1..4000, 1..4000, 1..4000, 1..4000)
    var tasks = mutableMapOf("in" to mutableListOf(ranges))
    val accepted = mutableListOf<RatingRange>()
    val rejected = mutableListOf<RatingRange>()
    while (tasks.isNotEmpty()) {
        val newTasks = mutableMapOf<String, MutableList<RatingRange>>()
        tasks.forEach { (w, ranges) ->
            val workflow = workflows[w]!!
            ranges.flatMap { workflow.test(it).entries }.forEach { (range, result) ->
                when (result) {
                    "A" -> accepted += range
                    "R" -> rejected += range
                    else -> newTasks.getOrPut(result) { mutableListOf() } += range
                }
            }
        }
        tasks = newTasks
    }
    return accepted.sumOf { it.size() }
}

// calculate answers

val day = 19
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
