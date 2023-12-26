package aoc

import aoc.AocParser.Companion.parselines
import aoc.util.getDayInput
import aoc.util.pairwise
import aoc.util.second
import java.math.BigInteger
import kotlin.math.abs
import kotlin.math.absoluteValue

val testInput = """
19, 13, 30 @ -2,  1, -2
18, 19, 22 @ -1, -1, -2
20, 25, 34 @ -2, -2, -4
12, 31, 28 @ -1, -2, -1
20, 19, 15 @  1, -5, -3
""".parselines

fun String.parse(i: Int) = split(",", "@").map { it.trim() }.let {
    Hailstone(i, it[0].toDouble(), it[1].toDouble(), it[2].toDouble(), it[3].toDouble(), it[4].toDouble(), it[5].toDouble())
}

class Hailstone(val i: Int, val x: Double, val y: Double, val z: Double, val vx: Double, val vy: Double, val vz: Double) {
    override fun toString() = "Hailstone $i: (${x.toLong()}, ${y.toLong()}, ${z.toLong()}) @ (${vx.toLong()}, ${vy.toLong()}, ${vz.toLong()})"
    fun xy() = Loc(x, y)
    fun xyz() = Loc3(x.toLong(), y.toLong(), z.toLong())
    fun vel() = Loc(vx, vy)
    fun locAt(t: Double) = Loc(x + vx * t, y + vy * t)
    fun locAt3(t: Long) = Loc3((x + vx * t).toLong(), (y + vy * t).toLong(), (z + vz * t).toLong())
}

enum class IntersectionType {
    PAST_FOR_A,
    PAST_FOR_B,
    PAST_FOR_BOTH,
    PARALLEL,
    OUTSIDE,
    INSIDE
}

val PRINT_DETAILS = false

/** Test whether x,y of two hailstones will intersect in the future, based on current positions and speed. */
fun collideInRegion(a: Hailstone, b: Hailstone, testArea: ClosedFloatingPointRange<Double>): IntersectionType {
//    val timeToBoundaryA = timeToBoundary(a, testArea)
//    val timeToBoundaryB = timeToBoundary(b, testArea)
//    val endpointA = a.locAt(timeToBoundaryA)
//    val endpointB = b.locAt(timeToBoundaryB)
    val xx = intersect(a, b)
    val loc = xx.loc
    val locStr = if (loc == null) "none"
    else if (abs(loc.x) < 1E10) "x=%.3f, y=%.3f".format(loc.x, loc.y)
    else "x=%.3f, y=%.3f".format(loc.x/1E14, loc.y/1E14)
    val intersectInRegion = xx.intersect && xx.ta >= 0 && xx.tb >= 0 && loc!!.x in testArea && loc.y in testArea

    val print = false // PRINT_DETAILS || intersectInRegion
    if (print) {
        println("Hailstone A: ${a.x.toLong()}, ${a.y.toLong()}, ${a.z.toLong()} @ ${a.vx.toLong()}, ${a.vy.toLong()}, ${a.vz.toLong()}")
        println("  Hailstone B: ${b.x.toLong()}, ${b.y.toLong()}, ${b.z.toLong()} @ ${b.vx.toLong()}, ${b.vy.toLong()}, ${b.vz.toLong()}")
    }
    val res = when {
        !xx.intersect -> IntersectionType.PARALLEL
        xx.ta < 0.0 && xx.tb < 0.0 -> IntersectionType.PAST_FOR_BOTH
        xx.ta < 0.0 -> IntersectionType.PAST_FOR_A
        xx.tb < 0.0 -> IntersectionType.PAST_FOR_B
        loc!!.x !in testArea || loc.y !in testArea -> IntersectionType.OUTSIDE
        intersectInRegion -> IntersectionType.INSIDE
        else -> throw IllegalStateException("Unexpected case: $xx")
    }
    if (print) {
        when (res) {
            IntersectionType.INSIDE -> println("  Hailstones' paths will cross inside the test area (at $locStr).")
            IntersectionType.OUTSIDE -> println("  Hailstones' paths will cross outside the test area (at $locStr).")
            IntersectionType.PAST_FOR_A -> println("  Hailstones' paths crossed in the past for hailstone A.")
            IntersectionType.PAST_FOR_B -> println("  Hailstones' paths crossed in the past for hailstone B.")
            IntersectionType.PAST_FOR_BOTH -> println("  Hailstones' paths crossed in the past for both hailstones.")
            IntersectionType.PARALLEL -> println("  Hailstones' paths are parallel; they never intersect.")
        }
        println()
    }
    return res
}

