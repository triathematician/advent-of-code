package aoc

import com.fasterxml.jackson.databind.ObjectMapper

class AocDay12: AocDay(12) {
    companion object { @JvmStatic fun main(args: Array<String>) { AocDay12().run() } }

    override val testinput = """
        {"x":[1,{"c":"red","b":2},3]}
    """.trimIndent().lines()

    fun String.parse(): Any =
        if (startsWith("["))
            ObjectMapper().readValue(this, List::class.java)
        else
            ObjectMapper().readValue(this, Map::class.java)

    fun Collection<*>.sum(zeroRed: Boolean = false): Int = sumOf {
        when (it) {
            is Int -> it
            is String -> 0
            is List<*> -> it.sum(zeroRed)
            is Map<*, *> -> if (zeroRed && "red" in it.values) 0 else it.values.sum(zeroRed)
            else -> throw RuntimeException("Unknown type: $it")
        }
    }

    override fun calc1(input: List<String>) =
        (input[0].parse() as Map<*, *>).values.sum()
    override fun calc2(input: List<String>) =
        (input[0].parse() as Map<*, *>).values.sum(zeroRed = true)
}