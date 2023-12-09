package aoc

class AocDay24: AocDay(24) {
    companion object { @JvmStatic fun main(args: Array<String>) { AocDay24().run() } }

    override val testinput = """
        1
        2
        3
        4
        5
        7
        8
        9
        10
        11
    """.trimIndent().lines()

    fun List<String>.parse() = map { it.toInt() }

    override fun calc1(input: List<String>): Long {
        return 0
        val pkgs = input.parse().toSet()
        val target = pkgs.sum() / 3
        val combos = pkgs.subsetsSummingTo(target)
        val combosBySize = combos.groupBy { it.size }
        combosBySize.entries.sortedBy { it.key }.forEach { (count, combos) ->
            print("Combos of size $count: ${combos.size}")
            val valid = combos.filter { (pkgs - it.toSet()).dividesIntoGroups(target, 2) }
            println(", valid: ${valid.size}")
            if (valid.isNotEmpty())
                return valid.minOf { it.fold(1L) { acc, i -> acc * i } }
        }
        return -1
    }

    override fun calc2(input: List<String>): Long {
        val pkgs = input.parse().toSet()
        val target = pkgs.sum() / 4
        val combos = pkgs.subsetsSummingTo(target, maxSize = 7)
        val combosBySize = combos.groupBy { it.size }
        combosBySize.entries.sortedBy { it.key }.forEach { (count, combos) ->
            print("Combos of size $count: ${combos.size}")
            val valid = combos.filter { (pkgs - it.toSet()).dividesIntoGroups(target, 3) }
            println(", valid: ${valid.size}")
            if (valid.isNotEmpty())
                return valid.minOf { it.fold(1L) { acc, i -> acc * i } }
        }
        return -1
    }

    fun Set<Int>.dividesIntoGroups(target: Int, groups: Int): Boolean {
        if (groups == 1)
            return sum() == target
        else if (groups == 2)
            return subsetsSummingTo(target).isNotEmpty()
        else {
            val s2 = subsetsSummingTo(target)
            return s2.any { (this - it.toSet()).dividesIntoGroups(target, 2) }
        }
    }

    fun Collection<Int>.subsetsSummingTo(target: Int, maxSize: Int? = null): List<List<Int>> {
        if (isEmpty())
            return listOf()

        val first = first()
        val withFirst = when {
            first < target -> drop(1).subsetsSummingTo(target - first, maxSize?.let { it - 1}).map { it + first }
            first == target -> listOf(listOf(first))
            else -> listOf()
        }
        return withFirst + drop(1).subsetsSummingTo(target, maxSize)
    }
}