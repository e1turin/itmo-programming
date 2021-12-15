package com.company;

import abstracts.Closable;
import abstracts.Thing;
import com.company.things.*;
import exc.IncorrectFileNameException;
import exc.NogivenNameException;
import types.Color;
import types.Feeling;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IncorrectFileNameException {
        String theFucsiaName;

        try{
            theFucsiaName = Inputter.inoutFileName("C:/names.txt");
        }catch (IncorrectFileNameException e){
            System.out.println("Не верное имя файла, файл не обнаружен");
        }

        Shorty theShortyes;
        try {
            theShortyes = new Shorty(Inputter.inputName(args));
        } catch (NogivenNameException e) {
            System.out.println("*Имя коротышек установлено по-умолчанию*");
            theShortyes = new Shorty("Коротышки");
        }

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
        Policeman theRigle = new Policeman("Ригль");
        Person theDocPilulckin = new Person("Доктор Пилюлькин");
        Healpack healpack = new Healpack(new Sound(""), new Quality(""));
        Bullets bullet = new Bullets(new Sound("свист пули"), new Quality(""));
        Policeman thePolicemen = new Policeman("Полицейские");


        theSmarty.toSee(theRigle.toShout("Полицейские, ружья на изготовку"));
        theSmarty.toCommandTo(theShortyes, new Command("забираться в ракету"));

        //
        //
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
        theSmarty.toClose(new Closable() {
            @Override
            public void close() {
                door.close();
                System.out.println("Закрыли дверь");
            }
        }); //door);
        //
        //


        theDocPilulckin.toSee(act);
        theDocPilulckin.toThrowsToWith(theKlepka, healpack);
        theDocPilulckin.toSee(new Action("Пуля прошла навылет",
                new Quality("Не смертельно")));
        theDocPilulckin.toHeal(theKlepka);
        theKlepka.toFeel(Feeling.PAIN);
        theSmarty.toHear(new Action(bullets.getName() + " барабанят по оболочке " + rocket.getName(), new Quality("Опасный звук")));
        thePolicemen.toShoot();


    }

}

class Inputter {
    static String inputName(String[] args) throws NogivenNameException {
        try {
            return args[0];
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new NogivenNameException("не передано имя", "введите имя в " +
                    "качестве аргумента командной строки", e);
        }
    }

    static boolean isCorrectFileName(String path) {
        return path.equals("C:/meme.txt");
    }

    static String inoutFileName(String path) throws IncorrectFileNameException {
        try (Scanner file = new Scanner(new File(path))) {
            if (file.hasNextLine())
                return file.nextLine();
        } catch (FileNotFoundException e) {
            if (!isCorrectFileName("C:/names.txt")) {
                throw new IncorrectFileNameException("Incorrect filename : " + "C" +
                        ":/names.txt");
            }
            //...
        }
        return path;
    }

}
