import aoc.*

val input1="""
            [J] [Z] [G]            
            [Z] [T] [S] [P] [R]    
[R]         [Q] [V] [B] [G] [J]    
[W] [W]     [N] [L] [V] [W] [C]    
[F] [Q]     [T] [G] [C] [T] [T] [W]
[H] [D] [W] [W] [H] [T] [R] [M] [B]
[T] [G] [T] [R] [B] [P] [B] [G] [G]
[S] [S] [B] [D] [F] [L] [Z] [N] [L]
 1   2   3   4   5   6   7   8   9 
""".trimIndent().lines()

val input2="""
move 4 from 2 to 1
move 1 from 6 to 9
move 6 from 4 to 7
move 1 from 2 to 5
move 3 from 6 to 3
move 4 from 3 to 9
move 2 from 1 to 3
move 6 from 7 to 5
move 5 from 7 to 6
move 6 from 8 to 7
move 6 from 7 to 6
move 1 from 8 to 3
move 15 from 6 to 4
move 7 from 5 to 6
move 1 from 7 to 2
move 2 from 5 to 3
move 5 from 9 to 8
move 5 from 5 to 6
move 1 from 7 to 4
move 5 from 6 to 5
move 3 from 3 to 8
move 4 from 5 to 8
move 1 from 2 to 8
move 7 from 1 to 2
move 2 from 6 to 2
move 2 from 5 to 8
move 1 from 1 to 8
move 8 from 2 to 6
move 3 from 3 to 4
move 4 from 9 to 3
move 5 from 3 to 6
move 5 from 6 to 8
move 3 from 4 to 8
move 13 from 6 to 5
move 14 from 4 to 8
move 1 from 2 to 6
move 1 from 4 to 2
move 12 from 5 to 4
move 30 from 8 to 6
move 1 from 8 to 9
move 1 from 9 to 4
move 15 from 4 to 5
move 1 from 2 to 9
move 1 from 4 to 2
move 1 from 2 to 1
move 1 from 9 to 3
move 8 from 5 to 7
move 2 from 5 to 6
move 7 from 8 to 1
move 1 from 3 to 4
move 1 from 7 to 3
move 1 from 4 to 6
move 26 from 6 to 7
move 1 from 3 to 7
move 3 from 7 to 2
move 1 from 1 to 9
move 16 from 7 to 5
move 2 from 7 to 4
move 12 from 7 to 6
move 1 from 1 to 9
move 4 from 6 to 1
move 7 from 1 to 5
move 2 from 1 to 8
move 1 from 7 to 2
move 1 from 1 to 4
move 2 from 4 to 5
move 1 from 9 to 4
move 3 from 6 to 9
move 8 from 6 to 5
move 5 from 5 to 9
move 19 from 5 to 8
move 1 from 9 to 8
move 3 from 8 to 7
move 1 from 7 to 3
move 8 from 5 to 2
move 2 from 4 to 2
move 4 from 9 to 8
move 1 from 2 to 3
move 2 from 3 to 2
move 4 from 9 to 5
move 8 from 8 to 4
move 9 from 8 to 5
move 5 from 8 to 4
move 5 from 5 to 7
move 12 from 2 to 3
move 2 from 2 to 8
move 1 from 8 to 6
move 1 from 8 to 7
move 10 from 4 to 3
move 1 from 2 to 9
move 13 from 5 to 3
move 1 from 7 to 5
move 27 from 3 to 4
move 1 from 8 to 7
move 3 from 5 to 2
move 6 from 6 to 3
move 2 from 4 to 1
move 27 from 4 to 2
move 2 from 7 to 8
move 23 from 2 to 4
move 2 from 1 to 4
move 2 from 7 to 2
move 4 from 2 to 9
move 10 from 3 to 4
move 1 from 3 to 5
move 1 from 5 to 1
move 5 from 2 to 5
move 30 from 4 to 2
move 1 from 8 to 9
move 1 from 8 to 1
move 27 from 2 to 3
move 2 from 4 to 2
move 1 from 9 to 4
move 2 from 1 to 3
move 8 from 3 to 7
move 19 from 3 to 1
move 1 from 4 to 7
move 5 from 9 to 1
move 4 from 2 to 9
move 4 from 3 to 4
move 1 from 3 to 5
move 1 from 2 to 7
move 1 from 9 to 3
move 1 from 9 to 1
move 5 from 5 to 4
move 5 from 7 to 3
move 1 from 5 to 6
move 23 from 1 to 6
move 1 from 9 to 2
move 1 from 2 to 5
move 24 from 6 to 9
move 6 from 4 to 7
move 4 from 4 to 8
move 1 from 4 to 9
move 4 from 7 to 4
move 4 from 3 to 4
move 4 from 9 to 8
move 6 from 7 to 9
move 4 from 7 to 6
move 1 from 1 to 4
move 2 from 6 to 4
move 1 from 6 to 2
move 1 from 1 to 8
move 1 from 7 to 3
move 1 from 6 to 9
move 13 from 4 to 2
move 3 from 3 to 2
move 15 from 9 to 8
move 1 from 5 to 9
move 5 from 9 to 1
move 4 from 1 to 7
move 4 from 7 to 3
move 8 from 2 to 7
move 9 from 8 to 2
move 1 from 1 to 2
move 7 from 9 to 2
move 4 from 3 to 1
move 4 from 1 to 4
move 2 from 9 to 1
move 20 from 2 to 8
move 3 from 4 to 8
move 1 from 2 to 3
move 4 from 2 to 7
move 1 from 3 to 4
move 1 from 9 to 3
move 1 from 4 to 7
move 1 from 2 to 5
move 1 from 4 to 3
move 2 from 1 to 6
move 1 from 5 to 6
move 1 from 7 to 1
move 12 from 7 to 2
move 12 from 2 to 6
move 9 from 6 to 2
move 1 from 6 to 8
move 1 from 3 to 9
move 8 from 2 to 4
move 1 from 9 to 6
move 1 from 4 to 6
move 4 from 4 to 9
move 1 from 4 to 9
move 1 from 1 to 5
move 2 from 6 to 3
move 1 from 5 to 4
move 1 from 2 to 8
move 10 from 8 to 6
move 10 from 8 to 3
move 1 from 3 to 4
move 8 from 8 to 1
move 3 from 9 to 8
move 2 from 9 to 1
move 11 from 6 to 7
move 1 from 1 to 7
move 8 from 1 to 4
move 3 from 6 to 7
move 1 from 1 to 4
move 14 from 8 to 6
move 1 from 8 to 7
move 1 from 6 to 8
move 6 from 4 to 1
move 1 from 8 to 5
move 4 from 1 to 8
move 2 from 7 to 1
move 1 from 6 to 7
move 5 from 4 to 2
move 2 from 4 to 3
move 4 from 2 to 8
move 15 from 7 to 3
move 3 from 3 to 6
move 1 from 5 to 2
move 21 from 3 to 6
move 2 from 8 to 7
move 1 from 7 to 8
move 32 from 6 to 9
move 1 from 7 to 8
move 5 from 8 to 4
move 2 from 8 to 7
move 14 from 9 to 8
move 14 from 8 to 1
move 2 from 6 to 1
move 2 from 7 to 4
move 1 from 9 to 3
move 17 from 9 to 5
move 6 from 1 to 8
move 4 from 4 to 6
move 2 from 2 to 5
move 2 from 8 to 2
move 1 from 6 to 7
move 2 from 2 to 6
move 4 from 3 to 2
move 7 from 6 to 3
move 6 from 5 to 7
move 1 from 8 to 9
move 1 from 6 to 7
move 4 from 8 to 6
move 1 from 9 to 3
move 4 from 1 to 4
move 12 from 5 to 9
move 7 from 7 to 8
move 3 from 4 to 2
move 8 from 9 to 4
move 2 from 6 to 2
move 1 from 7 to 4
move 2 from 6 to 9
move 1 from 5 to 3
move 1 from 8 to 1
move 2 from 8 to 7
move 2 from 2 to 9
move 7 from 2 to 3
move 8 from 4 to 1
move 2 from 8 to 4
move 4 from 9 to 7
move 2 from 9 to 5
move 16 from 1 to 3
move 3 from 7 to 4
move 1 from 7 to 6
move 1 from 6 to 2
move 2 from 5 to 3
move 10 from 4 to 2
move 2 from 8 to 7
move 19 from 3 to 8
move 17 from 3 to 9
move 3 from 1 to 7
move 17 from 9 to 2
move 1 from 7 to 5
move 1 from 7 to 5
move 2 from 5 to 7
move 2 from 9 to 2
move 6 from 7 to 6
move 3 from 6 to 7
move 1 from 8 to 9
move 1 from 9 to 3
move 4 from 2 to 5
move 17 from 2 to 3
move 3 from 7 to 5
move 1 from 5 to 3
move 7 from 2 to 3
move 2 from 2 to 4
move 1 from 7 to 1
move 1 from 1 to 5
move 2 from 5 to 3
move 1 from 4 to 5
move 1 from 4 to 3
move 14 from 3 to 5
move 17 from 8 to 7
move 2 from 6 to 2
move 12 from 3 to 5
move 15 from 5 to 9
move 7 from 7 to 3
move 7 from 7 to 6
move 1 from 2 to 3
move 11 from 9 to 6
move 13 from 5 to 7
move 10 from 6 to 8
move 6 from 8 to 3
move 2 from 5 to 8
move 1 from 2 to 9
move 10 from 7 to 6
move 9 from 6 to 8
move 1 from 5 to 1
move 10 from 6 to 4
move 8 from 4 to 5
move 1 from 1 to 2
move 3 from 9 to 1
move 10 from 3 to 7
move 1 from 4 to 7
move 12 from 7 to 9
move 7 from 3 to 5
move 13 from 8 to 7
move 3 from 9 to 5
move 5 from 5 to 6
move 3 from 1 to 9
move 5 from 9 to 6
move 10 from 6 to 4
move 15 from 7 to 5
move 3 from 9 to 4
move 1 from 4 to 3
move 3 from 8 to 9
move 6 from 9 to 6
move 2 from 5 to 1
move 1 from 2 to 7
move 12 from 5 to 8
move 3 from 9 to 5
move 11 from 5 to 6
move 1 from 1 to 2
move 1 from 2 to 8
move 3 from 7 to 8
move 10 from 8 to 3
move 1 from 1 to 7
move 10 from 4 to 9
move 1 from 7 to 8
move 5 from 5 to 3
move 15 from 6 to 5
move 8 from 3 to 9
move 3 from 4 to 5
move 1 from 7 to 8
move 8 from 8 to 9
move 1 from 6 to 5
move 5 from 3 to 2
move 5 from 2 to 3
move 5 from 9 to 8
move 1 from 6 to 8
move 2 from 5 to 1
move 4 from 3 to 2
move 16 from 5 to 6
move 3 from 5 to 9
move 4 from 8 to 5
move 8 from 6 to 4
move 4 from 2 to 3
move 1 from 1 to 4
move 6 from 3 to 6
move 24 from 9 to 2
move 1 from 1 to 9
move 1 from 9 to 4
move 2 from 4 to 5
move 1 from 3 to 2
move 10 from 6 to 8
move 22 from 2 to 6
move 1 from 2 to 7
move 1 from 7 to 5
move 10 from 8 to 9
move 7 from 9 to 3
move 6 from 4 to 8
move 3 from 9 to 2
move 5 from 8 to 3
move 1 from 4 to 1
move 1 from 8 to 3
move 3 from 6 to 2
move 5 from 5 to 1
move 1 from 5 to 3
move 5 from 6 to 3
move 1 from 2 to 7
move 16 from 3 to 2
move 1 from 8 to 1
move 1 from 4 to 7
move 1 from 5 to 3
move 6 from 6 to 4
move 14 from 2 to 8
move 3 from 3 to 5
move 2 from 3 to 6
move 3 from 5 to 6
move 4 from 6 to 4
move 3 from 4 to 8
move 7 from 2 to 9
move 2 from 2 to 1
move 9 from 8 to 4
move 7 from 1 to 7
move 8 from 7 to 5
move 2 from 8 to 4
move 3 from 9 to 6
move 4 from 4 to 6
move 1 from 7 to 3
move 4 from 8 to 2
move 2 from 9 to 8
move 9 from 6 to 7
move 1 from 9 to 8
move 1 from 1 to 5
move 3 from 4 to 5
move 1 from 3 to 2
move 5 from 8 to 2
move 9 from 2 to 7
move 1 from 6 to 7
move 1 from 6 to 2
move 9 from 7 to 4
move 2 from 5 to 9
move 10 from 4 to 6
move 1 from 8 to 6
move 5 from 4 to 3
move 5 from 4 to 9
move 5 from 9 to 5
move 1 from 1 to 7
move 4 from 7 to 8
move 8 from 5 to 3
move 3 from 3 to 8
move 6 from 7 to 6
move 3 from 3 to 1
move 5 from 3 to 7
move 1 from 9 to 6
move 2 from 7 to 6
move 1 from 9 to 3
move 4 from 6 to 9
move 2 from 2 to 6
move 1 from 7 to 3
move 6 from 5 to 4
move 7 from 6 to 9
move 6 from 6 to 8
move 2 from 1 to 2
move 1 from 5 to 1
move 5 from 8 to 5
move 1 from 3 to 9
move 4 from 4 to 5
move 10 from 9 to 2
move 14 from 6 to 4
move 1 from 3 to 8
move 1 from 8 to 5
move 2 from 7 to 9
move 1 from 1 to 2
move 14 from 4 to 7
move 1 from 1 to 4
move 3 from 4 to 1
move 3 from 5 to 1
move 6 from 5 to 1
move 10 from 7 to 3
move 6 from 1 to 5
move 6 from 1 to 7
move 3 from 8 to 3
move 1 from 5 to 1
move 3 from 9 to 6
move 1 from 9 to 3
move 6 from 5 to 9
move 2 from 6 to 1
move 9 from 2 to 1
move 6 from 9 to 6
move 2 from 8 to 7
move 5 from 7 to 3
move 7 from 7 to 5
move 4 from 2 to 8
move 6 from 8 to 3
move 1 from 9 to 4
move 1 from 7 to 3
move 2 from 5 to 3
move 7 from 6 to 4
move 28 from 3 to 4
move 1 from 3 to 8
move 1 from 5 to 9
move 9 from 4 to 5
move 12 from 4 to 5
move 2 from 4 to 6
move 5 from 4 to 6
move 1 from 3 to 8
move 10 from 5 to 8
move 10 from 5 to 4
move 5 from 5 to 9
move 3 from 4 to 1
move 5 from 6 to 9
move 2 from 6 to 7
move 2 from 7 to 5
move 10 from 9 to 4
move 1 from 8 to 5
move 5 from 1 to 5
move 8 from 8 to 7
move 8 from 5 to 3
move 8 from 7 to 8
move 2 from 8 to 2
move 7 from 3 to 2
move 21 from 4 to 7
move 10 from 1 to 9
move 3 from 4 to 5
move 1 from 4 to 8
move 1 from 8 to 3
move 7 from 8 to 5
move 2 from 3 to 1
move 7 from 7 to 2
move 1 from 1 to 4
move 1 from 1 to 6
move 8 from 9 to 3
move 2 from 8 to 4
move 3 from 3 to 1
move 3 from 4 to 7
move 1 from 6 to 7
move 5 from 2 to 4
move 2 from 1 to 6
""".mapLines { it.toMove() }

