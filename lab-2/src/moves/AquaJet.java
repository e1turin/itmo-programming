package moves;

import ru.ifmo.se.pokemon.*;

public class AquaJet extends PhysicalMove {
    public AquaJet(){
        super(Type.WATER, 40, 100, 1, 1);
    }

    protected String describe(){
        return "is using Aqua Jet";
    }

}
