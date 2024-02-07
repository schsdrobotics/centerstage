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
    override val start = Pose2d(12.0, -63.0, 270.deg)

    override val left = run {
        val purple = drive.actionBuilder(start, color)
                .setReversed(true)
                .splineTo(Vector2d(8.0, -42.0), 135.deg)
                .endTrajectory()

        NullAutoActions
    }

    override val middle = run {
        val purple = drive.actionBuilder(start, color)
                .setReversed(true)
                .splineTo(Vector2d(12.0, -(25.0 + HEIGHT / 2.0) - 1.5), 90.deg) // purple pixel
                .endTrajectory()

        NullAutoActions
    }

    override val right = run {
        val purple = drive.actionBuilder(start, color)
                .setReversed(true)
                .splineTo(Vector2d(19.0, -43.0), 60.deg)
                .endTrajectory()

        NullAutoActions
    }

    override val park = {
        builder: TrajectoryActionBuilder -> builder
            .splineToConstantHeading(Vector2d(60.0, -60.0), 0.0)
            .build()
    }
}