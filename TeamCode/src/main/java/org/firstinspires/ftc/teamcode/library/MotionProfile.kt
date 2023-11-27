package org.firstinspires.ftc.teamcode.library

import kotlin.math.exp
import kotlin.math.sqrt

fun profile(at: Int, goal: Int) = profile(at.toDouble(), goal.toDouble())

fun profile(at: Double, goal: Double): Double {
    val split = 1.0 / 2.0

    val climb = split * goal
    val drop = goal - climb
    val midpoint = goal / 2.0

    fun rise(x: Double) = (x - climb) / climb + 1

    fun fall(x: Double) = -(x - drop) / climb + 1

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