/** Get exact time to hit boundary. */
fun timeToBoundary(a: Hailstone, testArea: ClosedFloatingPointRange<Double>): Double {
    val txa = timeToBoundary(a.x, a.vx, testArea.start, testArea.endInclusive)
    val tya = timeToBoundary(a.y, a.vy, testArea.start, testArea.endInclusive)
    return minOf(txa, tya)
}

/** Get exact time to hit boundary. */
fun timeToBoundary(x: Double, vx: Double, min: Double, max: Double): Double = when {
    vx == 0.0 -> Double.POSITIVE_INFINITY
    vx > 0.0 -> (max - x) / vx
    vx < 0.0 -> (min - x) / vx
    else -> throw IllegalStateException()
}

class Intersection(val ta: Double, val tb: Double, val loc: Loc? = null) {
    val intersect = loc != null
}

/** Return percentage along segments for the intersection, or null if there is none. */
fun intersect(a: Hailstone, b: Hailstone): Intersection {
    val da = a.vel()
    val db = b.vel()
    val det = da.x * db.y - da.y * db.x
    if (abs(det) < 1.0)
        println("Near zero determinant: $det for hailstones $a and $b")
    if (parallel(a.vel(), b.vel())) {
        val colinear = parallel(da, b.xy() - a.xy())
        return if (colinear) {
            println("Colinear paths for hailstones $a and $b. Returning no intersection, but revisit this if there are cases here.")
            Intersection(0.0, 0.0, null)
        } else {
            // parallel but not colinear, definitely don't intersect
            Intersection(0.0, 0.0, null)
        }
    }
    val c = b.xy() - a.xy()
    val ta = (c.x * db.y - c.y * db.x) / det
    val tb = (c.x * da.y - c.y * da.x) / det
    val ax = Loc(a.x + da.x * ta, a.y + da.y * ta)
    val bx = Loc(b.x + db.x * tb, b.y + db.y * tb)
    val diffA = 2 * abs(ax.x - bx.x) / abs(ax.x + bx.x)
    val diffB = 2 * abs(ax.y - bx.y) / abs(ax.y + bx.y)
    if (diffA > .01 || diffB > .01)
        println("Big difference between hailstones $a and $b intersection points calculated: $diffA, $diffB")
    return Intersection(ta, tb, ax)
}

/** Check if x,y velocities of a and b are parallel. */
fun parallel(a: Loc, b: Loc) : Boolean {
    val err = 1E-6
    return abs(a.x / a.y - b.x / b.y) < err || abs(a.y / a.x - b.y / b.x) < err
}

class Loc3(val x: Long, val y: Long, val z: Long) {
    fun bigLong() = Loc3Big(x.toBigInteger(), y.toBigInteger(), z.toBigInteger())
    override fun toString() = "($x, $y, $z)"
    operator fun plus(o: Loc3) = Loc3(x + o.x, y + o.y, z + o.z)
    operator fun minus(o: Loc3) = Loc3(x - o.x, y - o.y, z - o.z)
}
class Loc3Big(val x: BigInteger, val y: BigInteger, val z: BigInteger) {
    override fun toString() = "($x, $y, $z)"
    operator fun plus(o: Loc3Big) = Loc3Big(x + o.x, y + o.y, z + o.z)
    operator fun minus(o: Loc3Big) = Loc3Big(x - o.x, y - o.y, z - o.z)
    fun cross(o: Loc3Big) = Loc3Big(y * o.z - z * o.y, z * o.x - x * o.z, x * o.y - y * o.x)

    /** Divide by GCD of x, y, z. */
    fun reduce(): Loc3Big {
        val gcd = x.gcd(y).gcd(z)
        return Loc3Big(x / gcd, y / gcd, z / gcd)
    }
}

class Loc(val x: Double, val y: Double) {
    override fun toString() = "($x, $y)"
    operator fun plus(o: Loc) = Loc(x + o.x, y + o.y)
    operator fun minus(o: Loc) = Loc(x - o.x, y - o.y)
}

// part 1

val TEST_AREA_SAMPLE = 7.0..27.0
val TEST_AREA_ACTUAL = 200000000000000.0..400000000000000.0

fun List<String>.part1(): Int {
    val hail: List<Hailstone> = this.mapIndexed { index, s -> s.parse(index) }
    val testArea = if (hail.size < 10) TEST_AREA_SAMPLE else TEST_AREA_ACTUAL
    val types = hail.pairwise().map { collideInRegion(it.first(), it.second(), testArea) }
        .groupBy { it }.mapValues { it.value.size }
    println(types)
    return types[IntersectionType.INSIDE]!!
}

