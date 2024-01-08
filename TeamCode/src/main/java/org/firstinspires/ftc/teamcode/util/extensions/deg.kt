package org.firstinspires.ftc.teamcode.util.extensions

val Int.deg: Double
    get() = Math.toRadians(this.toDouble())