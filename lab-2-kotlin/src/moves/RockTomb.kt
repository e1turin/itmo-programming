package moves

import ru.ifmo.se.pokemon.*

class RockTomb : PhysicalMove(Type.ROCK, 60.0, 95.0) {
    override fun applyOppEffects(p: Pokemon) {
        p.setMod(Stat.SPEED, -1)
    }

    public override fun describe(): String {
        return "is using Rock Tomb"
    }
}