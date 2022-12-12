import aoc.*
import aoc.AocParser.Companion.parse

val day = 12

val testInput = """
Sabqponm
abcryxxl
accszExk
acctuvwj
abdefghi
""".parse.charGrid

val input = """
abccccccaaccaaccccaaaaacccccaaaaccccccccccccccccccccccccccccccccaaaaaaaaaaaaaaaaaaaccccccccccccccccaaaccccccccccccaacccccccccccccccccccccccccccccccccccccccccaaaa
abaaaaccaaaaaccccaaaaaccccccaaaaccccccccccccccccccccaaacccccccccccaaaaaaaaaaaaaaaaaaccccccccccccccccaaaaccccccaaacaaccccccccccccccccccccccccccccccccccccccccaaaaa
abaaacccaaaaaaaacaaaaaacccccaaaaccccccccccccccccccccaaaaacccccccccaaaaaaaaaaaaaaaaacccccccccaaccccaaaaaacccccccaaaaaccccaaccccccccccccccacccccccccccccccccccaaaaa
abaaacccccaaaaaccccaaaaccccccaaacccccccccccccccccccaaaaaccccccccccaaaaaacacaaaaaacccccccccccaaccccaaaaacccccccccaaaaaaccaaaaaaccccccccccaaaccccacccccccccccaaaaaa
abaacccccaaaaaccccaacccccccccccccccaaaaacccccccccccaaaaacccccccccaaaaaaaaccaaaaaaacccccccaaaaaaaaccaaaaacccccccaaaaaaaccaaaaacccccccccccaaacccaaaccccccccccccccaa
abaaacccaaacaacccccccccccccccccccccaaaaaccccccccccccaaaaacccccccaaaaaaaaaccaaccaaacccccccaaaaaaaaccaacccccccccaaaaaaccaaaaaaccccccccccccaaaacaaaaccccccccccccccaa
abaaacccccccaaccccccccccccccccccccaaaaaaccccccccccccaaccccccaacccaaaccaaaaccccccaacccccccccaaaacccccccccccccccaacaaaccaaaaaaaccccccccccccajjjjjjjcccccccccccccccc
abcaacccccccccccccccccccccccccccccaaaaaaccccccccccccccccccccaaaaccccccaaaaccccccccccccccaacaaaaaccccccccccccccccccaaccccaaaaaacccccccccccjjjjjjjjjcccccaaaccccccc
abccccccccccccccccccccccccccccccccaaaaaaccaaccccccccccccccaaaaaacccccccaaacccccccccccaacaaaaaaaaccccccccccccccccccccccccaaccaaccccccccaiijjjjojjjjcccccaaacaccccc
abcccccccccccccccccccccccaaacccccccaaacacaaacccccccccccccccaaaaccccaaccccccccccccccccaaaaaaacccaccccccccccccccccccccccccaacccccccccccaiiijjooooojjkccaaaaaaaacccc
abccccccccccccccccccccccaaaaccccccccccaaaaaccccccccccccccccaaaaacccaaaaaccccccccccccccaaaaaacccccccccccccccccccccccccccccccccccccciiiiiiiioooooookkkcaaaaaaaacccc
abccccccccccccccccccccccaaaaccccccccccaaaaaaaacccccccccccccaacaaccaaaaacccccccaaacccaaaaaaaaccccccccccccccccccccccccccccccccccchiiiiiiiiooooouoookkkccaaaaaaccccc
abcccccccccaaccccccccccccaaaccccccccccccaaaaacccccccccccccccccccccaaaaaccccccaaaacccaaaaacaacccccccccccccaacaacccccccccccccccchhhiiiinnnooouuuuoookkkccaaaaaccccc
abcccccccccaaacccccccccccccccccccccccccaaaaacccccccccccccccccccccccaaaaacccccaaaaccccccaaccccccccccccccccaaaaacccccccccccccccchhhnnnnnnnnouuuuuuppkkkkaaaaaaccccc
abccccccaaaaaaaacccaaccccccccccccccccccaacaaccaacaaccccccccccccccccaacccccccccaaaccccccaacccccccccccccccaaaaacccccccccccccccchhhnnnnnnnnntuuxuuupppkkkkkacccccccc
abccccccaaaaaaaacacaaaacccccccccccccccccccaaccaaaaacccccccccccccccccccccccccccccccccccccccccccccccccccccaaaaaacccccccaacccccchhhnnnnttttttuxxxuuppppkkkkkcccccccc
abcccccccaaaaaaccaaaaaaccccccccccaaccccccccccaaaaaccccccccccccccccccccccccaaacccccccccccccccccccccccccccccaaaaccaaccaaacccaaahhhnnntttttttuxxxxuupppppllllccccccc
abcccccccaaaaaacccaaaacccccccccaaaaaaccccccccaaaaaacccccccccccccccccccccccaaacccccccccccccccccccccccccccccacccccaaaaaaacaaaaahhhppntttxxxxxxxxuuuuvpppplllccccccc
abcccccccaaaaaacccaaaacccccccccaaaaaacccccaaaaaaaaaccccccccccccccccccccaaaaaaaacccccccccccccccccccccaaaccccccaacaaaaaaccaaaaahhhpppttxxxxxxxxyuuvvvvvppplllcccccc
abcccccccaaccaacccaacaccaaaaccccaaaaacccccaaaaaaaaaccccccccccccccccccccaaaaaaaacccccccccccccccccccccaaacaaaaaaaccaaaaaaaaaaaaahhppptttxxxxxxyyyyyyvvvppplllcccccc
SbccccccccccccccccccccccaaaacccaaaaacccccccaaaaaaaaacaaaccccccccaacccccccaaaaaccccccccaaaaacccccccccaaaaaaaaaaaaaaaaaaaaaaaaacgggpppttxxxxEzzyyyyyvvvqqqlllcccccc
abccccccccccccccccccccccaaaacccaaaaacccccccaaaaaaaaccaaaccccccccaaacaaccaaaaaaccccccccaaaaacccccccaaaaaaaaaaaaaaaaaaaaaaaaaaacgggpppsssxxxyyyyyyvvvvvqqlllccccccc
abcccaaaccccccccccccccccaaaccccccccccccccccaaaaaaaaaaaaaccccccccaaaaaaccaaaaaacccccccaaaaaacccaaaccaaaaaccaaaaaaaaaaaacccccccccgggppssswwyyyyyyvvvvqqqqlllccccccc
abcaaaaaccccccccccccccccccccccccccccccccccaaaaaaaaaaaaacccccccaaaaaaacccaccaaacccccccaaaaaacccaaacccaaaaaaaaaaaccccaaacccaaaaacgggppsswwwyyyyyyvvqqqqqlllcccccccc
abcaaaaaaccccccccccccccccccccccccccccccccccaaccaaaaaaaaaaaccccaaaaaaacccccccccccccccccaaaaacccaaacaaaacaaaaaaaaccccaaacccaaaaacggpppsswwwywwyyyvvqqqmmmlccccccccc
abcaaaaaacccccccaacaaccccccccccccccccccccccccccaaaaaaaaaaaccccccaaaaacccccccccccccccccaaaccaaaaaaaaaaacccccccaacccccccccaaaaaacggpppsswwwwwwwwyvvqqqmmmcccccccccc
abcaaaaaccccccccaaaaaccccccccccccccccccccccccccccaaaaaaaacccccccaacaaacccccccccccccccccccccaaaaaaaaaccccccccccccccccccccaaaaaagggoossswwwwrrwwwvvqqmmmccccccccccc
abcaaaaacccccccaaaaaccccccccccccccccccccccccccccaaaaaaacccccccccaaccccccccccccccccccccccccccaaaaaaacccccccccccaaaccccccccaaaaagggooosssssrrrrwwwvqqmmmcccaacccccc
abcccccccccccccaaaaaaccccccccccccccccccccaacccccccccaaaccccccccccccccccccccccccccccccccccccccaaaaaaccccccccccccaaaaccccccaaaccgggooosssssrrrrrwwrrqmmmcccaacccccc
abcccccccccccccccaaaacccccccccccccccccccaaaacccccccacaaacccccccccccccccccccccccccccccccccccccaaaaaaacccccccccaaaaaacccccccccccgffoooooosoonrrrrrrrrmmmccaaaaacccc
abcccccccccccccccaccccccccccccccccccccccaaaacccccccaaaaacccccccccccccccccccccccccccccccccccccaaacaaacccccccccaaaaacccccccccccccfffoooooooonnnrrrrrmmmddcaaaaacccc
abccccccccccccccccccccccccccccccccccccccaaaaccccccccaaaaacccccccccccccccccccccccccaaaccccccccaacccccccccccccccaaaaaccccccccccccffffoooooonnnnnnrnnmmmdddaaaaacccc
abcccccccccccccccccccccccccccccccccccccccccccccccccaaaaaacccccccccccccccccaaaaaccaaaacccccccccccccccccccccccccaacccccccccccccccfffffffffeeeennnnnnmmdddaaaacccccc
abcccccccaaaccccccccaccccccccccccccccccccccccccccccaaaaccccccccccccaaaccccaaaaaccaaaaccccccccccccccccccccccccccccccccccccccccccccfffffffeeeeennnnnmddddaaaaaccccc
abcccaaccaaacccccaaaacccccaacccccccccccccccccccccccccaaacccccccccccaaacccaaaaaacccaaaccccccccccccccccccccccccccccccccccccccccccccccffffeeeeeeeedddddddcccaacccccc
abcccaaaaaaacccccaaaaaaccaaacccccccccccccccccccccccccccacccccccccccaaaaccaaaaaaccccccccccccccccccccccccccaacccccccccaaaccccccccccccccaaaaaaeeeeedddddcccccccccccc
abcccaaaaaacccccccaaaacccaaacaaaccccaaaacccccccccaaacaaaccccccaacccaaaacaaaaaaacccccccccccccccccccccccccaaaccccccccaaaacccccccccccccccccccaaaaeeddddccccccccccccc
abccccaaaaaaaacccaaaaaaaaaaaaaaaccccaaaacccccccccaaaaaaacccccaaacccaaaaaaaaaacccccccccccccccccccccccaaacaaaccccccccaaaaccccccccccccccccccccaaaccccccccccccccaaaca
abcccaaaaaaaaacccaacaaaaaaaaaaacccccaaaaccccccccccaaaaaacaaacaaacaaaaaaaaaacccccccccccccccaaacccccccaaaaaaaaaaccccccaaaccccccccccccccccccccaacccccccccccccccaaaaa
abcccaaaaaaaacccccccccccaaaaaacccccccaacccccccccccaaaaaaaaaaaaaaaaaaaaaaaaccccccccccccccccaaaacccccccaaaaaaaaacccccccccccccccccccccccccccccaaacccccccccccccccaaaa
abccaaaaaaacccccccccccccaaaaaaacccccccccccccccccaaaaaaaaaaaaaaaaaaaaaaaaaaacccccccccccccccaaaacccccccaaaaaaaacccccccccccccccccccccccccccccccccccccccccccccccaaaaa
""".parse.charGrid

