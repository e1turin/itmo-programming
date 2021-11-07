package moves;

import ru.ifmo.se.pokemon.*;

public class Blizzard extends SpecialMove{
    public Blizzard(){
        super(Type.ICE, 110, 70);
    }


    protected void applyOppEffects(Pokemon p){
        if(Math.random()<0.1D){
            Effect.freeze(p);
        }
    }

    protected void applySelfEffects(Pokemon p){
        if(Math.random()<0.1D){
            Effect.freeze(p);
        }
    }

    protected String describe(){
        return "is using Blizzard";
    }

}
