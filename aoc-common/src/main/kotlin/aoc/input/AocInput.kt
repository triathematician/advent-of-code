package aoc.input

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
        val text = readAocText(url)
        if (text != null) {
            input.createNewFile()
            input.writeText(text.trim())
            println("Downloaded input for Dec $day $year")
        }
    }
    return input.readText().trim().lines()
}

private const val MIN_LEADERBOARD_REFRESH_MILLIS = 1000 * 60 * 20

/** Get leaderboards. */
fun getLeaderboards() {
    val ts = System.currentTimeMillis()
    val fl1 = File(rootDir(2024), "src/main/resources/aoc/leaderboard.json")
    if (fl1.exists() && ts - fl1.lastModified() < MIN_LEADERBOARD_REFRESH_MILLIS)
        return

    val url1 = URL("https://adventofcode.com/2024/leaderboard/private/view/917872.json")
    readAocText(url1)?.let {
        println("Successfully downloaded leaderboard 1")
        fl1.writeText(it)
    }
    val fl2 = File(rootDir(2024), "src/main/resources/aoc/leaderboard2.json")
    val url2 = URL("https://adventofcode.com/2024/leaderboard/private/view/3240090.json")
    readAocText(url2)?.let {
        println("Successfully downloaded leaderboard 2")
        fl2.writeText(it)
    }
}

private fun readAocText(url: URL): String? {
    val connection = url.openConnection() as HttpURLConnection
    connection.requestMethod = "GET"
    connection.setRequestProperty("Cookie", "session=$SESSION_ID")
    if (connection.responseCode == HttpURLConnection.HTTP_OK) {
        return connection.inputStream.bufferedReader().readText()
    }
    return null
}

private fun rootDir(year: Int): File {
    var root = File("").absoluteFile
    while (root.name != ROOT_FOLDER) root = root.parentFile
    return File(root, "aoc-$year")
}