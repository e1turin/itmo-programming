package moves;
import ru.ifmo.se.pokemon.*;

public class RockSlide extends PhysicalMove{
    public RockSlide(){
        super(Type.ROCK, 75, 90);
    }

    protected void applyOppEffects(Pokemon p){
        if(Math.random()<0.3){
            Effect.flinch(p);
        }
    }

    protected String describe(){
        return "is using Rock Slide";
    }
}
