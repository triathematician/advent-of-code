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

    fun printBreak() {
        println("•".repeat(40).alternateBlueWhite())
    }

    fun run() {
        val testResult = test()
        val infoResult = info()

        printBreak()
        println(ANSI_BOLD + "       ADVENT OF CODE 2023 DAY ${format("%02d", day)}".alternateBlueWhite())
        printSnowScene()

        if (infoResult.isNotEmpty()) {
            printBreak()
            infoResult.forEach { println("$ANSI_WHITE$it") }
        }

        if (testResult != null) {
            printBreak()
            println("${ANSI_WHITE}Test code")
            println("${ANSI_WHITE}Answer: $ANSI_LIGHTBLUE${test()}")
        }

        printBreak()
        println("${ANSI_WHITE}Part 1")
        println("${ANSI_WHITE}Answer: $ANSI_LIGHTBLUE${part1()}")

        printBreak()
        println("${ANSI_WHITE}Part 2")
        println("${ANSI_WHITE}Answer: $ANSI_LIGHTBLUE${part2()}")

        printBreak()
    }
}