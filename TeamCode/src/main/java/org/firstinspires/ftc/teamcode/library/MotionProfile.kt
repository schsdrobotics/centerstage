package org.firstinspires.ftc.teamcode.library

import kotlin.math.exp
import kotlin.math.sqrt

fun profile(at: Int, goal: Int) = profile(at.toDouble(), goal.toDouble())

fun profile(at: Double, goal: Double): Double {
    val split = 1.0 / 2.35

    val climb = split * goal
    val drop = goal - climb
    val midpoint = goal / 2.0

    val flight = 2.0
    val descent = 1.8

    fun sigmoid(x: Double) = 1.0 / (1.0 + exp(-x))

    fun template(coeff: Double, gain: Double, const: Double = 0.0, mult: Double = 1.0)
            = { x: Double -> sigmoid(mult * (sqrt(coeff) / goal) * (gain * x - climb - const)) }

    fun rise(x: Double) = template(climb, flight)(x)

    fun fall(x: Double) = template(drop, descent, goal, -1.0)(x)

    fun peak(x: Double): Double {
        val slope = (fall(drop) - rise(climb)) / (drop - climb)
        val intercept = rise(climb)

        return slope * (x - climb) + intercept
    }

    val computed = when {
        at < climb -> rise(at)
        at in climb..drop -> peak(at)
        at > drop -> fall(at)

        else -> 0.0
    }

    val correction = 1.0 / peak(midpoint)

    return computed * correction
}