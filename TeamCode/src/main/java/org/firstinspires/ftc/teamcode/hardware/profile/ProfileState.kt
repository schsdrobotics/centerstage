package org.firstinspires.ftc.teamcode.hardware.profile;

data class ProfileState(val x: Double, val v: Double, val a: Double)

enum class ProfileStateComponent {
    Position,
    Velocity,
    Acceleration
}