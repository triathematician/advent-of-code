package aoc

import aoc.util.chunk

class AocDay19: AocDay(19) {
    companion object { @JvmStatic fun main(args: Array<String>) { AocDay19().run() } }

    override val testinput = """
        H => HO
        H => OH
        O => HH
        e => H
        e => O
        
        HOHOHO
    """.trimIndent().lines()

    fun String.parse() = chunk(0) to chunk(2)
        .replace("Rn","(").replace("Ar",")")

    override fun calc1(input: List<String>): Int {
        val reps = input.dropLast(2).map { it.parse() }
        val start = input.last()
        val results = mutableSetOf<String>()
        for ((from, to) in reps) {
            for (i in start.indices) {
                if (start.startsWith(from, i)) {
                    results.add(start.replaceRange(i, i + from.length, to))
                }
            }
        }
        return results.size
    }

    override fun calc2(input: List<String>): Int {
        val target = input.last()
        // parenthetical statements don't count to overall total ("Rn..Ar")
        val parentheticals = "Rn".toRegex().findAll(target).count()
        // anytime Y shows up it is next to an F and inside parentheses
        val ys = target.count { it == 'Y' }
        // now we count the number of capital letters to see how many elements are added from the start
        val caps = target.count { it.isUpperCase() }

        // subtract 2 for each parenthetical occurence, 2 for each Y/F occurence, and 1 since the first step gives us 2 elements
        return caps - parentheticals*2 - ys*2 - 1
    }

    //region INITIAL ATTEMPT - DOESN'T SCALE
    fun calc2b(input: List<String>): Int? {
        val reps = input.dropLast(2).map { it.parse() }
        val target = input.last()
        val terminal = reps.filter { it.first == "e" }.map { it.second }.toSet()
        val nonTerminal = reps.filter { it.first != "e" }
        return minToGet(nonTerminal, terminal, target)
    }

    fun minToGet(reps: List<Pair<String, String>>, terminal: Set<String>, target: String): Int? {
        if (target in terminal) return 1
        return reps.mapNotNull { (from, to) ->
            to.toRegex().findAll(target).mapNotNull {
                val newEnd = target.replaceRange(it.range, from)
                val min = minToGet(reps, terminal, newEnd)
                min?.let { it + 1 }
            }.minOrNull()
        }.minOrNull()
    }
    //endregion

}