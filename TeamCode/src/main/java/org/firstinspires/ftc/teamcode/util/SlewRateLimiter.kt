package org.firstinspires.ftc.teamcode.util

import com.acmerobotics.roadrunner.clamp
import com.acmerobotics.roadrunner.now

/**
 * @param growth - limit of rate of growth; > 0
 * @param decay - limit of rate of decay; < 0
 * @param initial - first input; 0 by default
 */
class SlewRateLimiter(val growth: Double, val decay: Double = -growth, initial: Double = 0.0) {
    var previous = initial
    var past = now()

    fun calculate(input: Double): Double {
        val now = now()
        val elapsed = now - past

        previous += clamp(input - previous, decay * elapsed, growth * elapsed)
        past = now()

        return previous
    }

    fun reset(value: Double) {
        previous = value
        past = now()
    }
}