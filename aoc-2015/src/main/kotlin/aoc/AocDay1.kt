package aoc

import aoc.util.cusum
import aoc.util.indexOfFirstNegative

class AocDay1: AocDay(1) {
    companion object { @JvmStatic fun main(args: Array<String>) { AocDay1().run() } }

    override val testinput = "))(((((".lines()

    fun score(it: Char) = if (it == '(') 1 else -1
    fun String.score() = map { score(it) }

    override fun calc1(input: List<String>) = input[0].score().sum()
    override fun calc2(input: List<String>) = input[0].score().cusum().indexOfFirstNegative() + 1
}