package aoc

class AocDay8: AocDay(8) {
    companion object { @JvmStatic fun main(args: Array<String>) { AocDay8().run() } }

    override val testinput = """
        ""
        "abc"
        "aaa\"aaa"
        "\x27"
    """.trimIndent().lines()

    fun String.charCount(): Int {
        var i = 0
        var count = 0
        while (i < length) {
            val str = substring(i).take(2)
            when {
                str == "\\\\" -> { i += 2; count++ }
                str == "\\\"" -> { i += 2; count++ }
                str == "\\x" -> { i += 4; count++ }
                else -> { i++; count++ }
            }
        }
        return count - 2
    }

    fun String.charCount2() = sumOf {
        when (it) {
            '\\' -> 2
            '"' -> 2
            else -> 1
        } as Int
    } + 2

    override fun calc1(input: List<String>) = input.sumOf { it.length - it.charCount() }
    override fun calc2(input: List<String>) = input.sumOf { it.charCount2() - it.length }
}