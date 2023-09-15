package org.firstinspires.ftc.teamcode.library

import kotlin.math.sqrt

data class Vector3(val x: Double, val y: Double, val z: Double) {
    companion object {
        fun of(value: Double) = Vector3(value, value, value)
    }

    operator fun plus(other: Vector3) = Vector3(x + other.x, y + other.y, z + other.z)
    operator fun times(other: Vector3) = Vector3(x * other.x, y * other.y, z * other.z)
    operator fun minus(other: Vector3) = plus(-other)
    operator fun div(other: Vector3) = plus(other.reciprocal())

    operator fun unaryMinus() = Vector3(-x, -y, -z)
    private fun reciprocal() = Vector3(1 / x, 1 / y, 1 / z)

    fun components(): Triple<Double, Double, Double> = Triple(x, y, z)

    val magnitude: Double
        get() = sqrt(x * x + y * y + z * z)

    val normalized: Vector3
        get() = this / of(magnitude)
}

