package com.company.things

import abstracts.Thing
import com.company.Quality
import com.company.Sound
import types.Color

class Shots(sound: Sound, quality: Quality) :
    Thing("Выстрелы", sound, quality, Color.UNDEFINED)