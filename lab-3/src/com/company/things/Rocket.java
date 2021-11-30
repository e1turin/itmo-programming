package com.company.things;

import abstracts.Thing;
import com.company.Quality;
import com.company.Sound;
import types.Color;

public class Rocket extends Thing {
    public Rocket(Sound sound, Quality quality) {
        super("Рокета", sound, quality, Color.UNDEFINED);
    }

}
