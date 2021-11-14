package moves

import ru.ifmo.se.pokemon.*

class Facade : PhysicalMove(Type.NORMAL, 70.0, 100.0) {
    override fun applyOppDamage(def: Pokemon, damage: Double) {
        val defStatus = def.condition
        if (defStatus == Status.BURN
                || defStatus == Status.POISON
                || defStatus == Status.PARALYZE) {
            super.applyOppDamage(def, damage * 2)
        } else {
            super.applyOppDamage(def, damage)
        }
    }

    override fun describe(): String {
        return "is using Facade"
    }
}