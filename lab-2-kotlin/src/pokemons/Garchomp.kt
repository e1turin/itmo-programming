package pokemons

import moves.EnergyBall

import ru.ifmo.se.pokemon.*

class Garchomp(name: String?, level: Int) : Gabite(name, level) {
    init {
        setStats(108.0, 130.0, 95.0, 80.0, 85.0, 102.0)
        setType(Type.DRAGON, Type.GROUND)
        setMove(EnergyBall())
    }
}