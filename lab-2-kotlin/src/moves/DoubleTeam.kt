package moves

import ru.ifmo.se.pokemon.*

class DoubleTeam : StatusMove(Type.NORMAL, 0.0, 100.0) {
    override fun applySelfEffects(p: Pokemon) {
        p.setMod(Stat.EVASION, 1)
    }

    override fun describe(): String {
        return "is using Double Team"
    }
}