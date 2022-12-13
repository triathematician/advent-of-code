package aoc

import com.fasterxml.jackson.databind.ObjectMapper
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

//region TYPE HELPERS

typealias PairChar = Pair<Char, Char>

//endregion

//region INPUT

fun aocInput(day: Int): String {
    TODO("Get input from AOC site")
}

//endregion

//region PRINTING

val Any?.print: Unit
    get() {
        println(this)
    }

fun String.alternateRedGreen() = toCharArray().toList().chunked(2).joinToString("") {
    "${ANSI_RED}${it[0]}" + (if (it.size == 2) "${ANSI_GREEN}${it[1]}" else "")
} + ANSI_RESET

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

//endregion

//region PARSING

/** Get list of objects, from e.g. [1, 2, [3]] */
val String.parselist: List<*>
    get() = ObjectMapper().readValue(this, List::class.java)

/** Convert to an [IntRange], default separator "-" */
fun String.toIntRange(delim: String = "-") = split(delim).let { it[0].toInt().rangeTo(it[1].toInt()) }

/** Splits this string on [delim] and parses the resulting chunks as integers. */
fun String.ints(delim: String = ",") = split(delim).map { it.trim().toInt() }
/** Splits this string on [delim] and parses the resulting chunks as longs. */
fun String.longs(delim: String = ",") = split(delim).map { it.trim().toLong() }

/** Returns the substring of this string that is after the first occurrence of [before] and before the first occurrence of [after]. */
fun String.between(before: String, after: String) = substringAfter(before).substringBefore(after)
/** Splits on given delimiter, mapping to result. */
fun <X> String.splitOn(delim: String, op: (String) -> X) = split(delim).map(op)

/** Returns the nth chunk from the specified item in this list. */
fun List<String>.chunk(index: Int, n: Int) = this[index].chunk(n)
/** Returns the nth chunk from the specified item in this list, parsed as an integer. */
fun List<String>.chunkint(index: Int, n: Int) = this[index].chunkint(n)
/** Returns the nth chunk from the specified item in this list, parsed as a long. */
fun List<String>.chunklong(index: Int, n: Int) = this[index].chunklong(n)

/** Returns the nth chunk from this string. */
fun String.chunk(n: Int) = trim().split(" ")[n]
/** Returns the nth chunk from this string, parsed as an integer. */
fun String.chunkint(n: Int) = trim().split(" ")[n].toInt()
/** Returns the nth chunk from this string, parsed as a long. */
fun String.chunklong(n: Int) = trim().split(" ")[n].toLong()

//endregion

//region LINE AND GROUP PARSING

/** Divide into groups with line breaks. */
fun <X> String.groupedOnBlankLines(op: (String) -> X) = trim().split("\n\n").map(op)
/** Maps lines to objects. */
fun <X> String.mapLines(op: (String) -> X) = trim().lines().map(op)

/** Convert each line to an Int. */
fun String.linesToInt() = mapLines { it.toInt() }

/** Splits string to list of lists. */
fun String.splitToGroups(groupSep: String, sep: String) = trim().split(groupSep).map { it.split(sep) }

/** Splits string to list of lists. */
fun String.splitToIntGroups(groupSep: String, sep: String) = trim().split(groupSep).map { it.split(sep).map { it.toInt() } }

//endregion

//region STRING UTILS

val String.halves
    get() = listOf(substring(0, length / 2), substring(length / 2, length))

/** Intersecting set of letters between two strings. */
fun String.intersect(other: String) = toSet().intersect(other.toSet())
/** Intersecting set of letters between lots of strings. */
fun List<String>.intersect() = map { it.toSet() }.reduce { x, y -> x.intersect(y) }

/** Detect if all characters are distinct. */
fun String.allDistinctCharacters() = toSet().size == length

//endregion

//region INT UTILS

fun List<IntRange>.fullyOverlapping() = this[0].containsAll(this[1]) || this[1].containsAll(this[0])
fun List<IntRange>.fullyDisjoint() = this[0].last < this[1].first || this[1].last < this[0].first

fun IntRange.containsAll(other: IntRange) = contains(other.first) && contains(other.last)

//endregion

//region DATA STRUCTURE UTILS

/** Get all i,j coordinates for elements in the matrix or list of lists. */
val <X> List<List<X>>.coords: List<Pair<Int, Int>>
    get() = indices.flatMap { i ->
        this[i].indices.map { i to it }
    }

//endregion