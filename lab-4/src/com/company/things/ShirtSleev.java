package com.company.things;

import abstracts.Thing;
import com.company.Quality;
import com.company.Sound;
import types.Color;

public class ShirtSleev extends Thing {
    public ShirtSleev(Sound sound, Quality quality) {
        super("Рукав", sound, quality, Color.WHITE);
    }
}
