package moves;
import ru.ifmo.se.pokemon.*;
public class HealPulse extends StatusMove{
    public HealPulse(){
        super(Type.PSYCHIC, 0, 100);
    }

    /* //TODO make teammates heal baf
    @Override
    protected void applySelfEffects(Pokemon p){
        p.setMod(Stat.HP, -(int)(p.getStat(Stat.HP)/2));
    }
    */

    public String describe(){
        return "is using Heal Pulse";
    }
}
