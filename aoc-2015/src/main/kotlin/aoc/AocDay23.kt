package aoc

class AocDay23: AocDay(23) {
    companion object { @JvmStatic fun main(args: Array<String>) { AocDay23().run() } }

    override val testinput = """
        inc a
        jio a, +2
        tpl a
        inc a
    """.trimIndent().lines()

    fun String.parse(): (State) -> Unit = when (this) {
        "inc a" -> ::incA
        "inc b" -> ::incB
        "tpl a" -> ::tplA
        "tpl b" -> ::tplB
        "hlf a" -> ::hlfA
        "hlf b" -> ::hlfB
        "jmp +2" -> { state -> state.line += 2-1 }
        "jmp -7" -> { state -> state.line += -7-1 }
        "jmp +19" -> { state -> state.line += 19-1 }
        "jio a, +22" -> { state -> if (state.a == 1) state.line += 22-1  }
        "jio a, +8" -> { state -> if (state.a == 1) state.line += 8-1 }
        "jio a, +2" -> { state -> if (state.a == 1) state.line += 2-1 }
        "jie a, +4" -> { state -> if (state.a % 2 == 0) state.line += 4-1 }
        else -> throw IllegalArgumentException("Unknown instruction: $this")
    }

    class State(var a: Int = 0, var b: Int = 0, var line: Int = 0)

    fun incA(state: State) { state.a++ }
    fun incB(state: State) { state.b++ }
    fun tplA(state: State) { state.a *= 3 }
    fun tplB(state: State) { state.b *= 3 }
    fun hlfA(state: State) { state.a /= 2 }
    fun hlfB(state: State) { state.b /= 2 }

    override fun calc1(input: List<String>): Int {
        val instructs = input.map { it.parse() }
        val state = State()
        while (state.line < instructs.size) {
            instructs[state.line](state)
            state.line++
        }
        return state.b
    }
    override fun calc2(input: List<String>): Int {
        val instructs = input.map { it.parse() }
        val state = State(a = 1)
        while (state.line < instructs.size) {
            instructs[state.line](state)
            state.line++
        }
        return state.b
    }
}