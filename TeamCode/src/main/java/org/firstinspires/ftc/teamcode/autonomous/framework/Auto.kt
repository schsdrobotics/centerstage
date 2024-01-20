package org.firstinspires.ftc.teamcode.autonomous.framework

import com.acmerobotics.roadrunner.Action
import com.acmerobotics.roadrunner.TrajectoryActionBuilder

abstract class Auto(val builder: TrajectoryActionBuilder) {
    abstract val left: Action
    abstract val middle: Action
    abstract val right: Action

    companion object {
        const val HEIGHT = 16.0
        const val WIDTH = 11.0
    }
}