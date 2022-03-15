package com.company.things

import abstracts.Thing
import com.company.Quality
import com.company.Sound
import types.Color

class Rocket(sound: Sound, quality: Quality) :
    Thing("Рокета", sound, quality, Color.UNDEFINED)