package aoc

class AocDay20: AocDay(20) {
    companion object { @JvmStatic fun main(args: Array<String>) { AocDay20().run() } }

    override val testinput = """
        200
    """.trimIndent().lines()

    /** All divisors, including 1 and self. */
    private fun Int.divisors(): List<Int> {
        val res = mutableListOf(1)
        for (i in 2..minOf(this-1, (this/2+1))) {
            if (this % i == 0) res += i
        }
        res += this
        return res
    }

    override fun calc1(input: List<String>): Int {
        // we know we want a large number of divisors, so can speed things up by looking for multiples of 120
        val target = input[0].toInt()
        generateSequence(120) { it + 120 }.map {
            if (it % 100000 < 120)
                println(it)
            it to it.divisors().sum() * 10
        }.first {
            it.second >= target
        }.let {
            println("House ${it.first} gets ${it.second} presents")
            println("   delivered from ${it.first.divisors()}")
            return it.first
        }
    }
    override fun calc2(input: List<String>): Int {
        // we know we want a large number of divisors, so can speed things up by looking for multiples of 120
        val target = input[0].toInt()
        val nums = IntArray(target/10)
        (1..target/10).forEach { elf ->
            if (elf % 1000000 == 0)
                println("Elf $elf is delivering their presents.")
            (1..50).forEach {
                if (elf*it-1 < nums.size) {
                    nums[elf * it - 1] += elf * 11
                }
            }
        }
        val i = nums.indexOfFirst { it >= target } + 1
        println("House $i gets at least ${nums[i - 1]} presents")
        println("   delivered from ${i.divisors().filter { it >= i / 50 }}")
        return i
    }
}