package aoc.aoc2016

import aoc.AocDay

class AocDay10: AocDay(10, 2016) {
    companion object { @JvmStatic fun main(args: Array<String>) { AocDay10().run() } }

    override val testinput = """
        value 5 goes to bot 2
        bot 2 gives low to bot 1 and high to bot 0
        value 3 goes to bot 1
        bot 1 gives low to output 1 and high to bot 0
        bot 0 gives low to output 2 and high to output 0
        value 2 goes to bot 2
    """.trimIndent().lines()

    /**
     * Store info about the bot, including what's in low, what's in high,
     * and rule for giving numbers to target bots or outputs.
     */
    class Bot(val n: Int, val isOutput: Boolean, var low: Int? = null, var high: Int? = null,
              var giveLow: Int? = null, var lowToOutput: Boolean = false,
              var giveHigh: Int? = null, var highToOutput: Boolean = false) {
        fun hasTwo() = low != null && high != null
        fun receive(value: Int) {
            if (low == null) low = value
            else if (low!! < value) high = value
            else {
                high = low
                low = value
            }
        }
        fun emptyHands() {
            low = null
            high = null
        }
    }

    fun List<String>.initBots(): MutableMap<Int, Bot> {
        val searchA = """value (\d+) goes to bot (\d+)""".toRegex()
        val searchB = """bot (\d+) gives low to (bot|output) (\d+) and high to (bot|output) (\d+)""".toRegex()
        val bots = mutableMapOf<Int, Bot>()
        forEach { line ->
            val matchA = searchA.matchEntire(line)
            if (matchA != null) {
                val value = matchA.groupValues[1].toInt()
                val n = matchA.groupValues[2].toInt()
                bots.getOrPut(n) { Bot(n, false) }.apply {
                    receive(value)
                }
            }
            val matchB = searchB.matchEntire(line)
            if (matchB != null) {
                val n = matchB.groupValues[1].toInt()
                val b = bots.getOrPut(n) { Bot(n, false) }.apply {
                    lowToOutput = matchB.groupValues[2] == "output"
                    giveLow = matchB.groupValues[3].toInt()
                    highToOutput = matchB.groupValues[4] == "output"
                    giveHigh = matchB.groupValues[5].toInt()
                }
                if (b.lowToOutput)
                    bots[1000 + b.giveLow!!] = Bot(b.giveLow!!, true)
                if (b.highToOutput)
                    bots[1000 + b.giveHigh!!] = Bot(b.giveHigh!!, true)
            }
        }
        return bots
    }

    fun Map<Int, Bot>.iterate(): Bot? {
        val botToGive = values.firstOrNull { it.hasTwo() } ?: return null
        if (botToGive.low == 17 && botToGive.high == 61)
            println("Bot ${botToGive.n} is responsible for comparing value-17 and value-61")
        val giveLowBot = this[botToGive.giveLow!! + if (botToGive.lowToOutput) 1000 else 0]
        val giveHighBot = this[botToGive.giveHigh!! + if (botToGive.highToOutput) 1000 else 0]
        giveLowBot!!.receive(botToGive.low!!)
        giveHighBot!!.receive(botToGive.high!!)
        botToGive.emptyHands()
        return botToGive
    }

    override fun calc1(input: List<String>): Int {
        val bots = input.initBots()
        var curBot: Bot?
        do {
            curBot = bots.iterate()
        } while (curBot != null)
        return 0
    }
    override fun calc2(input: List<String>): Int {
        val bots = input.initBots()
        var curBot: Bot?
        do {
            curBot = bots.iterate()
        } while (curBot != null)
        return bots[1000]!!.low!! * bots[1001]!!.low!! * bots[1002]!!.low!!
    }
}