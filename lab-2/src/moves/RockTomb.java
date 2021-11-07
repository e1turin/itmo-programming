package moves;

import ru.ifmo.se.pokemon.*;

public class RockTomb extends PhysicalMove {
    public RockTomb(){
        super(Type.ROCK, 60,95);
    }

    protected void applyOppEffects(Pokemon p){
        p.setMod(Stat.SPEED, -1);
    }

    public String describe(){
        return "is using Rock Tomb";
    }
}
