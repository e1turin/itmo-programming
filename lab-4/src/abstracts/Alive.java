package abstracts;

import com.company.Action;
import com.company.Command;
import com.company.Decision;
import com.company.Quality;
import types.Feeling;

import java.util.Objects;

public abstract class Alive {
    protected String name;
    protected Decision decision;

    public Alive(String name) {
        this.name = name;
        this.decision = new Decision("");
    }

    public Action toLet(Alive entity) {
        System.out.println(name + " пропустил(а) " + entity.getName());
        return new Action(name + " пропустил(а) кого-то по имени " + entity.getName(),
                new Quality(
                        "Пропустил(а)"));
    }

    public Action toClimbinto(Thing thing) {
        return new Action(name + "полез в" + thing.getName(), new Quality("Полез"));
    }

    public void toFeel(Feeling feeling) {
        System.out.println(name + " почувствовал(а) " + feeling);
    }

    public void toFeel(Action action) {
        System.out.println(name + " почувствовал(а) как " + action.getDescription());
    }

    public void toDecide(String decision) {
        this.decision = new Decision(decision);
        System.out.println(name + " решил(а) " + decision);
    }

    public String toSee(Action action) {
        System.out.println(name + " увидел(а) как " + action.getDescription());
        return name + "увидел(а)" + action.getDescription();
    }

    public Action toShout(String cmd){
        System.out.println(name+" скомандавал(а) "+cmd);
        return new Action(name+" скомандавал(а) "+cmd, new Quality(
                "Команда "+cmd));
    }

    public Action toTake(Thing thing){
        System.out.println(name+" взял(а) "+thing.getName());
        return new Action(name+" взял(а) "+thing.getName(),
                new Quality("Взяли "+thing.getName()));
    }

    public Action toHear(Action action){
        System.out.println(name+" услышал(а), как "+action.getDescription());
        return new Action(name+" услышал(а), как "+action.getDescription(),
                new Quality("Услышали: "+action.getDescription()));
    }

    public String getName() {
        return name;
    }

    public Decision getDecision() {
        return decision;
    }

    @Override
    public String toString() {
        return name;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Alive alive = (Alive) o;
        return name.equals(alive.name) && decision.equals(alive.decision);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, decision);
    }


}
