package org.firstinspires.ftc.teamcode.autonomous.implementations

import com.acmerobotics.roadrunner.Pose2d
import com.acmerobotics.roadrunner.TrajectoryActionBuilder
import com.acmerobotics.roadrunner.Vector2d
import org.firstinspires.ftc.teamcode.autonomous.framework.Auto
import org.firstinspires.ftc.teamcode.autonomous.framework.AutonomousSide
import org.firstinspires.ftc.teamcode.autonomous.framework.NoAuto
import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive
import org.firstinspires.ftc.teamcode.util.extensions.deg

class Close(drive: MecanumDrive, color: AutonomousSide) : Auto(drive, color) {
    override val start = Companion.start

    override val left = run {
        val purple = drive.actionBuilder(start)
                .setReversed(true)
                .splineTo(point(8.0, -42.0), 135.deg)
                .endTrajectory()

        NoAuto
    }

    override val middle = run {
        val purple = drive.actionBuilder(start)
                .setReversed(true)
                .splineTo(Vector2d(12.0, -(25.0 + APOTHEM) - 1.5), 90.deg) // purple pixel
                .endTrajectory()

        NoAuto
    }

    override val right = run {
        val purple = drive.actionBuilder(start)
                .setReversed(true)
                .splineTo(Vector2d(19.0, -43.0), 60.deg)
                .endTrajectory()

        NoAuto
    }

    override val park = {
        builder: TrajectoryActionBuilder -> builder
            .splineToConstantHeading(Vector2d(60.0, -60.0), 0.0)
            .build()
    }

    companion object {
        val start = Pose2d(12.0, -64.0, 270.deg)
    }
}