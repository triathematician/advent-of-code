package aoc.aoc2016

import aoc.AocDay
import aoc.util.between
import aoc.util.eachCount

class AocDay4: AocDay(4, 2016) {
    companion object { @JvmStatic fun main(args: Array<String>) { AocDay4().run() } }

    override val testinput = """
        aaaaa-bbb-z-y-x-123[abxyz]
        a-b-c-d-e-f-g-h-987[abcde]
        not-a-real-room-404[oarel]
        totally-real-room-200[decoy]
    """.trimIndent().lines()

    class Room(val encrypted: String, val id: Int, val checksum: String) {
        fun real(): Boolean {
            val chars = (encrypted.toList().eachCount() - '-')
                .entries.sortedWith(compareBy({ -it.value }, { it.key }))
                .take(5)
                .joinToString("") { it.key.toString() }
            return chars == checksum
        }
        fun decode(): String {
            val shift = id % 26
            return encrypted.map {
                if (it == '-') ' '
                else ((it - 'a' + shift) % 26 + 'a'.toInt()).toChar()
            }.joinToString("")
        }
    }

    fun String.parse() = Room(
        substringBefore('[').substringBeforeLast('-'),
        substringBefore('[').substringAfterLast('-').toInt(),
        between("[", "]")
    )

    override fun calc1(input: List<String>) =
        input.map { it.parse() }
            .filter { it.real() }
            .sumOf { it.id }

    override fun calc2(input: List<String>): Int {
        var result = 0
        input.forEach {
            val r = it.parse()
            if ("northpole" in r.decode()) {
                println("${r.decode()} - ${r.id}")
                result = r.id
            }
        }
        return result
    }
}