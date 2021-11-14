package com.company

import kotlin.jvm.JvmStatic

import ru.ifmo.se.pokemon.Battle

import pokemons.Virizion
import pokemons.Tirtouga
import pokemons.Carracosta
import pokemons.Gible
import pokemons.Gabite
import pokemons.Garchomp

object Main {
    @JvmStatic
    fun main(args: Array<String>) {
        val battle = Battle()
        val p1 = Virizion("Viri", 5)
        val p2 = Tirtouga("Tirt", 5)
        val p3 = Carracosta("Carra", 5)
        val p4 = Gible("Ble", 4)
        val p5 = Gabite("Bite", 4)
        val p6 = Garchomp("Garch", 4)
        battle.addAlly(p1)
        battle.addAlly(p2)
        battle.addAlly(p3)
        battle.addFoe(p4)
        battle.addFoe(p5)
        battle.addFoe(p6)
        battle.go()
    }
}