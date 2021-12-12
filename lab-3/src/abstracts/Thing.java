package abstracts;

import com.company.Action;
import com.company.Quality;
import com.company.Sound;
import types.Color;

import java.util.Objects;

public abstract class Thing implements Paintable {

    protected String name;
    protected Sound sound;
    protected Quality quality;
    protected Color color;


    public Thing(String name, Sound sound, Quality quality, Color color) {
        this.name = name;
        this.sound = sound;
        this.quality = quality;
        this.color = color;
    }

    public void paint(Color color) {
        System.out.println(name + " теперь цвета " + color);
        this.color = color;
    }

    public void toSound() {
        System.out.print(name + ": ");
        sound.sound();
    }

    public Action toAppearOn(Thing thing) {
        return new Action(name + " цвета " + color + " появилось на " + thing.getName() +
                " " + "цвета " + thing.getColor(),
                new Quality("Появление"));
    }

    Action toAct(String name) {
        return new Action(name, quality);
    }

    public String getName() {
        return name;
    }

    public Sound getSound() {
        return sound;
    }

    public Quality getQuality() {
        return quality;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Thing thing = (Thing) o;
        return name.equals(thing.name) && sound.equals(thing.sound) && quality.equals(thing.quality) && color == thing.color;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, sound, quality, color);
    }

}
