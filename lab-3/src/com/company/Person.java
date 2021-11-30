package com.company;

import abstracts.Alive;
import abstracts.Closable;
import types.Feeling;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Person extends Alive {
    public Person(String name) {
        super(name);
    }

    private class Face {
        private Set<Feeling> feelings;

        public Face() {
            feelings = new HashSet<Feeling>();
        }

        public Action toReshape(Feeling feeling) {
            feelings.add(feeling);
            return new Action(name + " исказил лицо от чувства " + feeling,
                    new Quality(
                    "Лицо исказилось"));
        }
    }

    Face face = new Face();


    void toDragIn(Person person) {
        System.out.println(name + " втащил(а) " + person.getName());
    }

    void toClose(Closable thing) {
        thing.close();
        System.out.println(name + " закрыл(a)");
    }

    Action toReshapeFace(Feeling feeling) {
        System.out.println(name + " исказил(а) лицо от чувства " + feeling);
        return face.toReshape(feeling);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return name.equals(person.name) && decision.equals(person.decision);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, decision);
    }

}
