package aoc

/** https://adventofcode.com/2015/day/11 */
class AocDay11: AocDay(11) {
    companion object { @JvmStatic fun main(args: Array<String>) { AocDay11().run() } }

    override val testinput = """
        abcdefgh
    """.trimIndent().lines()

    fun String.valid() =
        increasingRuns() >= 1 &&
        !contains("[iol]".toRegex()) &&
        doubles()
    fun String.increasingRuns() = (0..length - 3).count {
        this[it + 1] == this[it] + 1 && this[it + 2] == this[it] + 2
    }
    fun String.doubles() = (0..length - 2).filter {
        this[it + 1] == this[it]
    }.let {
        it.isNotEmpty() && it.max() - it.min() >= 2
    }

    fun String.increment(): String = if (last() == 'z') {
        dropLast(1).increment() + 'a'
    } else {
        dropLast(1) + (last() + 1)
    }

    override fun calc1(input: List<String>): String {
        var res = input[0].increment()
        while (!res.valid()) {
            res = res.increment()
        }
        return res
    }
    override fun calc2(input: List<String>) = calc1(listOf(calc1(input)))
}