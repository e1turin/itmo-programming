package moves

import ru.ifmo.se.pokemon.*

class EnergyBall : SpecialMove(Type.GRASS, 90.0, 100.0) {
    override fun applyOppEffects(p: Pokemon) {
        if (Math.random() < 0.1) {
            p.setMod(Stat.SPECIAL_DEFENSE, -1)
        }
    }

    public override fun describe(): String {
        return "is using Energy Ball"
    }
}