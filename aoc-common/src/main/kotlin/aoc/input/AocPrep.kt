package aoc.input

import printHeader
import java.io.File
import java.time.LocalDate
import java.time.Month

object AocPrep {

    fun prepForTomorrow(year: Int) {
        printHeader("Updating Files")
        updateFiles(year)
        println("Done!")
    }

    fun updateFiles(year: Int) {
        val rootPath = rootDir(year)
        val codePath = File(rootPath, "src/main/kotlin/aoc/")
        val day = LocalDate.now().let {
            if (it.year == year && it.month == Month.DECEMBER) minOf(it.dayOfMonth + 1, 25) else -1
        }
        if (day != -1) {
            val codeTemplate = File(codePath, "aoc_.kts").readText().replace("day = 0", "day = $day")
            val inputCode = File(codePath, "aoc$day.kts")
            if (!inputCode.exists()) {
                inputCode.writeText(codeTemplate)
                println("Created file ${inputCode.name}")
            }
        }
    }
}