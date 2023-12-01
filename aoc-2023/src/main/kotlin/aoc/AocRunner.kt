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

    fun run() {
        val testResult = test()
        val infoResult = info()

        println("•".repeat(23).alternateRedGreen())
        println(ANSI_BOLD + " ADVENT OF CODE DAY ${format("%02d", day)}".alternateRedGreen())
        printChristmasTree()

        if (infoResult.isNotEmpty()) {
            println("•".repeat(23).alternateRedGreen())
            infoResult.forEach { println("${ANSI_WHITE}$it") }
        }

        if (testResult != null) {
            println("•".repeat(23).alternateRedGreen())
            println("${ANSI_WHITE}Test code")
            println("${ANSI_WHITE}Answer: ${test()}")
        }

        println("•".repeat(23).alternateRedGreen())
        println("${ANSI_GREEN}Part 1")
        println("${ANSI_RED}Answer: $ANSI_BRIGHT_YELLOW${part1()}")

        println("•".repeat(23).alternateRedGreen())
        println("${ANSI_GREEN}Part 2")
        println("${ANSI_RED}Answer: $ANSI_BRIGHT_YELLOW${part2()}")

        println("•".repeat(23).alternateRedGreen())
    }

    private fun printChristmasTree() {
        (0..6).forEach {
            println((" ".repeat(11 - it) + "*".repeat(it*2+1)).alternateRedGreen())
        }
        println(" ".repeat(10) + ANSI_YELLOW + "|||" + ANSI_RESET)
    }
}