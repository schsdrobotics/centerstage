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

//    fun rise(x: Double) = template(climb, flight)(x)
    fun rise(x: Double) = (x - climb) / climb + 1

//    fun fall(x: Double) = template(drop, descent, goal, -1.0)(x)
    fun fall(x: Double) = (-(x - drop) / climb ) + 1

//    fun peak(x: Double): Double {
//        val slope = (fall(drop) - rise(climb)) / (drop - climb)
//        val intercept = rise(climb)
//
//        return slope * (x - climb) + intercept
//    }

    fun peak(x: Double) = 1.0

    val computed = when {
        at < climb -> rise(at) + 0.07
        at in climb..drop -> peak(at)
        at > drop -> fall(at)

        else -> 0.0
    }

    val correction = 1.0 / peak(midpoint)

    return computed * correction
}

fun main() {
    for (i in 1..1000) {
        println("${i}, ${profile(i, 1000)}")
    }
}