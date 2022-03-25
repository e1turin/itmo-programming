package com.github.e1turin.lab5.exceptions

class NonexistentCommandException(val cmd: String) : Exception("Attempt to execute nonexistent command")

