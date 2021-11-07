package moves;
import ru.ifmo.se.pokemon.*;
public class CrossChop extends PhysicalMove{
    public CrossChop(){
        super(Type.FIGHTING, 100, 80);
    }


    protected void  applySelfEffects(Pokemon p){
        p.setMod(Stat.SPEED, 4);
    }

    public String describe(){
        return "is using Cross Chop";
    }


}
