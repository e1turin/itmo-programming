package com.company;

import com.company.things.*;
import types.Feeling;

public class Main {

    public static void main(String[] args) {
        Shorty theShortyes = new Shorty("Коротышки");
        Person theFucsia = new Person("Фуксия");
        Person theSeledochks = new Person("Селедочка");
        Rocket rocket = new Rocket(new Sound(""), new Quality(""));
        Shots shots = new Shots(new Sound("Выстрелы"), new Quality("Страшные"));
        Bullets bullets = new Bullets(new Sound("Свист"), new Quality("Опасные " +
                "вокруг"));
        Person theKlepka = new Person("Клёпка");
        Something something = new Something(new Sound(""), new Quality(
                "Обжигающее"));
        Person theSmarty = new Person("Знайка");
        ShirtSleev shirtSleev = new ShirtSleev(new Sound(""), new Quality(""));
        Bloodstain bloodstain = new Bloodstain(new Sound(""), new Quality(""));
        Door door = new Door(new Sound("Закрываение"), new Quality(""));

        theSmarty.toDecide("сесть в ракету");

        theShortyes.toLet(theFucsia);
        theShortyes.toLet(theSeledochks);
        theShortyes.toClimbinto(rocket);

        shots.toSound();
        bullets.toSound();

        Action act = something.toBurn(theKlepka, "руку");
        theKlepka.toFeel(act);

        theSmarty.toSee(theKlepka.toReshapeFace(Feeling.PAIN));
        theSmarty.toSee(bloodstain.toAppearOn(shirtSleev));

        theSmarty.toDragIn(theKlepka);
        System.out.print("не теряя ни секунды ");
        theSmarty.toClose(door);











    }
}
