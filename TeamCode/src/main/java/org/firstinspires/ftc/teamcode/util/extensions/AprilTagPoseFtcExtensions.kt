package org.firstinspires.ftc.teamcode.util.extensions

import org.firstinspires.ftc.vision.apriltag.AprilTagPoseFtc

operator fun AprilTagPoseFtc.plus(other: AprilTagPoseFtc) = AprilTagPoseFtc(
        x + other.x,
        y + other.y,
        z + other.z,
        yaw + other.yaw,
        pitch + other.pitch,
        roll + other.roll,
        range + other.range,
        bearing + other.bearing,
        elevation + other.elevation
)

operator fun AprilTagPoseFtc.minus(other: AprilTagPoseFtc) = this + -other

operator fun AprilTagPoseFtc.times(other: AprilTagPoseFtc) = AprilTagPoseFtc(
        x * other.x,
        y * other.y,
        z * other.z,
        yaw * other.yaw,
        pitch * other.pitch,
        roll * other.roll,
        range * other.range,
        bearing * other.bearing,
        elevation * other.elevation
)

operator fun AprilTagPoseFtc.div(other: AprilTagPoseFtc) = this * other.reciprocal()

operator fun AprilTagPoseFtc.unaryMinus() = AprilTagPoseFtc(
        -x,
        -y,
        -z,
        -yaw,
        -pitch,
        -roll,
        -range,
        -bearing,
        -elevation,
)

fun AprilTagPoseFtc.reciprocal() = AprilTagPoseFtc(
        1.0 / x,
        1.0 / y,
        1.0 / z,
        1.0 / yaw,
        1.0 / pitch,
        1.0 / roll,
        1.0 / range,
        1.0 / bearing,
        1.0 / elevation,
)