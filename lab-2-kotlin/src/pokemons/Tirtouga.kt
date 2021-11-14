package pokemons

import moves.*

import ru.ifmo.se.pokemon.*

open class Tirtouga(name: String?, level: Int) : Pokemon(name, level) {
    init {
        setStats(54.0, 78.0, 103.0, 53.0, 45.0, 22.0)
        setType(Type.WATER, Type.ROCK)
        setMove(Slash(), RockTomb(), DoubleTeam())
    }
}