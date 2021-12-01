package com.company;

import abstracts.Alive;

import java.util.Objects;

public class Shorty extends Alive {
    private String name;
    private Decision decision;

    public Shorty(String name) {
        super(name);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Shorty shorty = (Shorty) o;
        return name.equals(shorty.name) && decision.equals(shorty.decision);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, decision);
    }

}
