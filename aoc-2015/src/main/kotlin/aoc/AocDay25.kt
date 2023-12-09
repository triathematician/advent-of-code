package aoc

class AocDay25: AocDay(25) {
    companion object { @JvmStatic fun main(args: Array<String>) { AocDay25().run() } }

    override val testinput = """
   |    1         2         3         4         5         6
---+---------+---------+---------+---------+---------+---------+
 1 | 20151125  18749137  17289845  30943339  10071777  33511524
 2 | 31916031  21629792  16929656   7726640  15514188   4041754
 3 | 16080970   8057251   1601130   7981243  11661866  16474243
 4 | 24592653  32451966  21345942   9380097  10600672  31527494
 5 |    77061  17552253  28094349   6899651   9250759  31663883
 6 | 33071741   6796745  25397450  24659492   1534922  27995004
    """.trimIndent().lines()

    val row = 2947
    val col = 3029
    val start = 20151125L
    val mult = 252533L
    val mod = 33554393L

    val sum = row+col
    fun seqNum(row: Int, col: Int) = (row+col-1)*(row+col-2)/2 + col

    init {
        println((1..6).map { code(1, it) })
        println((1..6).map { code(2, it) })
        println((1..6).map { code(3, it) })
    }

    override fun calc1(input: List<String>) = code(row, col)

    fun code(row: Int, col: Int): Long {
        val iters = seqNum(row, col) - 1
        var code = start
        (1..iters).forEach {
            code = (code * mult) % mod
        }
        return code
    }
    override fun calc2(input: List<String>) = null
}