package aoc

import aoc.util.ANSI_BLUE
import aoc.util.ANSI_RED
import aoc.util.ANSI_RESET
import aoc.util.chunkint

class Spell(
    val name: String,
    val costMana: Int,
    val instantDamage: Int = 0,
    val heal: Int = 0,
    val effect: Effect? = null
)

class Effect(val turns: Int, val increaseArmor: Int = 0, val increaseDamage: Int = 0, val increaseMana: Int = 0)

class AocDay22: AocDay(22) {
    companion object {
        @JvmStatic fun main(args: Array<String>) { AocDay22().run() }
        val MagicMissile = Spell("Magic Missile", 53, 4)
        val Drain = Spell("Drain", 73, 2, 2)
        val Shield = Spell("Shield", 113, effect = Effect(6, increaseArmor = 7))
        val Poison = Spell("Poison", 173, effect = Effect(6, increaseDamage = 3))
        val Recharge = Spell("Recharge", 229, effect = Effect(5, increaseMana = 101))
        val spells = listOf(MagicMissile, Drain, Shield, Poison, Recharge)

        // search threshold for a win
        const val MAX_MANA = 3000
    }

    class Player(
        val hp: Int, val mana: Int, val manaSpent: Int, val spellsCast: Int,
        val spellTurns: Map<Spell, Int> = spells.associateWith { 0 }
    ) {
        val effectArmor = spellTurns.filter { it.value > 0 }.map { it.key.effect!!.increaseArmor }.sum()
        val effectDamage = spellTurns.filter { it.value > 0 }.map { it.key.effect!!.increaseDamage }.sum()
        val effectMana = spellTurns.filter { it.value > 0 }.map { it.key.effect!!.increaseMana }.sum()
    }
    class Boss(val hp: Int, val damage: Int)

    override val testinput = "".lines()

    fun battle(initialPlayer: Player, initialBoss: Boss, hardMode: Boolean, spellCast: (Player) -> List<Spell>): Int {
        var inProgress = mutableListOf<Pair<Player, Boss>>(initialPlayer to initialBoss)
        val wonGames = mutableListOf<Pair<Player, Boss>>()
        while (inProgress.isNotEmpty()) {
            val nextRound = mutableListOf<Pair<Player, Boss>>()
            inProgress.forEach { (p, b) ->
                // player turn
                println("\n${ANSI_BLUE}-- Player turn --")
                val playerWins = printGameInfo(p, b, hardMode)
                val (player, boss) = applySpellEffects(p, b, hardMode)

                if (player.hp <= 0) {
                    println("Player loses their last hit point, and the boss wins.")
                } else if (playerWins) {
                    wonGames += player to boss
                } else {
                    // player casts one of several spells
                    spellCast(player).forEach {
                        if (it.costMana > player.mana) {
                            println("Player does not have enough mana to cast ${it.name}.")
                            throw Exception("Player does not have enough mana to cast ${it.name}.")
                        }
                        if (it.instantDamage > 0)
                            println("Player casts ${it.name}, dealing ${it.instantDamage} damage.")
                        else
                            println("Player casts ${it.name}.")
                        val playerAfterCast = Player(
                            player.hp + it.heal,
                            player.mana - it.costMana,
                            player.manaSpent + it.costMana,
                            player.spellsCast + 1,
                            player.spellTurns.toMap() + mapOf(it to (it.effect?.turns ?: 0))
                        )
                        val bossAfterCast = Boss(boss.hp - it.instantDamage, boss.damage)

                        // check if boss is dead
                        if (boss.hp <= 0) {
                            println("This kills the boss, and the player wins.")
                            wonGames += playerAfterCast to bossAfterCast
                        } else {
                            println("\n${ANSI_RED}-- Boss turn --")
                            val playerWins2 = printGameInfo(playerAfterCast, bossAfterCast, false)
                            val (player2, boss2) = applySpellEffects(playerAfterCast, bossAfterCast, false)

                            if (playerWins2) {
                                wonGames += playerAfterCast to bossAfterCast
                            } else {
                                // boss attacks
                                val damage = maxOf(1, boss2.damage - player2.effectArmor)
                                println("Boss attacks for $damage damage!")
                                if (damage >= player2.hp) {
                                    println("This kills the player, and the boss wins.")
                                } else {
                                    val playerAfterAttack = Player(
                                        player2.hp - damage, player2.mana, player2.manaSpent, player2.spellsCast,
                                        player2.spellTurns
                                    )
                                    nextRound += playerAfterAttack to boss2
                                }
                            }
                        }
                    }
                }
            }
            inProgress = nextRound.filter { it.first.manaSpent <= MAX_MANA }.toMutableList()
        }
        return wonGames.minOf { it.first.manaSpent }
    }

