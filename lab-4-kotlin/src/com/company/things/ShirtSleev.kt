package com.company.things

import abstracts.Thing
import com.company.Quality
import com.company.Sound
import types.Color

class ShirtSleev(sound: Sound, quality: Quality) :
    Thing("Рукав", sound, quality, Color.WHITE)