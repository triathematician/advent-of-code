package aoc

class StackVisualizer(val stacks: List<List<Any?>>) {
    var align = "center"
    var stackDelim = '|'
    var itemBefore = ' '
    var itemAfter = ' '
    var binDelim = '+'
    var borderTop = true
    var borderBottom = true
    var printLabels = true

    fun visualize() {
        val maxItemLength = stacks.flatten().maxOfOrNull { it.toString().length } ?: 0
        val maxStackHeight = stacks.maxOfOrNull { it.size } ?: 0

        if (borderTop)
            println("+${"-".repeat(maxItemLength + 2)}".repeat(stacks.size)+"+")

        repeat(maxStackHeight) { i ->
            print(stackDelim)
            stacks.forEach {
                val alignedWord = alignWord(it.getOrNull(maxStackHeight - 1 - i)?.toString() ?: "", maxItemLength, align)
                if (alignedWord.isBlank())
                    print(" $alignedWord $stackDelim")
                else
                    print("$ANSI_WHITE$itemBefore$ANSI_BLUE$ANSI_BOLD$alignedWord$ANSI_WHITE$itemAfter$ANSI_RESET$stackDelim")
            }
            println()
        }

        if (borderBottom)
            println("$binDelim${"-".repeat(maxItemLength + 2)}".repeat(stacks.size)+"+")

        if (printLabels) {
            print(" ")
            stacks.indices.forEach {
                print(alignWord("${it + 1}", maxItemLength + 2, "center") + " ")
            }
            println(" ")
        }
    }
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

fun main() {
    val stacks = listOf(
        listOf("apple", "banana"),
        listOf("dog", "cat", "bird", "peregrine falcon"),
        listOf("red", "green", "blue")
    )
    val visualizer = StackVisualizer(stacks)
    visualizer.visualize()
}