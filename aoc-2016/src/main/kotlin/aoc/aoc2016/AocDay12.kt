package aoc.aoc2016

import aoc.AocDay

class AocDay12: AocDay(12, 2016) {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            AocDay12().run()
        }
    }

    override val testinput = """
        cpy 41 a
        inc a
        inc a
        dec a
        jnz a 2
        dec a
    """.trimIndent().lines()

    class State(
        var regs: MutableMap<String, Int> = mutableMapOf("a" to 0, "b" to 0, "c" to 0, "d" to 0),
        var pos: Int = 0
    ) {
        fun apply(s: String) {
            val spl = s.split(" ")
            when (spl[0]) {
                "cpy" -> regs[spl[2]] = regs[spl[1]] ?: spl[1].toInt()
                "inc" -> regs[spl[1]] = regs[spl[1]]!! + 1
                "dec" -> regs[spl[1]] = regs[spl[1]]!! - 1
                "jnz" -> if ((regs[spl[1]] ?: spl[1].toInt()) != 0) {
                    pos += regs[spl[2]] ?: spl[2].toInt()
                } else {
                    pos++
                }
            }
            if (spl[0] != "jnz")
                pos++
        }
    }

    override fun calc1(input: List<String>): Int {
        val state = State()
        while (state.pos < input.size) {
            state.apply(input[state.pos])
        }
        return state.regs["a"]!!
    }

    override fun calc2(input: List<String>): Int {
        val state = State(regs = mutableMapOf("a" to 0, "b" to 0, "c" to 1, "d" to 0))
        while (state.pos < input.size) {
            state.apply(input[state.pos])
        }
        return state.regs["a"]!!
    }
}