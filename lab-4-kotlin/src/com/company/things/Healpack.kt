package com.company.things

import abstracts.Thing
import com.company.Quality
import com.company.Sound
import types.Color

class Healpack(sound: Sound, quality: Quality) :
    Thing("Походная аптечка", sound, quality, Color.UNDEFINED)