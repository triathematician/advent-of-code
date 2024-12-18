package aoc.aoc2016

import aoc.AocDay
import aoc.util.Hashes.md5

class AocDay5: AocDay(5, 2016) {
    companion object { @JvmStatic fun main(args: Array<String>) { AocDay5().run() } }

    override val testinput = """
        abc
    """.trimIndent().lines()

    override fun calc1(input: List<String>): String {
        val inpt = input[0]
        val res = mutableListOf<Char>()
        (0..Int.MAX_VALUE).forEach {
            val hash = (inpt + it).md5()
            if (hash.startsWith("00000")) {
                println("Found zeros at $it with hash $hash")
                res.add(hash[5])
                if (res.size == 8) {
                    val final = res.joinToString("")
                    println("Final password: $final")
                    return final
                }
            }
        }
        error("No password found")
    }

    override fun calc2(input: List<String>): String {
        val inpt = input[0]
        val res = sortedMapOf<Int, Char>()
        (0..Int.MAX_VALUE).forEach {
            val hash = (inpt + it).md5()
            if (hash.startsWith("00000")) {
                println("Found zeros at $it with hash $hash")
                if (hash[5] in '0'..'7') {
                    val pos = hash[5].toString().toInt()
                    if (pos !in res) {
                        res[pos] = hash[6]
                        println("  found char ${hash[6]} at position $pos")
                        if (res.size == 8) {
                            val final = res.values.joinToString("")
                            println("Final password: $final")
                            return final
                        }
                    }
                }
            }
        }
        error("No password found")
    }
}