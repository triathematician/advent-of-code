import aoc.util.AocParser.Companion.parselines
import aoc.*
import aoc.getDayInput

val testInput = """
190: 10 19
3267: 81 40 27
83: 17 5
156: 15 6
7290: 6 8 6 15
161011: 16 10 13
192: 17 8 14
21037: 9 7 18 13
292: 11 6 16 20
""".parselines

// part 1

fun List<String>.part1() = sumOf {
    val target = it.substringBefore(":").toLong()
    val args = it.substringAfter(":").trim().longs(" ")
    if (valid(target, args))
        target
    else 0
}

fun valid(target: Long, args: List<Long>): Boolean {
    if (args.size == 1)
        return args[0] == target
    else if (args.sum() == target || args[0] * args[1] == target)
        return true
    return valid(target, listOf(args[0] + args[1]) + args.drop(2)) ||
            valid(target, listOf(args[0] * args[1]) + args.drop(2))
}

//region FIRST ATTEMPT - MISREAD PROBLEM TO FOLLOW ORDER OF OPS

fun valid3(target: Long, args: List<Long>): Boolean =
    target in allPossibleSums(target, args)

/** Generate all possible sums less than or equal to the target. */
fun allPossibleSums(target: Long, args: List<Long>): Set<Long> {
    if (args.isEmpty())
        return setOf(0)
    else if (args.size == 1)
        return setOf(args[0])
    else if (args.size == 2)
        return setOf(args[0] * args[1], args[0] + args[1])

    val sums = mutableSetOf<Long>()
    val prod = args.reduce { x, y -> x * y }
    if (prod <= target)
        sums.add(prod)
    // divide into two groups that will be added together
    for (i in 1..args.size - 2) {
        val sumsA = allPossibleSums(target, args.subList(0, i))
        val sumsB = allPossibleSums(target, args.subList(i, args.size))
        sums.addAll(sumsA.map { a ->
            sumsB.map { b -> a + b }.filter { it <= target }
        }.flatten())
    }
    return sums
}

//endregion

// part 2

fun List<String>.part2() = sumOf {
    val target = it.substringBefore(":").toLong()
    val args = it.substringAfter(":").trim().longs(" ")
    if (valid2(target, args))
        target
    else 0
}

fun valid2(target: Long, args: List<Long>): Boolean {
    if (args.size == 1)
        return args[0] == target
    else if (args.sum() == target || args[0] * args[1] == target || join(args[0], args[1]) == target)
        return true
    return valid2(target, listOf(args[0] + args[1]) + args.drop(2)) ||
            valid2(target, listOf(args[0] * args[1]) + args.drop(2)) ||
            valid2(target, listOf(join(args[0], args[1])) + args.drop(2))
}

fun join(a: Long, b: Long) = "$a$b".toLong()

// calculate answers

val day = 7
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
