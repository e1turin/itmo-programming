package com.company.things;

import abstracts.Thing;
import com.company.Quality;
import com.company.Sound;
import types.Color;

public class Bloodstain extends Thing {
    public Bloodstain(Sound sound, Quality quality) {
        super("Кровяное пятно", sound, quality, Color.RED);
    }

}
