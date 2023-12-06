package aoc.util

import java.io.File
import java.net.HttpURLConnection
import java.net.URL

/** The root folder, parent for all year AOC projects. */
const val ROOT_FOLDER = "advent-of-code"
/** The cookie session id, copied from web browser. */
val SESSION_ID = File("D:\\code\\advent-of-code\\cookie.txt").readText()

/** Get input for given day and year of AOC, copy into a local directory. */
fun getDayInput(day: Int, year: Int): List<String> {
    val root = rootDir(year)
    val input = File(root, "src/main/resources/aoc/input/aoc$day.txt")
    if (!input.exists()) {
        input.parentFile.mkdirs()
        val url = URL("https://adventofcode.com/$year/day/$day/input")
        val connection = url.openConnection() as HttpURLConnection
        connection.setRequestProperty("Cookie", "session=$SESSION_ID")
        connection.requestMethod = "GET"
        if (connection.responseCode == HttpURLConnection.HTTP_OK) {
            val text = connection.inputStream.bufferedReader().readText()
            input.createNewFile()
            input.writeText(text)
            println("Downloaded input for Dec $day $year")
        }
    }
    val lines = input.readText().trim().lines()
    return if (lines.last().isEmpty()) lines.dropLast(1) else lines
}

private fun rootDir(year: Int): File {
    var root = File("").absoluteFile
    while (root.name != ROOT_FOLDER) root = root.parentFile
    return File(root, "aoc-$year")
}