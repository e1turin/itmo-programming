package pokemons

import moves.*

import ru.ifmo.se.pokemon.*

class Virizion(name: String?, level: Int) : Pokemon(name, level) {
    init {
        setStats(91.0, 90.0, 72.0, 90.0, 129.0, 108.0)
        setType(Type.GRASS, Type.FIGHTING)
        setMove(Blizzard(), SteelWing(), AquaJet(), RockSlide())
    }
}