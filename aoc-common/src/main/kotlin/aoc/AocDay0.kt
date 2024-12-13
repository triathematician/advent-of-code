package aoc

class AocDay0: AocDay(0, 2015) {
    companion object { @JvmStatic fun main(args: Array<String>) { AocDay0().run() } }

    override val testinput = """
        
    """.trimIndent().lines()

    override fun calc1(input: List<String>) = null
    override fun calc2(input: List<String>) = null
}