// part 2

val parallelHailstones = """
18, 19, 22 @ -1, -1, -2
20, 25, 34 @ -2, -2, -4
22, 13, 18 @ -2, 1, -2
19, 13, 30 @ -2, 1, -2
""".trimIndent().mapLines { it.parse(0) }

val parallelHailstones2 = """
281974048517427, 162586692606990, 317438841834668 @ 14, -19, 7
277661292234039, 274718355975078, 308175678115746 @ 14, -19, -5
251443737700485, 299042777218395, 331691921259038 @ 68, 34, -77
256910072363323, 125697352146654, 366453893454240 @ 40, 20, -44
276393131794075, 291938143229626, 238911680329090 @ 10, 127, 144
217695580278467, 167295523885635, 369314559700858 @ 130, 254, -137
""".trimIndent().mapLines { it.parse(0) }

// note that it may take up to 2,000,000,000,000 (2 trillion) time steps for rock to hit all hailstones!
//   - estimate based on average side of target area (200-400 trillion) and average speed of hailstones (~100)
//   - we expect the speed of the rock to be about the same, but maybe a bit faster
//   - because the difference between hailstone x values is exactly 1/13 that of difference between hailstone y values
//     the y speed must be exactly 13 times the x speed of the rock
//   - we could have speed (1, 13) in which case the rock would start to the right of hailstone 1's x, and below hailstone's y,
//     hitting hailstone 1 first, and then hailstone 2 after 4,312,756,283,388 time steps
//   - multiples of speed (1, 13) would also be permissible, cutting in half the number of time steps
//   - since (h1z-h2z) % 12 == 2, and the first two hailstones are growing apart by 12 every second,
//     we know that dt*vz must be 2 mod 12
//   - therefore, dt must be 1, 2, 5, 7, or 11 mod 12 -- it cannot have divisors 3 or 4
//
//   ...
//
// looking at factors of difference between hailstones with common velocities (in one of the directions), we know that
//   - (vx, vy, vz) of rock must be (26, 331, 53), with positive or negative values
// this is done by comparing a fixed velocity, and computing the factorization of (A-B) for all pairs of hailstones with the same velocity
//   - then the difference between the hailstone velocity and a common multiple of the factorization must equal the rock velocity
//   - and the time to pass between the hailstones can be computed from that
// from looking at parallel lines we can compute dt=359396356949 for these two

class HPair(val ha: Hailstone, val hb: Hailstone) {
    val dx = (ha.x - hb.x).toLong().absoluteValue
    val dy = (ha.y - hb.y).toLong().absoluteValue
    val dz = (ha.z - hb.z).toLong().absoluteValue
    val dvx = (ha.vx - hb.vx).toLong().absoluteValue
    val dvy = (ha.vy - hb.vy).toLong().absoluteValue
    val dvz = (ha.vz - hb.vz).toLong().absoluteValue
}

