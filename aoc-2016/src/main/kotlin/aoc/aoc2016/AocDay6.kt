package aoc.aoc2016

import aoc.AocDay
import aoc.util.eachCount

class AocDay6: AocDay(6, 2016) {
    companion object { @JvmStatic fun main(args: Array<String>) { AocDay6().run() } }

    override val testinput = """
        eedadn
        drvtee
        eandsr
        raavrd
        atevrs
        tsrnev
        sdttsa
        rasrtv
        nssdts
        ntnada
        svetve
        tesnvt
        vntsnd
        vrdear
        dvrsen
        enarar
    """.trimIndent().lines()

    override fun calc1(input: List<String>): String {
        val len = input.first().length
        return (0 until len).map { i ->
            input.map { it[i] }.eachCount().maxBy { it.value }!!.key
        }.joinToString("")
    }
    override fun calc2(input: List<String>): String {
        val len = input.first().length
        return (0 until len).map { i ->
            input.map { it[i] }.eachCount().minBy { it.value }!!.key
        }.joinToString("")
    }
}