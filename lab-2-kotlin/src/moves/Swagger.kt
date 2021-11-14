package moves

import ru.ifmo.se.pokemon.*

class Swagger : StatusMove(Type.NORMAL, 0.0, 85.0) {
    override fun applyOppEffects(p: Pokemon) {
        p.confuse()
        p.setMod(Stat.ATTACK, 2)
    }

    override fun describe(): String {
        return "is using Swagger"
    }
}