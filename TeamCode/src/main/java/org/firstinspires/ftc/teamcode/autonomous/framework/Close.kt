package org.firstinspires.ftc.teamcode.autonomous.framework

import com.acmerobotics.roadrunner.Pose2d
import com.acmerobotics.roadrunner.Rotation2d
import com.acmerobotics.roadrunner.TrajectoryActionBuilder
import com.acmerobotics.roadrunner.Vector2d
import org.firstinspires.ftc.teamcode.autonomous.AutonomousSide
import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive
import org.firstinspires.ftc.teamcode.util.extensions.deg
import kotlin.math.tan

class Close(drive: MecanumDrive, color: AutonomousSide) : Auto(drive, color) {
    override val left = run {
        val purple = drive.actionBuilder(start, color)
                .setReversed(true)
                .splineTo(Vector2d(8.0, -42.0), 135.deg)
                .endTrajectory()

        val yellow = drive.actionBuilder(Pose2d(Vector2d(8.0, -42.0), 315.deg), color)
                .setReversed(true)
                .setTangent(tan(11.5 / 41.5))
                .lineToXLinearHeading(47.0, Rotation2d.exp(180.deg))
                .endTrajectory()

        AutoActions(purple.build(), yellow.build(), park = park(yellow.fresh()))
    }

    override val middle = run {
        val purple = drive.actionBuilder(start, color)
                .setReversed(true)
                .splineTo(Vector2d(12.0, -(25.0 + HEIGHT / 2.0) - 1.5), 90.deg) // purple pixel
                .endTrajectory()

        val yellow = drive.actionBuilder(Pose2d(Vector2d(12.0, -(25.0 + HEIGHT / 2.0) - 1.5), 270.deg), color)
                .setReversed(true)
                .setTangent(0.deg)
                .splineToLinearHeading(Pose2d(47.0, -37.0, 180.deg), 0.deg)
                .endTrajectory()

        AutoActions(purple.build(), yellow.build(), park = park(yellow.fresh()))
    }

    override val right = run {
        val purple = drive.actionBuilder(start, color)
                .setReversed(true)
                .splineTo(Vector2d(19.0, -43.0), 60.deg)
                .endTrajectory()

        val yellow = drive.actionBuilder(Pose2d(Vector2d(19.0, -43.0), 240.deg), color)
                .setReversed(true)
                .setTangent((-135).deg)
                .lineToYConstantHeading(-47.0)
                .setTangent(0.deg)
                .splineToLinearHeading(Pose2d(47.0, -44.0, 180.deg), 0.deg)
                .endTrajectory()

        AutoActions(purple.build(), yellow.build(), park = park(yellow.fresh()))
    }

    companion object {
        val start = Pose2d(12.0, -63.0, 270.deg)
        val park = {
            builder: TrajectoryActionBuilder -> builder
                .splineToConstantHeading(Vector2d(60.0, -60.0), 0.0)
                .build()
        }
    }
}