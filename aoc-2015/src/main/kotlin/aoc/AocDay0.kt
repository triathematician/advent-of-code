package aoc

abstract class AocDay(val day: Int, val year: Int = 2015) {

    abstract val testinput: String
    abstract val input: String

    abstract fun test1(): Any
    abstract fun test2(): Any
    abstract fun part1(): Any
    abstract fun part2(): Any

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

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            AocRunner(day,
                info = { listOf("Leaderboard: ", "Answers: ") },
                test = { "$testResult, $testResult2" },
                part1 = { answer1 },
                part2 = { answer2 }
            ).run()
        }
    }
}