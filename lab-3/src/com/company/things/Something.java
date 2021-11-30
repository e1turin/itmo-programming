package com.company.things;

import abstracts.Alive;
import abstracts.Thing;
import com.company.Action;
import com.company.Quality;
import com.company.Sound;
import types.Color;

public class Something extends Thing {
    public Something(Sound sound, Quality quality) {
        super("Что-то", sound, quality, Color.UNDEFINED);
    }

    public Action toBurn(Alive whom, String what) {
        System.out.println();
        return new Action(name + " обожгло " + whom.getName() + " а именно: " + what,
                new Quality("Больно"));
    }

}
