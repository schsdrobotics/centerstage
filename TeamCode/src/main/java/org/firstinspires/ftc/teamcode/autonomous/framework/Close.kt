package org.firstinspires.ftc.teamcode.autonomous.framework

import com.acmerobotics.roadrunner.Pose2d
import com.acmerobotics.roadrunner.Rotation2d
import com.acmerobotics.roadrunner.TrajectoryActionBuilder
import com.acmerobotics.roadrunner.Vector2d
import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive
import org.firstinspires.ftc.teamcode.util.extensions.deg
import kotlin.math.tan

class Close(drive: MecanumDrive, builder: TrajectoryActionBuilder) : Auto(drive, builder) {
    override val left = run {
        val purple = builder
                .setReversed(true)
                .splineTo(Vector2d(8.0, -42.0), 135.deg)
                .endTrajectory()

        val yellow = drive.actionBuilder(Pose2d(Vector2d(8.0, -41.0), 315.deg))
                .setReversed(true)
                .setTangent(tan(11.5 / 41.5))
                .lineToXLinearHeading(49.5, Rotation2d.fromDouble(180.deg))
                .endTrajectory()

        AutoActions(purple.build(), yellow.build())
    }

    override val middle = run {
        val purple = builder
                .setReversed(true)
                .splineTo(Vector2d(12.0, -(25.0 + HEIGHT / 2.0) - 1.5), 90.deg) // purple pixel
                .endTrajectory()

        val yellow = drive.actionBuilder(Pose2d(Vector2d(12.0, -(25.0 + HEIGHT / 2.0) - 1.5), 270.deg))
                .setReversed(true)
                .splineToLinearHeading(Pose2d(49.5, -37.0, 180.deg), 0.deg)
                .endTrajectory()

        AutoActions(purple.build(), yellow.build())
    }

    override val right = run {
        val purple = builder
                .setReversed(true)
                .splineTo(Vector2d(22.5, -44.0), 90.deg)
                .endTrajectory()

        val yellow = drive.actionBuilder(Pose2d(Vector2d(22.5, -44.0), 270.deg))
                .setReversed(true)
                .splineToLinearHeading(Pose2d(49.5, -44.0, 180.deg), 0.deg)
                .endTrajectory()

        AutoActions(purple.build(), yellow.build())
    }

    companion object {
        val start = Pose2d(12.0, -63.0, 270.deg)
    }
}