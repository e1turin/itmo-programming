package com.company.things;

import abstracts.Closable;
import abstracts.Thing;
import com.company.Quality;
import com.company.Sound;
import types.Color;

public class Door extends Thing implements Closable {

    public Door(Sound sound, Quality quality) {
        super("Дверь", sound, quality, Color.UNDEFINED);
    }

    @Override
    public void close() {
        class Knock extends Sound {
            public Knock(){
                super("Стук");
            }
        }

        Knock knock = new Knock();

        System.out.print("Дверь " + color + "цвета закрылась. ");
        knock.sound();
    }
}
