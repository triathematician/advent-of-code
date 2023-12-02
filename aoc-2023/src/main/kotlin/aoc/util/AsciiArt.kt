package aoc.util

import kotlin.random.Random

fun String.alternateRedGreen() = toCharArray().toList().chunked(2).joinToString("") {
    "$ANSI_RED${it[0]}" + (if (it.size == 2) "$ANSI_GREEN${it[1]}" else "")
}

fun String.alternateBlueWhite() = toCharArray().toList().chunked(2).joinToString("") {
    "$ANSI_LIGHTBLUE${it[0]}" + (if (it.size == 2) "$ANSI_WHITE${it[1]}" else "")
}

fun String.randomBlueWhite() = toCharArray().toList().chunked(2).joinToString("") {
    val ansi1 = if (Random.nextBoolean()) ANSI_LIGHTBLUE else ANSI_WHITE
    val ansi2 = if (Random.nextBoolean()) ANSI_LIGHTBLUE else ANSI_WHITE
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

fun printSnowScene() {
    val array = Array(6) { Array(40) { " " } }
    val treeLocs = (1..6).map { (1..35).random() }
    treeLocs.forEach {
        val ht = listOf(1,2,2,3,3,3).random()
        array[ht][it] = "$ANSI_GREEN/"
        array[ht][it+1] = "$ANSI_GREEN\\"
        array[ht+1][it-1] = "$ANSI_GREEN/"
        array[ht+1][it] = "${ANSI_GREEN}_"
        array[ht+1][it+1] = "${ANSI_GREEN}_"
        array[ht+1][it+2] = "$ANSI_GREEN\\"
        array[ht+2][it] = "$ANSI_BROWN|"
        array[ht+2][it+1] = "$ANSI_BROWN|"
    }

    (0..5).onEach { line ->
        (0..39).forEach {
            val ch = array[line][it]
            if (ch == " ") {
                array[line][it] = when ((0..1).random()) {
                    0 -> ANSI_LIGHTBLUE
                    else -> ANSI_WHITE
                } + when ((0..9).random()) {
                    in 0..1 -> "*"
                    in 2..3 -> "."
                    else -> " "
                }
            }
        }
    }
    array.forEach { println(it.joinToString("")) }
}