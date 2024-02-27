package org.firstinspires.ftc.teamcode.autonomous.framework

import com.acmerobotics.roadrunner.Action
import com.acmerobotics.roadrunner.Pose2d
import com.acmerobotics.roadrunner.Rotation2d
import com.acmerobotics.roadrunner.TrajectoryActionBuilder
import com.acmerobotics.roadrunner.Vector2d
import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive
import org.firstinspires.ftc.teamcode.util.extensions.deg

abstract class Auto(val drive: MecanumDrive, val color: Alliance) {
    abstract val start: Pose2d

    abstract val left: AutoActions
    abstract val middle: AutoActions
    abstract val right: AutoActions

    abstract fun getPark(): (TrajectoryActionBuilder) -> Action

    fun signed(y: Double) = when (color) {
        Alliance.Red -> y
        Alliance.Blue -> -y
    }

    fun point(x: Double, y: Double): Vector2d = when (color) {
        Alliance.Red -> Vector2d(x, y)
        Alliance.Blue -> Vector2d(x, -y)
    }

    fun pose(x: Double, y: Double, heading: Double): Pose2d = when (color) {
        Alliance.Red -> Pose2d(x, y, heading)
        Alliance.Blue -> Pose2d(x, -y, Rotation2d.fromDouble(heading).inverse().log())
    }

    val Int.invertibleDeg
        get() = when (color) {
            Alliance.Red -> this.deg
            Alliance.Blue -> -this.deg
        }

    companion object {
        const val HEIGHT = 17.25
        const val WIDTH = 11.025

        const val MAJOR_APOTHEM = HEIGHT / 2.0
        const val MINOR_APOTHEM = WIDTH / 2.0

        const val MAX_WHEEL_VEL = 50.0
        const val PROFILE_DECEL = -15.0
        const val PROFILE_ACCEL = 40.0

        const val MAX_ANGULAR_VEL = Math.PI / 2.0

        const val MAX_ANGULAR_ACCEL = Math.PI / 2.0
    }
}