package com.company.things

import abstracts.Thing
import com.company.Quality
import com.company.Sound
import types.Color

class Bloodstain(sound: Sound, quality: Quality) :
    Thing("Кровяное пятно", sound, quality, Color.RED)