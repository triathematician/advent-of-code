import aoc.AocParser.Companion.parselines
import aoc.AocRunner
import aoc.print
import aoc.util.Coord
import aoc.util.x
import aoc.util.y
import kotlin.math.absoluteValue

val day = 15

val testInput = """
Sensor at x=2, y=18: closest beacon is at x=-2, y=15
Sensor at x=9, y=16: closest beacon is at x=10, y=16
Sensor at x=13, y=2: closest beacon is at x=15, y=3
Sensor at x=12, y=14: closest beacon is at x=10, y=16
Sensor at x=10, y=20: closest beacon is at x=10, y=16
Sensor at x=14, y=17: closest beacon is at x=10, y=16
Sensor at x=8, y=7: closest beacon is at x=2, y=10
Sensor at x=2, y=0: closest beacon is at x=2, y=10
Sensor at x=0, y=11: closest beacon is at x=2, y=10
Sensor at x=20, y=14: closest beacon is at x=25, y=17
Sensor at x=17, y=20: closest beacon is at x=21, y=22
Sensor at x=16, y=7: closest beacon is at x=15, y=3
Sensor at x=14, y=3: closest beacon is at x=15, y=3
Sensor at x=20, y=1: closest beacon is at x=15, y=3
""".parselines.map { it.parseBeacon() }

val input = """
Sensor at x=2885528, y=2847539: closest beacon is at x=2966570, y=2470834
Sensor at x=2224704, y=1992385: closest beacon is at x=2018927, y=2000000
Sensor at x=3829144, y=1633329: closest beacon is at x=2966570, y=2470834
Sensor at x=43913, y=426799: closest beacon is at x=152363, y=369618
Sensor at x=2257417, y=2118161: closest beacon is at x=2386559, y=2090397
Sensor at x=8318, y=3994839: closest beacon is at x=-266803, y=2440278
Sensor at x=69961, y=586273: closest beacon is at x=152363, y=369618
Sensor at x=3931562, y=3361721: closest beacon is at x=3580400, y=3200980
Sensor at x=476279, y=3079924: closest beacon is at x=-266803, y=2440278
Sensor at x=2719185, y=2361091: closest beacon is at x=2966570, y=2470834
Sensor at x=2533382, y=3320911: closest beacon is at x=2260632, y=3415930
Sensor at x=3112735, y=3334946: closest beacon is at x=3580400, y=3200980
Sensor at x=1842258, y=3998928: closest beacon is at x=2260632, y=3415930
Sensor at x=3712771, y=3760832: closest beacon is at x=3580400, y=3200980
Sensor at x=1500246, y=2684955: closest beacon is at x=2018927, y=2000000
Sensor at x=3589321, y=142859: closest beacon is at x=4547643, y=-589891
Sensor at x=1754684, y=2330721: closest beacon is at x=2018927, y=2000000
Sensor at x=2476631, y=3679883: closest beacon is at x=2260632, y=3415930
Sensor at x=27333, y=274008: closest beacon is at x=152363, y=369618
Sensor at x=158732, y=2405833: closest beacon is at x=-266803, y=2440278
Sensor at x=2955669, y=3976939: closest beacon is at x=3035522, y=4959118
Sensor at x=1744196, y=13645: closest beacon is at x=152363, y=369618
Sensor at x=981165, y=1363480: closest beacon is at x=2018927, y=2000000
Sensor at x=2612279, y=2151377: closest beacon is at x=2386559, y=2090397
Sensor at x=3897, y=2076376: closest beacon is at x=-266803, y=2440278
Sensor at x=2108479, y=1928318: closest beacon is at x=2018927, y=2000000
Sensor at x=1913043, y=3017841: closest beacon is at x=2260632, y=3415930
Sensor at x=2446778, y=785075: closest beacon is at x=2386559, y=2090397
Sensor at x=2385258, y=2774943: closest beacon is at x=2386559, y=2090397
Sensor at x=3337656, y=2916144: closest beacon is at x=3580400, y=3200980
Sensor at x=380595, y=66906: closest beacon is at x=152363, y=369618
Sensor at x=1593628, y=3408455: closest beacon is at x=2260632, y=3415930
""".parselines.map { it.parseBeacon() }

