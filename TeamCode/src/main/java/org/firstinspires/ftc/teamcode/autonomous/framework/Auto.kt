package org.firstinspires.ftc.teamcode.autonomous.framework

import com.acmerobotics.roadrunner.TrajectoryActionBuilder

abstract class Auto(val builder: TrajectoryActionBuilder) {
    abstract val left: AutoActions
    abstract val middle: AutoActions
    abstract val right: AutoActions

    companion object {
        const val HEIGHT = 16.0
        const val WIDTH = 11.0
    }
}