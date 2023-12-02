package aoc

import aoc.util.*
import java.io.File
import java.lang.String.format
import java.net.HttpURLConnection
import java.net.HttpURLConnection.HTTP_OK
import java.net.URL

abstract class AocDay(val day: Int, val year: Int = 2015) {

    abstract val testinput: List<String>

    val input = getDayInput(day, year)
    open val info: String = ""

    abstract fun calc1(input: List<String>): Any?
    abstract fun calc2(input: List<String>): Any?

    fun test1() = calc1(testinput)
    fun test2() = calc2(testinput)
    fun part1() = calc1(input)
    fun part2() = calc2(input)

    fun run() {
        getDayInput(day, year)
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