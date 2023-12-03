package aoc

import aoc.util.chunk

class AocDay9: AocDay(9) {
    companion object { @JvmStatic fun main(args: Array<String>) { AocDay9().run() } }

    override val testinput = """
        London to Dublin = 464
        London to Belfast = 518
        Dublin to Belfast = 141
    """.trimIndent().lines()

    fun List<String>.parse() = map {
        Triple(it.chunk(0), it.chunk(2), it.chunk(4).toInt())
    }

    override fun calc1(input: List<String>): Int {
        val parse = input.parse()
        val cities = parse.flatMap { setOf(it.first, it.second) }.toSet()
        val paths = cities.associateWith { c ->
            (parse.filter { it.first == c }.map { it.second to it.third } +
            parse.filter { it.second == c }.map { it.first to it.third }).toMap()
        }
        return cities.minOf {
            shortestPathThroughRemainder(it, paths, setOf(it))
        }
    }

    private fun shortestPathThroughRemainder(start: String, paths: Map<String, Map<String, Int>>, visited: Set<String>): Int {
        if (visited.size == paths.size)
            return 0
        return paths.keys.filter { it !in visited }.minOf {
            paths[start]!![it]!! + shortestPathThroughRemainder(it, paths, visited + it)
        }
    }

    override fun calc2(input: List<String>): Int {
        val parse = input.parse()
        val cities = parse.flatMap { setOf(it.first, it.second) }.toSet()
        val paths = cities.associateWith { c ->
            (parse.filter { it.first == c }.map { it.second to it.third } +
                    parse.filter { it.second == c }.map { it.first to it.third }).toMap()
        }
        return cities.maxOf {
            longestPathThroughRemainder(it, paths, setOf(it))
        }
    }

    private fun longestPathThroughRemainder(start: String, paths: Map<String, Map<String, Int>>, visited: Set<String>): Int {
        if (visited.size == paths.size)
            return 0
        return paths.keys.filter { it !in visited }.maxOf {
            paths[start]!![it]!! + longestPathThroughRemainder(it, paths, visited + it)
        }
    }

}