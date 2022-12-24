package aoc.util

fun printChristmasTree() {
    (0..6).forEach {
        println((" ".repeat(11 - it) + "*".repeat(it*2+1)).alternateRedGreen())
    }
    println(" ".repeat(10) + ANSI_YELLOW + "|||" + ANSI_RESET)
}