fun List<String>.part2(): Long {
    if (input.size < 10) return 0L

    val hail = input.mapIndexed { index, s -> s.parse(index) }

    println("--Factorizing difference between hailstones 1 and 2--")
    with (HPair(parallelHailstones2[0], parallelHailstones2[1])) {
        println("dt and vx must be factors in: " + dx.factorize())
        println("dt and vy must be factors in: " + dy.factorize())
        println("vy must be 13 times vx, since that is the difference in factors")
        println("" + dz.factorize() + " speed=$dvz")
        println("  modulo speed: ${dz % dvz}")
    }

    if (false) {
        println()
        println("--Factorizing difference between hailstones 3 and 4--")
        with(HPair(parallelHailstones2[2], parallelHailstones2[3])) {
            println("" + dx.factorize() + " speed=$dvx")
            println("  modulo speed: ${dx % dvx}")
            println("" + dy.factorize() + " speed=$dvy")
            println("  modulo speed: ${dy % dvy}")
            println("" + dz.factorize() + " speed=$dvz")
            println("  modulo speed: ${dz % dvz}")
        }

        println("\n\n=============================================================")
        hail.groupBy { it.vx }
            .filter { it.value.size > 2 }
            .forEach {
                println("--Factorizing difference between hailstones ${it.value} with vx=${it.key}--")
                it.value.pairwise().forEach {
                    println("vx must be factors in: " + HPair(it.first(), it.second()).dx.factorize())
                }
            }

        println("\n\n=============================================================")
        hail.groupBy { it.vy }
            .filter { it.value.size > 2 }
            .forEach {
                println("--Factorizing difference between hailstones ${it.value} with vy=${it.key}--")
                it.value.pairwise().forEach {
                    println("vy must be factors in: " + HPair(it.first(), it.second()).dy.factorize())
                }
            }

        println("\n\n=============================================================")
        hail.groupBy { it.vz }
            .filter { it.value.size > 2 }
            .forEach {
                println("--Factorizing difference between hailstones ${it.value} with vz=${it.key}--")
                it.value.pairwise().forEach {
                    println("vz must be factors in: " + HPair(it.first(), it.second()).dz.factorize())
                }
            }
    }

    if (false) {
        println("\n\n=============================================================")
        //    fun <E> List<E>.sample() = take(10) + takeLast(10)
        fun <E> List<E>.sample() = this
        hail.sortedBy { it.vx }.sample().forEach { println(it) }
        println("---")
        hail.sortedBy { it.vy }.sample().forEach { println(it) }
        println("---")
        hail.sortedBy { it.vz }.sample().forEach { println(it) }
    }

    val vel = Triple(26, -331, 53)
    val hail1 = parallelHailstones2[0] // 281974048517427, 162586692606990, 317438841834668 @ 14, -19, 7
    val x = 270392223533307 // from hailstone with x-speed = 26
    println((hail1.x.toLong() - x) % (vel.first - hail1.vx.toLong()))
    val timeToH1 = (hail1.x.toLong() - x) / (vel.first - hail1.vx.toLong())
    val y = hail1.locAt3(timeToH1).y - vel.second * timeToH1
    val z = hail1.locAt3(timeToH1).z - vel.third * timeToH1
    val rock = Hailstone(0, x.toDouble(), y.toDouble(), z.toDouble(), vel.first.toDouble(), vel.second.toDouble(), vel.third.toDouble())
    println("Rock thrown from ($x, $y, $z) at direction $vel")

    val hail2 = parallelHailstones2[1]
    val timeToH1b = (hail2.x.toLong() - x) / (vel.first - hail2.vx.toLong())
    val yb = hail2.locAt3(timeToH1b).y - vel.second * timeToH1b
    val zb = hail2.locAt3(timeToH1b).z - vel.third * timeToH1b
    val rock2 = Hailstone(0, x.toDouble(), yb.toDouble(), zb.toDouble(), vel.first.toDouble(), vel.second.toDouble(), vel.third.toDouble())
    println("Rock thrown from ($x, $yb, $zb) at direction $vel")

    return x + y + z
}

fun Long.factorize(): Pair<Long, List<Long>> {
    var n = this
    val factors = mutableListOf<Long>()
    while (n % 2 == 0L) {
        factors.add(2)
        n /= 2
    }
    var i = 3L
    while (i * i <= n) {
        while (n % i == 0L) {
            factors.add(i)
            n /= i
        }
        i += 2
    }
    if (n > 1)
        factors.add(n)
    return this to factors
}

data class PossibleCollision(val dt: Long, val vx: Long, val vy: Long)

fun List<String>.part2b(): Int {
    val ph = if (size < 10) parallelHailstones else parallelHailstones2

    fun cross(a: Loc3, b: Loc3) = a.bigLong().cross(b.bigLong())

    // start by computing two different vectors that span the plane of the stone vector
    val hailA1 = ph[0]
    val hailA2 = ph[1]
    val vecA1 = hailA1.xyz() - hailA2.xyz()
    val vecA2 = hailA1.locAt3(1000) - hailA2.xyz()
    val normA = cross(vecA1, vecA2).reduce()
    println("Direction of first normal: $normA")

    // now compute the secondary plane
    val hailB1 = ph[2]
    val hailB2 = ph[3]
    val vecB1 = hailB1.xyz() - hailB2.xyz()
    val vecB2 = hailB1.locAt3(1000) - hailB2.xyz()
    val normB = cross(vecB1, vecB2).reduce()
    println("Direction of second normal: $normB")

    // now the cross product of two normal vectors gives out direction
    val dir = normA.cross(normB).reduce()
    println("Direction of intersecting line: $dir")
    return 0
}

// calculate answers

val day = 24
val input = getDayInput(day, 2023)
println("-".repeat(80))
val testResult = testInput.part1()
println("-".repeat(80))
val testResult2 = testInput.part2()
println("-".repeat(80))
val answer1 = input.part1().also { it.print }
println("-".repeat(80))
val answer2 = input.part2().also { it.print }
println("-".repeat(80))

// print results

AocRunner(day,
    test = { "$testResult, $testResult2" },
    part1 = { answer1 },
    part2 = { answer2 }
).run()
