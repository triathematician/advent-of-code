package aoc

class AocDay17: AocDay(17) {
    companion object { @JvmStatic fun main(args: Array<String>) { AocDay17().run() } }

    override val testinput = """
        20
        15
        10
        5
        5
    """.trimIndent().lines()

    fun count(sizes: List<Int>, target: Int): List<List<Int>>? = when {
        target < 0 || sizes.sum() < target -> null
        sizes.sum() == target -> listOf(sizes)
        target == 0 -> listOf(listOf())
        else -> {
            val s1 = count(sizes.drop(1), target) // without first bin
            val s2 = count(sizes.drop(1), target - sizes[0]) // with first bin
            (s1 ?: listOf()) + (s2?.map { it + sizes[0] } ?: listOf())
        }
    }

    override fun calc1(input: List<String>): Int {
        val target = if (input.size == 5) 25 else 150
        val nums = input.map { it.toInt() }
        return count(nums, target)!!.count()
    }
    override fun calc2(input: List<String>): Int {
        val target = if (input.size == 5) 25 else 150
        val nums = input.map { it.toInt() }
        val sets = count(nums, target)!!
        val minSize = sets.minOf { it.count() }
        return sets.count { it.count() == minSize }
    }
}