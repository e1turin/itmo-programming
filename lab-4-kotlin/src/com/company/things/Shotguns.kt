package com.company.things

import abstracts.Thing
import com.company.Quality
import com.company.Sound
import types.Color

class Shotguns(sound: Sound, quality: Quality) :
    Thing("Ружья", sound, quality, Color.UNDEFINED)