package aoc

import aoc.util.splitInto
import aoc.util.parsePath
import aoc.util.walk

class AocDay3: AocDay(3) {
    companion object { @JvmStatic fun main(args: Array<String>) { AocDay3().run() } }

    override val testinput = "^>v<".lines()

    override fun calc1(input: List<String>) = input[0].parsePath()
        .walk().toSet().size
    override fun calc2(input: List<String>) = input[0].parsePath()
        .splitInto(2).flatMap { it.walk().toSet() }.toSet().size
}