    fun printGameInfo(player: Player, boss: Boss, playerHardMode: Boolean): Boolean {
        val hp = if (playerHardMode) "${player.hp} (-1)" else "${player.hp}"
        println("- Player has $hp hit points, ${player.effectArmor} armor, ${player.mana} mana")
        println("- Boss has ${boss.hp} hit points")

        if (player.spellTurns[Shield]!! > 0)
            println("Shield's timer is now ${player.spellTurns[Shield]!! - 1}.")
        if (player.spellTurns[Recharge]!! > 0)
            println("Recharge provides ${player.effectMana} mana; its timer is now ${player.spellTurns[Recharge]!! - 1}.")

        // check if boss is dead
        return if (boss.hp <= player.effectDamage) {
            println("Poison deals ${player.effectDamage} damage. This kills the boss, and the player wins.")
            true
        } else {
            if (player.effectDamage > 0)
                println("Poison deals ${player.effectDamage} damage; its timer is now ${player.spellTurns[Poison]!! - 1}")
            false
        }
    }

    fun applySpellEffects(player: Player, boss: Boss, playerHardMode: Boolean): Pair<Player, Boss> {
        val playerAfterEffects = Player(
            player.hp + if (playerHardMode) -1 else 0,
            player.mana + player.effectMana,
            player.manaSpent,
            player.spellsCast,
            player.spellTurns.mapValues { maxOf(it.value - 1, 0) }.toMap()
        )
        val bossAfterEffects = Boss(boss.hp - player.effectDamage, boss.damage)
        return playerAfterEffects to bossAfterEffects
    }

    init {
        // run tests
        println("\n${ANSI_RESET} BATTLE SIM 1")
        println("=".repeat(40))
        val sample1 = listOf(Poison, MagicMissile)
        battle(Player(10, 250, 0, 0), Boss(13, 8), false) {
            p: Player -> listOf(sample1[p.spellsCast])
        }

        println("\n${ANSI_RESET} BATTLE SIM 2")
        println("=".repeat(40))
        val sample2 = listOf(Recharge, Shield, Drain, Poison, MagicMissile)
        battle(Player(10, 250, 0, 0), Boss(14, 8), false) {
                p: Player -> listOf(sample2[p.spellsCast])
        }

        println("\n${ANSI_RESET} BATTLE SIM 3")
        println("=".repeat(40))
        val sample3 = listOf(Shield, Recharge, Poison, MagicMissile)
        battle(Player(10, 400, 0, 0), Boss(13, 8), true) {
                p: Player -> listOf(sample3[p.spellsCast])
        }

        println("\n${ANSI_RESET} BATTLE SIM 4")
        println("=".repeat(40))
        val sample4 = listOf(Shield, Recharge, Drain, Poison, MagicMissile)
        battle(Player(14, 400, 0, 0), Boss(8, 7), true) {
                p: Player -> listOf(sample4[p.spellsCast])
        }
    }

    override fun calc1(input: List<String>): Int {
        if (input.size < 2) return 0

        println("\n${ANSI_RESET} PART 1 SOLVER")
        println("=".repeat(40))
        val initBoss = Boss(input[0].chunkint(2), input[1].chunkint(1))
        val initPlayer = Player(50, 500, 0, 0)
        val spellCaster = { player: Player ->
            spells.filter { player.mana >= it.costMana && player.spellTurns[it]!! == 0 }
        }
        return battle(initPlayer, initBoss, false, spellCaster)
    }

    override fun calc2(input: List<String>): Int {
        if (input.size < 2) return 0

        println("\n${ANSI_RESET} PART 2 SOLVER")
        println("=".repeat(40))
        val initBoss = Boss(input[0].chunkint(2), input[1].chunkint(1))
        val initPlayer = Player(50, 500, 0, 0)
        val spellCaster = { player: Player ->
            spells.filter { player.mana >= it.costMana && player.spellTurns[it]!! == 0 }
        }
        return battle(initPlayer, initBoss, true, spellCaster)
    }
}