package moves

import ru.ifmo.se.pokemon.*

class CrossChop : PhysicalMove(Type.FIGHTING, 100.0, 80.0) {
    override fun applySelfEffects(p: Pokemon) {
        p.setMod(Stat.SPEED, 4)
    }

    public override fun describe(): String {
        return "is using Cross Chop"
    }
}