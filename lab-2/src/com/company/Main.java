package com.company;

import pokemons.*;
import ru.ifmo.se.pokemon.*;

public class Main {
    public static void main(String[] args) {
        Battle battle = new Battle();
        Virizion p1 = new Virizion("Viri", 5);
        Tirtouga p2 = new Tirtouga("Tirt", 5);
        Carracosta p3 = new Carracosta("Carra", 5);
        Gible p4 = new Gible("Ble", 4);
        Gabite p5 = new Gabite("Bite", 4);
        Garchomp p6 = new Garchomp("Garch", 4);
        battle.addAlly(p1);
        battle.addAlly(p2);
        battle.addAlly(p3);
        battle.addFoe(p4);
        battle.addFoe(p5);
        battle.addFoe(p6);
        battle.go();

    }
}
