package com.company;

import abstracts.Alive;

import java.util.Objects;

public class Policeman extends Alive {
    private String name;
    private Decision decision;

    public Policeman(String name) {
        super(name);
    }

    public Action toShoot(){
        return new Action(name+" стреляет(ют) ", new Quality("Стрельба"));
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Policeman shorty = (Policeman) o;
        return name.equals(shorty.name) && decision.equals(shorty.decision);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, decision);
    }

}
