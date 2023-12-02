package aoc

class AocDay2: AocDay(2) {
    companion object { @JvmStatic fun main(args: Array<String>) { AocDay2().run() } }

    override val testinput = """
        2x3x4
        1x1x10
    """.trimIndent().lines()

    fun List<String>.parse() = map {
        it.split("x").map { it.toInt() }.sorted()
    }

    override fun calc1(input: List<String>) = input.parse().sumOf {
        3*it[0]*it[1] + 2*it[1]*it[2] + 2*it[0]*it[2]
    }
    override fun calc2(input: List<String>) = input.parse().sumOf {
        2*(it[0]+it[1]) + it[0]*it[1]*it[2]
    }
}