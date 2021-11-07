package pokemons;

import moves.*;
import ru.ifmo.se.pokemon.*;

public class Gible extends Pokemon {
    public Gible (String name, int level) {
       super(name, level);
       setStats(58,70,45,40,45,42);
       setType(Type.DRAGON, Type.GROUND);
       setMove(new Facade(), new Swagger());
    }
}
