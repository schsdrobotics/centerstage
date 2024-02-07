package org.firstinspires.ftc.teamcode.autonomous.framework

import com.acmerobotics.roadrunner.Action
import com.acmerobotics.roadrunner.Pose2d
import com.acmerobotics.roadrunner.Rotation2d
import com.acmerobotics.roadrunner.TrajectoryActionBuilder
import com.acmerobotics.roadrunner.Vector2d
import org.firstinspires.ftc.teamcode.autonomous.AutonomousSide
import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive

abstract class Auto(val drive: MecanumDrive, val color: AutonomousSide) {
    abstract val start: Pose2d

    abstract val left: AutoActions
    abstract val right: AutoActions
    abstract val middle: AutoActions

    abstract val park: (TrajectoryActionBuilder) -> Action

    fun point(x: Double, y: Double): Vector2d = when (color) {
        AutonomousSide.Red -> Vector2d(x, y)
        AutonomousSide.Blue -> Vector2d(x, -y)
    }
    fun pose(x: Double, y: Double, heading: Double): Pose2d = when (color) {
        AutonomousSide.Red -> Pose2d(x, y, heading)
        AutonomousSide.Blue -> Pose2d(x, -y, Rotation2d.fromDouble(heading).inverse().toDouble())
    }

    companion object {
        const val HEIGHT = 16.0
        const val WIDTH = 11.0
    }
}