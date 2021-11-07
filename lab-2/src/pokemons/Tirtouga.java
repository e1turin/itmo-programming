package pokemons;

import moves.*;
import ru.ifmo.se.pokemon.*;

public class Tirtouga extends Pokemon{
    public Tirtouga(String name, int level) {
        super(name, level);
        setStats(54,78,103, 53, 45, 22);
        setType(Type.WATER, Type.ROCK);
        setMove(new Slash(), new RockTomb(), new DoubleTeam());
    }
}
