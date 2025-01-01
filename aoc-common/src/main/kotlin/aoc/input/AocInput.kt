package aoc.input

import aoc.report.AocSite
import java.io.File

/** The root folder, parent for all year AOC projects. */
const val ROOT_FOLDER = "advent-of-code"
/** The cookie session id, copied from web browser. */
val SESSION_ID = File("D:\\code\\advent-of-code\\cookie.txt").readText()

/** Get input for given day and year of AOC, copy into a local directory. */
fun getDayInput(day: Int, year: Int): List<String> {
    val input = AocSite.download(year, "day/$day/input", "site/input/aoc$day.txt", Int.MAX_VALUE)
    return input.trim().lines()
}