package pokemons

import moves.CrossChop

import ru.ifmo.se.pokemon.*

class Carracosta(name: String?, level: Int) : Tirtouga(name, level) {
    init {
        setStats(74.0, 108.0, 133.0, 83.0, 65.0, 32.0)
        setType(Type.WATER, Type.ROCK)
        addMove(CrossChop())
    }
}