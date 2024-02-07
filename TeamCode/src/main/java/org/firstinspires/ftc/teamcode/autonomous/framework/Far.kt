package org.firstinspires.ftc.teamcode.autonomous.framework

import com.acmerobotics.roadrunner.Pose2d
import com.acmerobotics.roadrunner.TrajectoryActionBuilder
import com.acmerobotics.roadrunner.Vector2d
import org.firstinspires.ftc.teamcode.autonomous.AutonomousSide
import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive
import org.firstinspires.ftc.teamcode.util.extensions.deg

class Far(drive: MecanumDrive, color: AutonomousSide) : Auto(drive, color) {
    override val start = Pose2d(-36.0, -63.0, 270.deg)

    override val left = run {
        val purple = drive.actionBuilder(start)
                .setReversed(true)
                .splineTo(Vector2d(-40.0, -42.5), 135.deg)
                .endTrajectory()

        NullAutoActions
    }

    override val middle = run {
        val purple = drive.actionBuilder(start, color)
                .setReversed(true)
                .lineToY(-(25.0 + HEIGHT / 2.0) + 1.0)
                .endTrajectory()


        NullAutoActions
    }

    override val right = run {
        val purple = drive.actionBuilder(start, color)
                .setReversed(true)
                .splineTo(Vector2d(-30.0, -40.0), 45.deg)
                .endTrajectory()

        NullAutoActions
    }


    override val park = {
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