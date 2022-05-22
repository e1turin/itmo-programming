package com.github.e1turin.exceptions

class NonexistentCommandException(val cmd: String) : Exception("Attempt to execute nonexistent command")

