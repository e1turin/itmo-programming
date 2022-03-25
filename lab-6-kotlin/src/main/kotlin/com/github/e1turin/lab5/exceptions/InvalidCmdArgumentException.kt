package com.github.e1turin.lab5.exceptions

class InvalidCmdArgumentException(val arg: String) : Exception(
    "Command was executed with invalid argument")
