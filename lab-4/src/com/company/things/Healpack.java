package com.company.things;

import abstracts.Thing;
import com.company.Quality;
import com.company.Sound;
import types.Color;

public class Healpack extends Thing {
    public Healpack(Sound sound, Quality quality) {
        super("Походная аптечка", sound, quality, Color.UNDEFINED);
    }

}
