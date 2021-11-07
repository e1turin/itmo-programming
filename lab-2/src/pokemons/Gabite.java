package pokemons;

import moves.*;
import ru.ifmo.se.pokemon.*;

public class Gabite extends Gible {
    public Gabite (String name, int level) {
        super(name, level);
        setStats(68,90,65,50,55,82);
        setType(Type.DRAGON, Type.GROUND);
        setMove(new HealPulse());
    }
}
