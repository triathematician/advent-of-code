package aoc.util

fun String.parseInts() = split(' ', ',').mapNotNull { it.toIntOrNull() }
