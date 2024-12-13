package aoc.util

/** Representation of equation a*x+b*y=c. */
class Exy(val a: Int, val b: Int, val c: Int)
/** Representation of equation a*x+b*y+c*z=d. */
class Exyz(val a: Int, val b: Int, val c: Int, val d: Int)

/** Solve two equations and return the (x,y) solution. */
fun solveInt(eq1: Exy, eq2: Exy): Coord? {
    val num_x = eq2.b * eq1.c - eq1.b * eq2.c
    val denom_x = eq1.a * eq2.b - eq2.a * eq1.b
    val num_y = eq2.a * eq1.c - eq1.a * eq2.c
    val denom_y = eq1.b * eq2.a - eq2.b * eq1.a

    return when {
        // no solution or infinite solutions
        denom_x == 0 || denom_y == 0 -> null
        // no integer solution
        num_x % denom_x != 0 || num_y % denom_y != 0 -> null
        // integer solution
        else -> Coord(num_x / denom_x, num_y / denom_y)
    }
}

/** Representation of equation a*x+b*y=c. */
class ExyLong(val a: Long, val b: Long, val c: Long)
/** Representation of equation a*x+b*y+c*z=d. */
class ExyzLong(val a: Long, val b: Long, val c: Long, val d: Long)

/** Solve two equations and return the (x,y) solution. */
fun solveLong(eq1: ExyLong, eq2: ExyLong): Pair<Long, Long>? {
    val num_x = eq2.b * eq1.c - eq1.b * eq2.c
    val denom_x = eq1.a * eq2.b - eq2.a * eq1.b
    val num_y = eq2.a * eq1.c - eq1.a * eq2.c
    val denom_y = eq1.b * eq2.a - eq2.b * eq1.a

    return when {
        // no solution or infinite solutions
        denom_x == 0L || denom_y == 0L -> null
        // no integer solution
        num_x % denom_x != 0L || num_y % denom_y != 0L -> null
        // integer solution
        else -> num_x / denom_x to num_y / denom_y
    }
}