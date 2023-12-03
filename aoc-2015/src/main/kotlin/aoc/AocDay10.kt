package aoc

import java.lang.StringBuilder

class AocDay10: AocDay(10) {
    companion object { @JvmStatic fun main(args: Array<String>) { AocDay10().run() } }

    override val testinput = """
        1
    """.trimIndent().lines()

    fun String.lookSay(): String {
        val res = StringBuilder()
        val ptr = StringPointer(this, 0)
        while (!ptr.done()) {
            val digit = ptr.cur()
            val count = ptr.advanceWhile { it == digit }
            res.append(count.toString() + digit)
        }
        return res.toString()
    }

    init {
        println("1".lookSay())
        println("11".lookSay())
        println("21".lookSay())
        println("1211".lookSay())
        println("111221".lookSay())
    }

    override fun calc1(input: List<String>): Int {
        var res = input[0]
        repeat(40) {
            res = res.lookSay()
        }
        return res.length
    }
    override fun calc2(input: List<String>): Int {
        var res = input[0]
        repeat(50) {
            res = res.lookSay()
        }
        return res.length
    }
}

class StringPointer(val str: String, var pos: Int) {
    fun cur() = str[pos]
    fun done() = pos >= str.length
    fun advanceWhile(predicate: (Char) -> Boolean): Int {
        var count = 0
        while (pos < str.length && predicate(cur())) {
            pos++
            count++
        }
        return count
    }
}