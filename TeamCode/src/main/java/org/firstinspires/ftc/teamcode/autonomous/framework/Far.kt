package org.firstinspires.ftc.teamcode.autonomous.framework

import com.acmerobotics.roadrunner.Pose2d
import com.acmerobotics.roadrunner.TrajectoryActionBuilder
import com.acmerobotics.roadrunner.Vector2d
import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive
import org.firstinspires.ftc.teamcode.util.extensions.deg

class Far(drive: MecanumDrive, builder: TrajectoryActionBuilder) : Auto(drive, builder) {
    override val left = run {
        val purple = builder
                .setReversed(true)
                .splineTo(Vector2d(-40.0, -42.5), 135.deg)
                .endTrajectory()

        val yellow = drive.actionBuilder(Pose2d(-40.0, -42.5, 315.deg))
                .setReversed(true)
                .setTangent(0.deg)
                .lineToXConstantHeading(-36.0)
                .setTangent(45.deg)
                .splineToLinearHeading(Pose2d(-30.0, -10.0, 180.deg), 0.deg)
                .splineToConstantHeading(Vector2d(35.5, -10.0), 0.deg)
                .splineToConstantHeading(Vector2d(49.5, -31.0), 0.deg)
                .endTrajectory()

        AutoActions(purple.build(), yellow.build())
    }

    override val middle = run {
        val purple = builder
                .setReversed(true)
                .lineToY(-(25.0 + HEIGHT / 2.0) + 1.0)
                .endTrajectory()

        val yellow = drive.actionBuilder(Pose2d(-36.0, -(25.0 + HEIGHT / 2.0) + 1.0, 270.deg))
                .setReversed(true)
                .setTangent(250.deg)
                .splineToLinearHeading(Pose2d(-51.0, -10.0, 180.deg), 0.deg)
                .splineToConstantHeading(Vector2d(35.5, -10.0), 0.deg)
                .splineToConstantHeading(Vector2d(49.5, -37.0), 0.deg)
                .endTrajectory()

        AutoActions(purple.build(), yellow.build())
    }

    override val right = run {
        val purple = builder
                .setReversed(true)
                .splineTo(Vector2d(-30.0, -40.0), 45.deg)
                .endTrajectory()

        val yellow = drive.actionBuilder(Pose2d(-30.0, -40.0, 225.deg))
                .setReversed(true)
                .setTangent(180.deg)
                .splineToSplineHeading(Pose2d(-40.0, -10.0, 180.deg), 0.deg)
                .splineToConstantHeading(Vector2d(35.5, -10.0), 0.deg)
                .splineToConstantHeading(Vector2d(49.5, -44.0), 0.deg)
                .endTrajectory()

        AutoActions(purple.build(), yellow.build())
    }

    companion object {
        val start = Pose2d(-36.0, -63.0, 270.deg)
    }
}