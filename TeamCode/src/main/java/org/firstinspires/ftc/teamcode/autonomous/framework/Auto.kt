package org.firstinspires.ftc.teamcode.autonomous.framework

import com.acmerobotics.roadrunner.Action
import com.acmerobotics.roadrunner.Pose2d
import com.acmerobotics.roadrunner.Rotation2d
import com.acmerobotics.roadrunner.TrajectoryActionBuilder
import com.acmerobotics.roadrunner.Vector2d
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
        AutonomousSide.Blue -> Pose2d(x, -y, Rotation2d.exp(heading).inverse().log())
    }

    companion object {
        const val HEIGHT = 16.0
        const val WIDTH = 11.0

        const val APOTHEM = HEIGHT / 2.0

        const val MAX_WHEEL_VEL = 70.0
        const val PROFILE_DECEL = -35.0
        const val PROFILE_ACCEL = 70.0

        const val MAX_ANGULAR_VEL = Math.PI / 3.0

        const val MAX_ANGULAR_ACCEL = Math.PI / 3.0
    }
}