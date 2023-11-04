package org.firstinspires.ftc.teamcode.library

import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt

class MotionProfile(private val constraints: AccelVelPair) {
    fun calculate(velocity: Double, delta: Double, error: Double): AccelVelPair {
        var output: AccelVelPair

        if (constraints.velocity > abs(velocity)) {
            output = AccelVelPair(
                constraints.acceleration,
                velocity + constraints.acceleration * delta
            )
        } else {
            output = AccelVelPair(
                0.0,
                constraints.velocity
            )
        }

        val shouldDecelerate = error <= output.velocity.pow(2) / (2.0 * constraints.acceleration)

        if (shouldDecelerate) {
            output = AccelVelPair(
                -constraints.acceleration,
                velocity - constraints.acceleration * delta
            )
        }

        return output
    }

    data class AccelVelPair(val acceleration: Double, val velocity: Double)
}