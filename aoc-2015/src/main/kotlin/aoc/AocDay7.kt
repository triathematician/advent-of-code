package aoc

class AocDay7: AocDay(7) {
    companion object { @JvmStatic fun main(args: Array<String>) { AocDay7().run() } }

    override val testinput = """
        123 -> x
        456 -> y
        x AND y -> d
        x OR y -> e
        x LSHIFT 2 -> f
        y RSHIFT 2 -> g
        NOT x -> h
        NOT y -> i
    """.trimIndent().lines()

    data class Op(val in1: String, val in2: String, val op: OpType, val tgt: String) {
        var value: Int? = null
        fun calc(table: Map<String, Op>): Int =
            value ?: try {
                calcValue(table).also { value = it }
            } catch (x: NullPointerException) {
                println("NPE on $this")
                throw x
            }
        fun calcValue(table: Map<String, Op>): Int {
            val v1 = in1.toIntOrNull() ?: table[in1]!!.calc(table)
            val v2 = if (in2.isEmpty()) 0 else in2.toIntOrNull() ?: table[in2]!!.calc(table)
            return when (op) {
                OpType.ASSIGN -> v1
                OpType.NOT -> v1.inv()
                OpType.AND -> v1 and v2
                OpType.OR -> v1 or v2
                OpType.LSHIFT -> v1 shl v2
                OpType.RSHIFT -> v1 shr v2
            }
        }
    }

    enum class OpType { AND, OR, LSHIFT, RSHIFT, NOT, ASSIGN }

    fun List<String>.opTable() = associate {
        val parts = it.split(" ")
        parts.last() to when (parts.size) {
            3 -> Op(parts[0], "", OpType.ASSIGN, parts[2])
            4 -> Op(parts[1], "", OpType.NOT, parts[3])
            5 -> Op(parts[0], parts[2], OpType.valueOf(parts[1].toUpperCase()), parts[4])
            else -> throw Exception("Invalid input: $it")
        }
    }

    override fun calc1(input: List<String>): Int {
        val wire = if (input.size < 50) "i" else "a"
        val opTable = input.opTable()
        return opTable[wire]!!.calc(opTable).let {
            if (it < 0) 65536 + it else it
        }
    }
    override fun calc2(input: List<String>): Int {
        val wire = if (input.size < 50) "i" else "a"
        val signal = calc1(input)
        val opTable = input.opTable().toMutableMap()
        opTable["b"] = Op(signal.toString(), "", OpType.ASSIGN, "b")
        return opTable[wire]!!.calc(opTable).let {
            if (it < 0) 65536 + it else it
        }
    }
}