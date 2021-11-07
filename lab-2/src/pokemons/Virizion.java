package pokemons;

import moves.*;
import ru.ifmo.se.pokemon.*;

public class Virizion extends Pokemon{
    public Virizion(String name, int level) {
        super(name, level);
        setStats(91,90,72,90,129,108);
        setType(Type.GRASS, Type.FIGHTING);
        setMove(new Blizzard(), new SteelWing(), new AquaJet(), new RockSlide());
    }
}
