package moves

import ru.ifmo.se.pokemon.*

class Blizzard : SpecialMove(Type.ICE, 110.0, 70.0) {
    override fun applyOppEffects(p: Pokemon) {
        if (Math.random() < 0.1) {
            Effect.freeze(p)
        }
    }

    override fun applySelfEffects(p: Pokemon) {
        if (Math.random() < 0.1) {
            Effect.freeze(p)
        }
    }

    override fun describe(): String {
        return "is using Blizzard"
    }
}