package org.firstinspires.ftc.teamcode.roadrunner

import com.acmerobotics.roadrunner.Pose2d
import com.acmerobotics.roadrunner.PoseVelocity2d
import com.acmerobotics.roadrunner.Vector2d
import com.arcrobotics.ftclib.command.CommandBase

class GoToPoint(val drive: MecanumDrive, val target: Vector2d) : CommandBase() {
    var shouldFinish = false

    override fun execute() {
        val error = target - drive.pose.position

        if (error.norm() < 0.1) shouldFinish = true

        val powers = PoseVelocity2d(
            Vector2d(
                (MecanumDrive.PARAMS.axialGain * MULTIPLIER) * error.x,
                (MecanumDrive.PARAMS.lateralGain * MULTIPLIER) * error.y,
            ),
            0.0,
        )

        drive.setDrivePowers(powers)
    }

    override fun isFinished() = shouldFinish

    companion object {
        const val MULTIPLIER = 0.5
    }
}