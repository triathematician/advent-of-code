package aoc.util

data class Loc(val x: Int = 0, val y: Int = 0) {
    operator fun plus(dir: Compass) = Loc(x + dir.dx, y + dir.dy)
}

data class Loc2Dir(val x: Int, val y: Int, val dir: Compass)
data class Loc3(val x: Int, val y: Int, val z: Int)

enum class Compass(val c: Char, val dx: Int, val dy: Int) {
    NORTH('^', 0, -1),
    EAST('>', 1, 0),
    WEST('<', -1, 0),
    SOUTH('v', 0, 1),
    NONE(' ', 0, 0)
}

fun String.parsePath() = map { c -> Compass.values().first { it.c == c }}

fun List<Compass>.walk(from: Loc = Loc()): List<Loc> {
    var p = from
    val path = mutableListOf(p)
    forEach { dir ->
        p += dir
        path += p
    }
    return path
}