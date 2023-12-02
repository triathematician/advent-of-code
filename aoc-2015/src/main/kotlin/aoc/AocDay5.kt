package aoc

import aoc.util.vowel

class AocDay5: AocDay(5) {
    companion object { @JvmStatic fun main(args: Array<String>) { AocDay5().run() } }

    override val testinput = """
        ugknbfddgicrmopn
        aaa
        jchzalrnumimnmhp
        haegwjzuvuyypxyu
        dvszwmarrgswjxmb
        qjhvhtzxzqqjkmpb
        xxyxx
        uurcxstgmygtbstg
        ieodomkazucvgmuy
    """.trimIndent().lines()

    override fun calc1(input: List<String>) = input.count { word ->
        word.count { it.vowel } >= 3 &&
            ('a'..'z').any { "$it$it" in word } &&
            !listOf("ab", "cd", "pq", "xy").any { it in word }
    }
    override fun calc2(input: List<String>) = input.count { word ->
        ".*([a-z][a-z]).*\\1.*".toRegex().matches(word) &&
            ".*([a-z])[a-z]\\1.*".toRegex().matches(word)
    }
}