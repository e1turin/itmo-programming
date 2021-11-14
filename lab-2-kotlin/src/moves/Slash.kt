package moves

import ru.ifmo.se.pokemon.*

class Slash : PhysicalMove(Type.NORMAL, 70.0, 100.0) {
    override fun applySelfEffects(p: Pokemon) {
        p.setMod(Stat.SPEED, 4)
    }

    public override fun describe(): String {
        return "is using Slash"
    }
}