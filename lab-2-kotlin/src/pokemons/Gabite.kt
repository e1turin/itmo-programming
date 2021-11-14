package pokemons

import moves.HealPulse

import ru.ifmo.se.pokemon.Type

open class Gabite(name: String?, level: Int) : Gible(name, level) {
    init {
        setStats(68.0, 90.0, 65.0, 50.0, 55.0, 82.0)
        setType(Type.DRAGON, Type.GROUND)
        setMove(HealPulse())
    }
}