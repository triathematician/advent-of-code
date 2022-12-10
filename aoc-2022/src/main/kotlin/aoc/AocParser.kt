package aoc

/** Universal parser for various possible inputs. */
class AocParser(_input: String) {

    var input = _input.trimIndent()
    val lines by lazy { input.lines() }

    var groupSep: String? = null
    var listSep: String? = null

    //region OUTPUT TYPES

    val pairChars: List<PairChar> by lazy { mapLines { it[0] to it[2] } }
    val charMatrix: List<List<Char>> by lazy { mapLines { it.toList() } }
    val intMatrix: List<List<Int>> by lazy { mapMatrix { it.toInt() } }
    val intrangeMatrix: List<List<IntRange>> by lazy { mapMatrix { it.toIntRange() } }

    //endregion

    //region MAPPERS

    fun <X> mapLines(op: (String) -> X): List<X> =
        lines.map(op)

    fun <X> mapMatrix(op: (String) -> X): List<List<X>> =
        input.split(groupSep!!).map { it.split(listSep!!).map(op) }

    //endregion

    companion object {
        val String.parse
            get() = AocParser(this)
        val String.parselines
            get() = AocParser(this).lines

        fun String.groups() = groups("\n", ",")

        fun String.groups(groupSep: String, listSep: String) = AocParser(this).apply {
            this.groupSep = groupSep
            this.listSep = listSep
        }
    }
}

/** Decodes content as a matrix. */
class AocParserMatrix(val parser: AocParser) {
    val ints: List<List<Int>>
        get() = parser.mapMatrix { it.toInt() }
}