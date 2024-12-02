package aoc

import aoc.util.*
import java.lang.String.format

/** Runs an AOC day. */
class AocRunner(
    val day: Int,
    val info: () -> List<String> = { listOf() },
    val test: () -> Any? = { null },
    val part1: () -> Any,
    val part2: () -> Any
) {

    constructor(day: Int, part1: Any, part2: Any): this(day,
        part1 = { part1 }, part2 = { part2 }
    )

    constructor(day: Int, info: String, part1: Any, part2: Any): this(day,
        info = { listOfNotNull(info) }, part1 = { part1 }, part2 = { part2 }
    )

    fun printBreak(size: Int) {
        println("•".repeat(size).alternateBlueWhite())
    }

    fun run() {
        val testResult = test()
        val infoResult = info()

        val SIZE = 60
        printBreak(SIZE)
        println(ANSI_BOLD + "       ADVENT OF CODE 2024 DAY ${format("%02d", day)}".alternateBlueWhite())
        println()
        printSnowScene(SIZE)

        if (infoResult.isNotEmpty()) {
            printBreak(SIZE)
            infoResult.forEach { println("$ANSI_GRAY$it") }
        }

        if (testResult != null) {
            printBreak(SIZE)
            println("${ANSI_GRAY}Test Case")
            println("${ANSI_GRAY}Answer(s): $ANSI_BLUE${test()}")
        }

        printBreak(SIZE)
        println("${ANSI_WHITE}Part 1")
        println("${ANSI_WHITE}Answer: $ANSI_LIGHT_BLUE${part1()}")

        printBreak(SIZE)
        println("${ANSI_WHITE}Part 2")
        println("${ANSI_WHITE}Answer: $ANSI_LIGHT_BLUE${part2()}")

        printBreak(SIZE)
    }
}