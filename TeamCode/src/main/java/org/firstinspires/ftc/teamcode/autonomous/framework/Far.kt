package org.firstinspires.ftc.teamcode.autonomous.framework

import com.acmerobotics.roadrunner.Pose2d
import com.acmerobotics.roadrunner.TrajectoryActionBuilder
import com.acmerobotics.roadrunner.Vector2d
import org.firstinspires.ftc.teamcode.autonomous.AutonomousSide
import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive
import org.firstinspires.ftc.teamcode.util.extensions.deg

class Far(drive: MecanumDrive, color: AutonomousSide) : Auto(drive, color) {
    override val left = run {
        val purple = drive.actionBuilder(start)
                .setReversed(true)
                .splineTo(Vector2d(-40.0, -42.5), 135.deg)
                .endTrajectory()

        val cycle = drive.actionBuilder(Pose2d(-40.0, -42.5, (135 + 180).deg))
                .setReversed(true)
                .setTangent(0.deg)
                .lineToXConstantHeading(-36.0)
                .setTangent(45.deg)
                .splineToLinearHeading(Pose2d(-30.0, -11.5, 180.deg), 0.deg)
                .lineToXConstantHeading(-56.0)
                .build()

        val white = drive.actionBuilder(Pose2d(-55.0, -11.5, 180.deg))
                .setReversed(true)
                .setTangent(0.deg)
                .splineToConstantHeading(Vector2d(35.5, -10.0), 0.deg)
                .splineToConstantHeading(Vector2d(47.0, -44.0), 0.deg)
                .endTrajectory()

        val yellow = drive.actionBuilder(Pose2d(Vector2d(47.0, -44.0), 180.deg))
                .setReversed(true)
                .splineToConstantHeading(Vector2d(47.0, -31.0), 0.deg)
                .endTrajectory()

        AutoActions(purple.build(), yellow.build(), cycle, park(yellow.fresh()), white.build())
    }

    override val middle = run {
        val purple = drive.actionBuilder(start, color)
                .setReversed(true)
                .lineToY(-(25.0 + HEIGHT / 2.0) + 1.0)
                .endTrajectory()

        val cycle = drive.actionBuilder(Pose2d(-36.0, -(25.0 + HEIGHT / 2.0) + 1.0, 270.deg))
                .splineToLinearHeading(Pose2d(-56.0, -11.5, 180.deg), 90.deg)
                .build()

        val white = drive.actionBuilder(Pose2d(-55.0, -11.5, 180.deg))
                .setReversed(true)
                .setTangent(250.deg)
                .splineToLinearHeading(Pose2d(-51.0, -10.0, 180.deg), 0.deg)
                .splineToConstantHeading(Vector2d(35.5, -10.0), 0.deg)
                .splineToConstantHeading(Vector2d(47.0, -31.0), 0.deg)
                .endTrajectory()

        val yellow = drive.actionBuilder(Pose2d(Vector2d(47.0, -31.0), 180.deg))
                .setReversed(true)
                .splineToConstantHeading(Vector2d(47.0, -37.0), 0.deg)
                .endTrajectory()

        AutoActions(purple.build(), yellow.build(), cycle, park(yellow.fresh()), white.build())
    }

    override val right = run {
        val purple = drive.actionBuilder(start, color)
                .setReversed(true)
                .splineTo(Vector2d(-30.0, -40.0), 45.deg)
                .endTrajectory()

        val cycle = drive.actionBuilder(Pose2d(-30.0, -40.0, 225.deg))
                .splineToLinearHeading(Pose2d(-56.0, -11.5, 180.deg), 90.deg)
                .build()

        val white = drive.actionBuilder(Pose2d(-55.0, -11.5, 180.deg))
                .setReversed(true)
                .setTangent(0.deg)
                .splineToSplineHeading(Pose2d(-40.0, -10.0, 180.deg), 0.deg)
                .splineToConstantHeading(Vector2d(35.5, -10.0), 0.deg)
                .splineToConstantHeading(Vector2d(47.0, -31.0), 0.deg)
                .endTrajectory()

        val yellow = drive.actionBuilder(Pose2d(Vector2d(47.0, -31.0), 180.deg))
                .setReversed(true)
                .splineToConstantHeading(Vector2d(47.0, -44.0), 0.deg)
                .endTrajectory()

        AutoActions(purple.build(), yellow.build(), cycle, park(yellow.fresh()), white.build())
    }

    companion object {
        val start = Pose2d(-36.0, -63.0, 270.deg)
        val park = {
            builder: TrajectoryActionBuilder -> builder
                .setReversed(true)
                .setTangent(0.deg)
                .lineToXConstantHeading(47.0)
                .setTangent(90.deg)
                .lineToYConstantHeading(-12.5)
                .setTangent(0.deg)
                .lineToXConstantHeading(60.0)
                .build()
        }
    }
}