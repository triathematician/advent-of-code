package aoc.aoc2025

import aoc.AocDay

class AocDay1: AocDay(1, 2025) {
    companion object { @JvmStatic fun main(args: Array<String>) { AocDay1().run() } }

    override val testinput = """
        L68
        L30
        R48
        L5
        R60
        L55
        L1
        L99
        R14
        L82
    """.trimIndent().lines()

    override fun calc1(input: List<String>): Int {
        var state = State(50, 0)
        for (line in input)
            state = calc1(line, state)
        return state.combo
    }

    fun calc1(line: String, state: State): State {
        val left = line[0] == 'L'
        val dist = line.substring(1).toInt()
        val newStart = (if (left) 100 + state.pos - dist else state.pos + dist) % 100
        return State(newStart, state.combo + if (newStart == 0) 1 else 0)
    }

    override fun calc2(input: List<String>): Int {
        var state = State(50, 0)
        for (line in input)
            state = calc2(line, state)
        return state.combo
    }

    fun calc2(line: String, state: State): State {
        val left = line[0] == 'L'
        val dist = line.substring(1).toInt()
        val num0 = if (left) {
            (100 - state.pos + dist) / 100 + if (state.pos == 0) -1 else 0
        } else {
            (state.pos + dist) / 100
        }
        val newStart = (if (left) state.pos - dist else state.pos + dist).mod(100)
        print("Started at ${state.pos}, rotated $line to point at $newStart")
        if (num0 > 1 && newStart == 0) {
            println(", passing [0] ${num0-1} times and ending at [0], for a total of $num0 times!")
        } else if (newStart == 0) {
            println("!!!!!")
        } else if (num0 > 0) {
            println(", passing [0] $num0 times!")
        } else {
            println(".")
        }
        return State(newStart, state.combo + num0)
    }
}

class State(var pos: Int, var combo: Int)