package moves

import ru.ifmo.se.pokemon.*

class SteelWing : PhysicalMove(Type.STEEL, 70.0, 90.0) {
    override fun applySelfEffects(p: Pokemon) {
        if (Math.random() < 0.1) {
            p.setMod(Stat.DEFENSE, 1)
        }
    }

    override fun describe(): String {
        return "is using Steel Wing"
    }
}