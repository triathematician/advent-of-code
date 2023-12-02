import aoc.AocParser.Companion.parselines
import aoc.*

val day = 2

val testInput = """
Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green
Game 2: 1 blue, 2 green; 3 green, 4 blue, 1 red; 1 green, 1 blue
Game 3: 8 green, 6 blue, 20 red; 5 blue, 4 red, 13 green; 5 green, 1 red
Game 4: 1 green, 3 red, 6 blue; 3 green, 6 red; 3 green, 15 blue, 14 red
Game 5: 6 red, 1 blue, 3 green; 2 blue, 1 red, 2 green
""".parselines

class GameResult(var red: Int, var green: Int, var blue: Int)

val input = aocInput(day).parselines

fun List<String>.parseInput() = associate {
    val (game, colors) = it.split(": ")
    val gameNum = game.chunkint(1)
    val colorBins = colors.splitOn(";") {
        val res = GameResult(0, 0, 0)
        it.split(",").map {
            val (n, col) = it.trim().split(" ")
            when (col) {
                "red" -> res.red += n.toInt()
                "green" -> res.green += n.toInt()
                "blue" -> res.blue += n.toInt()
            }
        }
        res
    }
    gameNum to colorBins
}

// test case

val testResult = testInput.part1()
val testResult2 = testInput.part2()

// part 1

fun List<String>.part1() = parseInput().filter {
    it.value.all {
        it.red <= 12 && it.green <= 13 && it.blue <= 14
    }
}.keys.sum()

val answer1 = input.part1().also { it.print }

// part 2

fun List<String>.part2() = parseInput().map {
    (it.value.maxOf { it.red }) *
    (it.value.maxOf { it.green }) *
    (it.value.maxOf { it.blue })
}.sum()

val answer2 = input.part2().also { it.print }

// print results

AocRunner(day,
    test = { "$testResult, $testResult2" },
    part1 = { answer1 },
    part2 = { answer2 }
).run()
