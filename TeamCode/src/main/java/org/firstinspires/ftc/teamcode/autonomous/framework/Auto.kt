package org.firstinspires.ftc.teamcode.autonomous.framework

import com.acmerobotics.roadrunner.TrajectoryActionBuilder
import org.firstinspires.ftc.teamcode.autonomous.AutonomousSide
import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive

abstract class Auto(val drive: MecanumDrive, val color: AutonomousSide) {
    abstract val left: AutoActions
    abstract val middle: AutoActions
    abstract val right: AutoActions

    companion object {
        const val HEIGHT = 16.0
        const val WIDTH = 11.0
    }
}