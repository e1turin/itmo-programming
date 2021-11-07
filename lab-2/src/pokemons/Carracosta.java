package pokemons;

import moves.*;
import ru.ifmo.se.pokemon.*;

public class Carracosta extends Tirtouga{
    public Carracosta(String name, int level) {
        super(name, level);
        setStats(74,108,133,83,65,32);
        setType(Type.WATER, Type.ROCK);
        addMove(new CrossChop());
    }
}
