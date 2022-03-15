package com.company.things

import abstracts.Thing
import com.company.Quality
import com.company.Sound
import types.Color

class Bullets(sound: Sound, quality: Quality) :
    Thing("Пули", sound, quality, Color.UNDEFINED)