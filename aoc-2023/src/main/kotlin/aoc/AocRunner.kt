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
            infoResult.forEach { println("${ANSI_WHITE}$it") }
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

    private fun printChristmasTree() {
        (0..6).forEach {
            println((" ".repeat(11 - it) + "*".repeat(it*2+1)).alternateRedGreen())
        }
        println(" ".repeat(10) + ANSI_YELLOW + "|||" + ANSI_RESET)
    }

    private fun printSnowScene() {
        val array = Array(6) { Array(40) { " " } }
        val treeLocs = (1..6).map { (1..35).random() }
        treeLocs.forEach {
            val ht = listOf(1,2,2,3,3,3).random()
            array[ht][it] = "$ANSI_GREEN/"
            array[ht][it+1] = "$ANSI_GREEN\\"
            array[ht+1][it-1] = "$ANSI_GREEN/"
            array[ht+1][it] = "${ANSI_GREEN}_"
            array[ht+1][it+1] = "${ANSI_GREEN}_"
            array[ht+1][it+2] = "$ANSI_GREEN\\"
            array[ht+2][it] = "$ANSI_BROWN|"
            array[ht+2][it+1] = "$ANSI_BROWN|"
        }

        (0..5).onEach { line ->
            (0..39).forEach {
                val ch = array[line][it]
                if (ch == " ") {
                    array[line][it] = when ((0..1).random()) {
                        0 -> ANSI_LIGHTBLUE
                        else -> ANSI_WHITE
                    } + when ((0..9).random()) {
                        in 0..1 -> "*"
                        in 2..3 -> "."
                        else -> " "
                    }
                }
            }
        }
        array.forEach { println(it.joinToString("")) }
    }
}