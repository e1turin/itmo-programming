package pokemons

import moves.*

import ru.ifmo.se.pokemon.*

open class Gible(name: String?, level: Int) : Pokemon(name, level) {
    init {
        setStats(58.0, 70.0, 45.0, 40.0, 45.0, 42.0)
        setType(Type.DRAGON, Type.GROUND)
        setMove(Facade(), Swagger())
    }
}