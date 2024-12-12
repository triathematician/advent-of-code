import aoc.util.AocParser.Companion.parselines
import aoc.*
import aoc.getDayInput
import aoc.util.*

val testInput = """
RRRRIICCFF
RRRRIICCCF
VVRRRCCFFF
VVRCCCJFFF
VVVVCJJCFE
VVIVCCJJEE
VVIIICJJEE
MIIIIIJJEE
MIIISIJEEE
MMMISSJEEE
""".parselines

// part 1

class Region(
    val ch: Char,
    val pos: Set<Coord>
) {
    fun area() = pos.size
    fun neighborWalls() = pos.sumOf { it.adj().count { it in pos } }
    fun perimeter() = 4 * pos.size - neighborWalls()
    fun sideWalls(): Int {
        val verticals = pos.flatMap { c ->
            listOfNotNull(
                if (c.left in pos) null else c,
                if (c.right in pos) null else c.right
            )
        }.toSet()
        val verticalWallCount = verticals.groupBy { it.x }
            .mapValues { (_, v) ->
                val set = v.sortedBy { it.y }
                set.count {
                    if (it.top !in set)
                        true
                    else if (it in pos != it.top in pos)
                        true
                    else
                        false
                }
            }
            .values.sum()
        val horizontals = pos.flatMap { c ->
            listOfNotNull(
                if (c.top in pos) null else c,
                if (c.bottom in pos) null else c.bottom
            )
        }.toSet()
        val horizontalWallCount = horizontals.groupBy { it.y }
            .mapValues { (_, v) ->
                val set = v.sortedBy { it.x }
                set.count {
                    if (it.left !in set)
                        true
                    else if (it in pos != it.left in pos)
                        true
                    else
                        false
                }
            }
            .values.sum()
        return horizontalWallCount + verticalWallCount
    }
}

fun CharGrid.regions(): List<Region> {
    val regions = mutableListOf<Region>()
    allIndices2().forEach { c ->
        val adj = c.adj()
        val ch = at(c)
        val existingRegions = regions.filter { it.ch == ch }
            .filter { it.pos.any { it in adj } }
        if (existingRegions.isNotEmpty()) {
            val commonRegion = Region(ch, existingRegions.flatMap { it.pos }.toSet() + c)
            regions.removeAll(existingRegions)
            regions.add(commonRegion)
        } else {
            regions.add(Region(ch, setOf(c)))
        }
    }
    return regions
}

fun List<String>.part1(): Int =
    regions().sumOf { it.area() * it.perimeter() }

// part 2

fun List<String>.part2(): Int =
    regions().sumOf { it.area() * it.sideWalls() }

// calculate answers

val day = 12
val input = getDayInput(day, 2024)
val testResult = testInput.part1()
val testResult2 = testInput.part2()
val answer1 = input.part1().also { it.print }
val answer2 = input.part2().also { it.print }

// print results

AocRunner(day,
    test = { "$testResult, $testResult2" },
    part1 = { answer1 },
    part2 = { answer2 }
).run()
