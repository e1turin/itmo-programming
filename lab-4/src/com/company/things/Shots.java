package com.company.things;

import abstracts.Thing;
import com.company.Quality;
import com.company.Sound;
import types.Color;


public class Shots extends Thing {
    public Shots(Sound sound, Quality quality) {
        super("Выстрелы", sound, quality, Color.UNDEFINED);
    }
}