// test case

val tstart = testInput.find('S')
val tgoal = testInput.find('E')
println("$tstart to $tgoal")

var testResult: String = ""
// find the way up
Pathfinder.shortestPath(tstart, tgoal) {
    it.adjUp(testInput)
}.let {
    testResult += "Shortest path: $it\n"
    testResult += "Labels: ${String(it.map { testInput[it] }.toCharArray())}\n"
    testResult += "Steps: ${it.size - 1}\n"
}
// when we have lots of options for start, reverse it and find shortest path down, change up the adjacency
Pathfinder.shortestPathTo(tgoal,
    goal = { testInput[it] in arrayOf('a', 'S') },
    adj = { it.adjDown(testInput) }
).let {
    testResult += "Shortest path down: $it\n"
    testResult += "Labels: ${String(it.map { testInput[it] }.toCharArray())}\n"
    testResult += "Steps: ${it.size - 1}"
}

typealias Maze = List<List<Char>>

fun Coord.adjUp(input: Maze) = adj(input).filter { testUp(input, this, it) }
fun Coord.adjDown(input: Maze) = adj(input).filter { testDown(input, this, it) }

fun testUp(input: Maze, from: Coord, to: Coord): Boolean {
    val ch = input[from.first][from.second].let { if (it == 'S') 'a' else if (it == 'E') 'z' else it }
    val testCh = input[to.first][to.second].let { if (it == 'S') 'a' else if (it == 'E') 'z' else it }
    return testCh - ch <= 1
}
fun testDown(input: Maze, from: Coord, to: Coord) = testUp(input, to, from)

