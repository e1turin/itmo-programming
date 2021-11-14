package moves

import ru.ifmo.se.pokemon.*

class RockSlide : PhysicalMove(Type.ROCK, 75.0, 90.0) {
    override fun applyOppEffects(p: Pokemon) {
        if (Math.random() < 0.3) {
            Effect.flinch(p)
        }
    }

    override fun describe(): String {
        return "is using Rock Slide"
    }
}