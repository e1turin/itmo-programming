package moves;

import ru.ifmo.se.pokemon.*;

public class Slash extends PhysicalMove {
    public Slash(){
        super(Type.NORMAL, 70, 100);
    }

    protected void applySelfEffects(Pokemon p){
        p.setMod(Stat.SPEED, 4);
    }

    public String describe(){
        return "is using Slash";
    }
}
