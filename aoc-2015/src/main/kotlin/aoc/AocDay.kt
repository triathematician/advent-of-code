package aoc

import aoc.util.*
import java.lang.String.format

abstract class AocDay(val day: Int, val year: Int = 2015) {

    abstract val testinput: String
    abstract val input: String
    open val info: String = ""

    abstract fun calc1(input: String): Any?
    abstract fun calc2(input: String): Any?

    fun test1() = calc1(testinput)
    fun test2() = calc2(testinput)
    fun part1() = calc1(input)
    fun part2() = calc2(input)

    fun run() {
        val testResult = test1()

        println("•".repeat(23).alternateRedGreen())
        println(ANSI_BOLD + " ADVENT OF CODE DAY ${format("%02d", day)}".alternateRedGreen())
        printChristmasTree()

        if (info.isNotEmpty()) {
            println("•".repeat(23).alternateRedGreen())
            info.forEach { println("${ANSI_WHITE}$it") }
        }

        println("•".repeat(23).alternateRedGreen())
        println("${ANSI_WHITE}Test Answers: $testResult, ${test2()}")

        println("•".repeat(23).alternateRedGreen())
        println("${ANSI_GREEN}Part 1 ${ANSI_RED}Answer: $ANSI_BRIGHT_YELLOW${part1()}")
        println("${ANSI_GREEN}Part 2 ${ANSI_RED}Answer: $ANSI_BRIGHT_YELLOW${part2()}")

        println("•".repeat(23).alternateRedGreen())
    }
}