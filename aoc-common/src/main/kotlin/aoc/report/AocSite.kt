package aoc.report

import aoc.input.ROOT_FOLDER
import aoc.input.SESSION_ID
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import java.io.File
import java.net.HttpURLConnection
import java.net.URL

/** Tools for downloading content from the AOC site. */
object AocSite {
    private const val MIN_LEADERBOARD_REFRESH_MILLIS = 1000 * 60 * 5

    /** Get leaderboards. */
    fun getLeaderboards(year: Int) {
        download(year, "leaderboard/private/view/917872.json", "site/stats/leaderboard.json", MIN_LEADERBOARD_REFRESH_MILLIS)
        download(year, "leaderboard/private/view/3240090.json", "site/stats/leaderboard2.json", MIN_LEADERBOARD_REFRESH_MILLIS)
    }

    fun download(year: Int, aocPath: String, locPath: String, refreshTime: Int): String {
        val ts = System.currentTimeMillis()
        val file = resourceFile(year, locPath)
        if (!file.exists() || ts - file.lastModified() > refreshTime) {
            file.parentFile.mkdirs()
            readAocText(year, aocPath)?.let {
                println("Successfully downloaded $locPath from $aocPath - ${file.absolutePath}.")
                file.writeText(it)
                return it
            }
        }
        return file.readText()
    }

    //region HTTP calls

    private fun readAocText(year: Int, path: String): String? {
        val url = URL("https://adventofcode.com/$year/$path")
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"
        connection.setRequestProperty("Cookie", "session=$SESSION_ID")
        if (connection.responseCode == HttpURLConnection.HTTP_OK) {
            return connection.inputStream.bufferedReader().readText()
        }
        return null
    }

    //endregion

    fun rootDir(year: Int): File {
        var root = File("").absoluteFile
        while (root.name != ROOT_FOLDER) root = root.parentFile
        return File(root, "aoc-$year")
    }

    fun resourceFile(year: Int, locPath: String) =
        File(rootDir(year), "src/main/resources/aoc/$locPath")

    fun readLeaderboard(year: Int, locPath: String): AocLeaderboard {
        val json = resourceFile(year, locPath).readText()
        return ObjectMapper()
            .registerModule(KotlinModule.Builder().build())
            .readValue(json)
    }
}