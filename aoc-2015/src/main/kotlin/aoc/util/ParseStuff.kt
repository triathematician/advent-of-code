package aoc.util

/** Returns the nth chunk from this string. */
fun String.chunk(n: Int) = trim().split(" ")[n]
/** Returns the nth chunk from this string, parsed as an integer. */
fun String.chunkint(n: Int) = trim().split(" ")[n].toInt()
/** Returns the nth chunk from this string, parsed as a long. */
fun String.chunklong(n: Int) = trim().split(" ")[n].toLong()

fun String.parseInts() = split(' ', ',').mapNotNull { it.toIntOrNull() }

// LINE PARSERS

fun String.parseToMap(pairSep: String = ",", keySep: String = ":", valueOp: (String) -> Any = { it }) =
    split(pairSep).associate { it.trim().split(keySep).let { it[0].trim() to valueOp(it[1].trim()) } }
