package com.github.e1turin.exceptions

import java.io.IOException

class InvalidCmdArgumentException(message: String) : IOException(
    "Command was executed with invalid argument: $message")
