package aoc.util

import kotlin.random.Random

fun String.alternateRedGreen() = toCharArray().toList().chunked(2).joinToString("") {
    "$ANSI_RED${it[0]}" + (if (it.size == 2) "$ANSI_GREEN${it[1]}" else "")
}

fun String.alternateBlueWhite() = toCharArray().toList().chunked(2).joinToString("") {
    "$ANSI_LIGHT_BLUE${it[0]}" + (if (it.size == 2) "$ANSI_WHITE${it[1]}" else "")
}

fun String.randomBlueWhite() = toCharArray().toList().chunked(2).joinToString("") {
    val ansi1 = if (Random.nextBoolean()) ANSI_LIGHT_BLUE else ANSI_WHITE
    val ansi2 = if (Random.nextBoolean()) ANSI_LIGHT_BLUE else ANSI_WHITE
    "$ANSI_BOLD${ansi1}${it[0]}" + (if (it.size == 2) "${ansi2}${it[1]}" else "")
}

fun alignWord(word: String, blockLength: Int, align: String): String {
    return when (align.toLowerCase()) {
        "left" -> String.format("%-${blockLength}s", word)
        "right" -> String.format("%${blockLength}s", word)
        "center" -> {
            val prefix = (blockLength - word.length) / 2
            val suffix = blockLength - prefix - word.length
            "${" ".repeat(prefix)}$word${" ".repeat(suffix)}"
        }
        else -> word
    }
}

fun printChristmasTree() {
    (0..6).forEach {
        println((" ".repeat(11 - it) + "*".repeat(it*2+1)).alternateRedGreen())
    }
    println(" ".repeat(10) + ANSI_YELLOW + "|||" + ANSI_RESET)
}

fun printSnowScene(size: Int) {
    val rows = 7
    val trees = 8
    val array = Array(rows) { Array(size) { " " } }
    val treeLocs = (1..trees).map { (1..size-5).random() }
    treeLocs.forEach {
        val ht = listOf(1,2,2,3,3,3).random()
        array[ht][it] = "$ANSI_GREEN/"
        array[ht][it+1] = "\\"
        array[ht+1][it-1] = "$ANSI_GREEN/"
        array[ht+1][it] = "_"
        array[ht+1][it+1] = "_"
        array[ht+1][it+2] = "\\"
        array[ht+2][it] = "$ANSI_BROWN¦"
        array[ht+2][it+1] = "¦"
    }

    (0 until rows).onEach { line ->
        (0 until size).forEach {
            val ch = array[line][it]
            if (ch == " ") {
                val char = when ((0..29).random()) {
                    in 1..3 -> "*"
                    in 4..4 -> "¤"
                    in 5..7 -> "•" // "❉❄"
                    in 8..11 -> "·"
                    else -> " "
                }
                val col = if (char == " ") "" else when ((0..4).random()) {
                    0 -> ANSI_LIGHT_BLUE
                    1 -> ANSI_WHITE
                    2 -> ANSI_LIGHT_CYAN
                    3 -> ANSI_DARK_CYAN
                    else -> ANSI_WHITE
                }
                array[line][it] = col+char
            }
        }
    }
    array.forEach {
        val line = it.joinToString("")
        println(line.replace("$ANSI_GREEN/\\ ", "$ANSI_GREEN/\\$ANSI_LIGHT_CYAN·"))
    }
}