val stacks9000 = (1..9).map { i ->
    input1.mapNotNull {
        val ch = it.itemOnStack(i)
        if (ch in 'A'..'Z') ch else null
    }.reversed().toMutableList()
}.toMutableList()

val stacks9001 = stacks9000.map { it.toMutableList() }.toMutableList()

fun String.itemOnStack(n: Int) = getOrNull(n*4-3) ?: ' '

println("\n${ANSI_GREEN}Initial queues:$ANSI_RESET")
printStacks(stacks9000)

input2.onEach {
    it.applyTo(stacks9000, reverse = true)
    it.applyTo(stacks9001, reverse = false)
}

println("\n${ANSI_GREEN}Final stacks (9000):$ANSI_RESET")
printStacks(stacks9000)
println("\n${ANSI_GREEN}Final stacks (9001):$ANSI_RESET")
printStacks(stacks9001)

AocRunner(5, "Leaderboard: 6:42/7:58, Answers: ZRLJGSCTR/PRTTGRFPB",
    stacks9000.topOfStacks(),
    stacks9001.topOfStacks()
).run()

typealias StackList = MutableList<MutableList<Char>>
fun StackList.topOfStacks() = String(map { it.last() }.toCharArray())

fun printStacks(stacks: StackList) = StackVisualizer(stacks).apply {
    stackDelim = ' '
    itemBefore = '['
    itemAfter = ']'
    borderTop = false
}.visualize()

typealias Move = Triple<Int, Int, Int>

fun String.toMove(): Move = trim().split(" ").mapNotNull { it.toIntOrNull() }.let { Triple(it[0], it[1], it[2]) }

fun Move.count() = first
fun Move.from() = second
fun Move.to() = third

fun Move.applyTo(stacks: StackList, reverse: Boolean) {
    val toMove = stacks[from() - 1].takeLast(count())
    stacks[from() - 1] = stacks[from() - 1].dropLast(count()).toMutableList()
    stacks[to() - 1] += if (reverse) toMove.reversed() else toMove
}

