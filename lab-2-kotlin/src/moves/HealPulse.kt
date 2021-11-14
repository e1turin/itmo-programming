package moves

import ru.ifmo.se.pokemon.*

class HealPulse : StatusMove(Type.PSYCHIC, 0.0, 100.0) {
    /* //TODO make teammates heal baf
    @Override
    protected void applySelfEffects(Pokemon p){
        p.setMod(Stat.HP, -(int)(p.getStat(Stat.HP)/2));
    }
    */
    public override fun describe(): String {
        return "is using Heal Pulse"
    }
}