// part 1

val start = input.find('S')
val goal = input.find('E')
val path = Pathfinder.shortestPath(start, goal) { it.adjUp(input) }
println("Path from $start to $goal: ${path.size - 1} steps, $path")
val answer1 = path.size - 1

// part 2

// when we have lots of options for start, reverse it and find shortest path down, change up the adjacency
val path2 = Pathfinder.shortestPathTo(goal,
    goal = { input[it] in arrayOf('a', 'S') },
    adj = { it.adjDown(input) }
)
println("Path from $goal to 'a': ${path2.size - 1} steps, $path2")
val answer2 = path2.size - 1

// print results

AocRunner(day,
    info = { listOf("Leaderboard: 7:39/9:45", "Answers: 440/439") },
    test = { testResult },
    part1 = { answer1 },
    part2 = { answer2 }
).run()

//region INITIAL CODING
//
//fun calcSteps(testInput: Maze, start: Coord): List<List<Int>> {
//    val coordsToDo = testInput.allIndices().toMutableSet()
//    val coordsFound = mutableSetOf<Coord>()
//    val tsteps = testInput.indices.map { testInput[0].indices.map { -1 }.toMutableList() }
//    tsteps[start.first][start.second] = 0
//    coordsToDo -= start
//    coordsFound += start
//    var coordsAdding = setOf(start)
//    var n = 1
//    while (coordsToDo.isNotEmpty() && coordsAdding.isNotEmpty()) {
//        coordsAdding = coordsAdding.flatMap { it.adjUp(testInput) }.toSet() - coordsFound
//        coordsToDo.removeAll(coordsAdding)
//        coordsFound.addAll(coordsAdding)
//        coordsAdding.forEach { tsteps[it.first][it.second] = n }
//        n++
//    }
//    return tsteps
//}
//fun calcStepsDown(testInput: Maze, start: Coord): Int {
//    val coordsToDo = testInput.allIndices().toMutableSet()
//    val coordsFound = mutableSetOf<Coord>()
//    coordsToDo -= start
//    coordsFound += start
//    var coordsAdding = setOf(start)
//    var n = 1
//    while (coordsToDo.isNotEmpty() && coordsAdding.isNotEmpty()) {
//        coordsAdding = coordsAdding.flatMap { it.adjDown(testInput) }.toSet() - coordsFound
//        if (coordsAdding.any { testInput[it.first][it.second] == 'a' })
//            return n
//        coordsToDo.removeAll(coordsAdding)
//        coordsFound.addAll(coordsAdding)
//        n++
//    }
//    return -1
//}
//
//endregion
