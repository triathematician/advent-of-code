package aoc

import aoc.util.chunk
import aoc.util.chunkint

class AocDay21: AocDay(21) {
    companion object { @JvmStatic fun main(args: Array<String>) { AocDay21().run() } }

    val PRINT_ALL_LOADOUTS = false

    val shopinput = """
        Weapons:    Cost  Damage  Armor
        Dagger        8     4       0
        Shortsword   10     5       0
        Warhammer    25     6       0
        Longsword    40     7       0
        Greataxe     74     8       0

        Armor:      Cost  Damage  Armor
        None          0     0       0
        Leather      13     0       1
        Chainmail    31     0       2
        Splintmail   53     0       3
        Bandedmail   75     0       4
        Platemail   102     0       5

        Rings:      Cost  Damage  Armor
        Damage +1    25     1       0
        Damage +2    50     2       0
        Damage +3   100     3       0
        Defense +1   20     0       1
        Defense +2   40     0       2
        Defense +3   80     0       3
    """.trimIndent()

    class Shop(
        val weapons: List<Item>,
        val armor: List<Item>,
        val rings: List<Item>
    )
    class Item(val name: String, val cost: Int, val damage: Int, val armor: Int) {
        override fun toString() = name
    }
    fun String.item() = replace("\\s+".toRegex()," ").replace(" +","+").let {
        Item(it.chunk(0), it.chunkint(1), it.chunkint(2), it.chunkint(3))
    }

    val shop = shopinput.lines().let { input ->
        val weapons = (1..5).map { input[it].item() }
        val armor = (8..13).map { input[it].item() }
        val rings = (16..21).map { input[it].item() }
        Shop(weapons, armor, rings)
    }

    override val testinput = """
        Hit Points: 12
        Damage: 7
        Armor: 2
    """.trimIndent().lines()

    /** All possible loadouts, sorted by cost. */
    val loadouts: Map<List<Item>, Int> by lazy {
        val res = mutableListOf<List<Item>>()
        shop.weapons.forEach { w ->
            shop.armor.forEach { a ->
                res += listOf(w, a)
                shop.rings.forEach { r1 ->
                    res += listOf(w, a, r1)
                }
                shop.rings.forEachIndexed { i, r1 ->
                    shop.rings.drop(i+1).forEach { r2 ->
                        res += listOf(w, a, r1, r2)
                    }
                }
            }
        }
        val resCosts = res.associateWith { it.sumOf { it.cost } }
            .entries.sortedBy { it.value }
            .associate { it.key to it.value }

        if (!PRINT_ALL_LOADOUTS) {
            println("There are ${res.size} possible loadouts")
        } else {
            println("Possible Loadouts")
            println("-----------------")
            resCosts.forEach { println(it.key.printStats()) }
        }
        resCosts
    }

    fun List<Item>.printStats(): String {
        return "${joinToString(", ")} " +
                "[${sumOf { it.cost }}¢ ${sumOf { it.damage }}⚔ ${sumOf { it.armor }}⛨]"
    }

    fun playerWins(playerHp: Int, playerLoadout: List<Item>, bossHp: Int, bossDamage: Int, bossArmor: Int): Boolean {
        return battle(playerHp, playerLoadout, bossHp, bossDamage, bossArmor, false).first > 0
    }

    /** Battle player/boss, returning Pair(player, boss). */
    fun battle(playerHp: Int, playerLoadout: List<Item>, bossHp: Int, bossDamage: Int, bossArmor: Int, print: Boolean): Pair<Int, Int> {
        var player = playerHp
        var boss = bossHp
        var playerTurn = true
        while (player > 0 && boss > 0) {
            if (playerTurn) {
                val damage = maxOf(1, playerLoadout.sumOf { it.damage } - bossArmor)
                boss -= damage
                if (print) {
                    println("  - Player deals $damage damage; boss goes down to $boss hit points.")
                }
            } else {
                val damage = maxOf(1, bossDamage - playerLoadout.sumOf { it.armor })
                player -= damage
                if (print) {
                    println("  - Boss deals $damage damage; player goes down to $player hit points.")
                }
            }
            playerTurn = !playerTurn
        }
        return player to boss
    }

    override fun calc1(input: List<String>): Int {
        val loadouts = loadouts
        val bossHp = input[0].chunkint(2)
        val bossDamage = input[1].chunkint(1)
        val bossArmor = input[2].chunkint(1)
        val playerHp = if (bossHp == 12) 8 else 100
        val minLoadout = loadouts.entries.first {
            playerWins(playerHp, it.key, bossHp, bossDamage, bossArmor)
        }
        println("-".repeat(80))
        println("Min loadout: ${minLoadout.key.printStats()}")
        println("-".repeat(80))
        println("Battle sim:")
        battle(playerHp, minLoadout.key, bossHp, bossDamage, bossArmor, true)
        println("-".repeat(80))
        return minLoadout.value
    }
    override fun calc2(input: List<String>): Int {
        val loadouts = loadouts
        val bossHp = input[0].chunkint(2)
        val bossDamage = input[1].chunkint(1)
        val bossArmor = input[2].chunkint(1)
        val playerHp = if (bossHp == 12) 8 else 100
        val minLoadout = loadouts.entries.reversed().first {
            !playerWins(playerHp, it.key, bossHp, bossDamage, bossArmor)
        }
        println("-".repeat(80))
        println("Max loadout: ${minLoadout.key.printStats()}")
        println("-".repeat(80))
        println("Battle sim:")
        battle(playerHp, minLoadout.key, bossHp, bossDamage, bossArmor, true)
        println("-".repeat(80))
        return minLoadout.value
    }
}