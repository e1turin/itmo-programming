package com.company.things;

import abstracts.Thing;
import com.company.Quality;
import com.company.Sound;
import types.Color;

public class Bullets extends Thing {
    public Bullets(Sound sound, Quality quality) {
        super("Пули", sound, quality, Color.UNDEFINED);
    }

}
