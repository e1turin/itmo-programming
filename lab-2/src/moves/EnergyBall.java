package moves;

import ru.ifmo.se.pokemon.*;
public class EnergyBall extends SpecialMove{
    public EnergyBall(){
        super(Type.GRASS, 90, 100);
    }

    protected void applyOppEffects(Pokemon p){
        if(Math.random()<0.1D) {
            p.setMod(Stat.SPECIAL_DEFENSE, -1);
        }
    }

    public String describe(){
        return "is using Energy Ball";
    }

}
