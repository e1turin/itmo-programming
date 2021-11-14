package moves

import ru.ifmo.se.pokemon.*

class AquaJet : PhysicalMove(Type.WATER, 40.0, 100.0, 1, 1) {
    override fun describe(): String {
        return "is using Aqua Jet"
    }
}