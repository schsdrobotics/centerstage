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

        val yellow = drive.actionBuilder(Pose2d(-40.0, -41.0, 315.deg))
                .setReversed(true)
                .setTangent((180 - 65).deg)
                .lineToXLinearHeading(-36.0, 180.deg)
                .setTangent(270.deg)
                .lineToY(-10.0)
                .setTangent(0.deg)
                .splineToSplineHeading(Pose2d(-36.0, -10.0, 180.deg), 0.deg)
                .splineToConstantHeading(Vector2d(35.5, -10.0), 0.deg)
                .splineToConstantHeading(Vector2d(49.5, -37.0), 0.deg)
                .endTrajectory()

        AutoActions(purple.build(), yellow.build())
    }

    override val middle = run {
        val purple = builder
                .setReversed(true)
                .lineToY(-(25.0 + HEIGHT / 2.0) + 2.0)
                .lineToY(-(25.0 + HEIGHT / 2.0) - 1.5)
                .endTrajectory()

        val yellow = drive.actionBuilder(Pose2d(-36.0, -(25.0 + HEIGHT / 2.0) - 1.5, 270.deg))
                .setReversed(true)
                .setTangent(180.deg)
                .splineToSplineHeading(Pose2d(-51.0, -10.0, 180.deg), 0.deg)
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