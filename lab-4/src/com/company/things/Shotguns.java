package com.company.things;

import abstracts.Thing;
import com.company.Quality;
import com.company.Sound;
import types.Color;

public class Shotguns extends Thing {
    public Shotguns(Sound sound, Quality quality) {
        super("Ружья", sound, quality, Color.UNDEFINED);
    }

}
