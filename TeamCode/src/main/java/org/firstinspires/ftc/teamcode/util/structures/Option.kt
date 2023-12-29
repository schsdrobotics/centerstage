package org.firstinspires.ftc.teamcode.util.structures

sealed class Option<T>
class None<T> : Option<T>()
class Some<T>(val value: T) : Option<T>()