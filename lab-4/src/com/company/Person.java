package com.company;

import abstracts.Alive;
import abstracts.Closable;
import abstracts.Thing;
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

    Action toCommandTo(Alive whom, Command cmd){
        System.out.println(name+" скомандовал(а) "+whom.getName()+": "+cmd.getDescription());
        return new Action(name+" скомандовал(а) "+whom.getName()+": "+cmd.getDescription(),
                new Quality("Скомандовали: "+cmd.getDescription()));
    }

    Action toHeal(Alive whom){
        System.out.println(name+" подлечил "+whom.getName());
        return new Action(name+" подлечил "+whom.getName(),
                new Quality("Подлечили "+whom.getName()));
    }

    Action toLookAt(Thing thing){
        System.out.println(name+" взглянул(а) на "+thing.getName());
        return new Action(name+" взглянул(а) на "+thing.getName(),
                new Quality("Взглянули на "+thing.getName()));
    }

    Action toThrowsToWith(Alive whom, Thing thing){
        System.out.println(name+" бросился(лась) к "+whom.getName()+" с "+thing.getName());
        return new Action(name+" бросился(лась) к "+whom.getName()+" с "+thing.getName(),
                new Quality("Идет помощь для "+whom.getName()));
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