fun String.parseBeacon(): SensorBeacon {
    val spl = split(' ', '=', ',', ':').mapNotNull { it.toIntOrNull() }
    return SensorBeacon(spl[0] to spl[1], spl[2] to spl[3])
}
fun Coord.distTo(other: Coord) = (x - other.x).absoluteValue + (y - other.y).absoluteValue

data class SensorBeacon(val sensor: Coord, val beacon: Coord) {
    val dist = sensor.distTo(beacon)
}

fun List<SensorBeacon>.countNoBeaconsInRow(n: Int, print: Boolean): Int {
    val xx = flatMap { listOf(it.beacon.x, it.sensor.x) }
    val range = xx.min()..xx.max()
    val maxdist = maxOf { it.dist }
    val res = (range.min() - maxdist..range.max() + maxdist).count { col ->
        // if its closer to a beacon than the closest detected, we know it counts
        val res = nobeacon(col to n)
        if (print) { if (res) print('#') else print('.') }
        res
    }
    println()
    return res
}

fun List<SensorBeacon>.nobeacon(xy: Coord) =
    any { xy != it.beacon && xy.distTo(it.sensor) <= it.dist }
fun List<SensorBeacon>.canputbeacon(xy: Coord) =
    all { xy != it.beacon && xy != it.sensor && xy.distTo(it.sensor) > it.dist }

fun List<SensorBeacon>.search(quad: SearchQuadrant, print: Boolean, pfx: String = ""): Coord? {
    if (print) print("$pfx Searching $quad")
    if (quad.size <= 2) {
        return quad.coords().firstOrNull { canputbeacon(it) }
    } else if (any { quad.inside(it) }) {
        if (print) println("  INSIDE")
        return null
    } else {
        quad.fourths().forEach {
            search(it, print, "$pfx ")?.let {
                println(" -- Found $it!")
                return it
            }
        }
    }
    return null
}

operator fun SensorBeacon.contains(p: Coord) = sensor.distTo(p) <= dist

class SearchQuadrant(val xr: IntRange, val yr: IntRange) {
    override fun toString() = "$xr x $yr"
    val size = (xr.last - xr.first + 1).toLong() * (yr.last - yr.first + 1).toLong()
    fun coords() = xr.flatMap { x -> yr.map { y -> x to y }}
    fun inside(s: SensorBeacon): Boolean {
        return (xr.start to yr.start) in s
                && (xr.start to yr.last) in s
                && (xr.last to yr.start) in s
                && (xr.last to yr.last) in s
    }
    fun fourths(): List<SearchQuadrant> {
        val xmid = (xr.first + xr.last)/2
        val ymid = (yr.first + yr.last)/2
        return listOf(
            SearchQuadrant(xr.first..xmid, yr.first..ymid),
            SearchQuadrant(xmid+1..xr.last, yr.first..ymid),
            SearchQuadrant(xr.first..xmid, ymid+1..yr.last),
            SearchQuadrant(xmid+1..xr.last, ymid+1..yr.last)
        )
    }
}

// test case

testInput.countNoBeaconsInRow(9, true)
val testResult = testInput.countNoBeaconsInRow(10, true)
testInput.countNoBeaconsInRow(11, true)
val tsq = SearchQuadrant(0..20, 0..20)
val testResult2 = testInput.search(tsq, true)?.let { "$it: ${it.freq()}"}

fun Coord.freq() = x * 4000000L + y

// part 1

val answer1 = input.countNoBeaconsInRow(2000000, false)
answer1.print

// part 2

val sq = SearchQuadrant(0..4000000, 0..4000000)
val answer2 = input.search(sq, true)?.let { "$it: ${it.freq()}"}.toString()
answer2.print

// print results

AocRunner(day,
    info = { listOf("Leaderboard: 10:40/27:14", "Answers: 5716881/10852583132904") },
    test = { "$testResult, $testResult2" },
    part1 = { answer1 },
    part2 = { answer2 }
).run()
