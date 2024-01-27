package org.firstinspires.ftc.teamcode.autonomous.framework

import com.acmerobotics.roadrunner.Action
import com.acmerobotics.roadrunner.Pose2d
import com.acmerobotics.roadrunner.TrajectoryActionBuilder
import com.acmerobotics.roadrunner.Vector2d
import org.firstinspires.ftc.teamcode.util.extensions.deg

class Close(
        builder: TrajectoryActionBuilder,
        whenPurple: Action,
        whenYellow: Action
) : Auto(builder, whenPurple, whenYellow) {

    override val left = run {
        val purple = builder
                .setReversed(true)
                .splineTo(Vector2d(8.0, -41.0), 135.deg)
                .stopAndAdd(whenPurple)
        val yellow = purple
                .splineToLinearHeading(Pose2d(49.5, -31.5, 180.deg), 0.deg)
                .stopAndAdd(whenYellow)
                .build()

        yellow
    }

    override val middle = run {
        val purple = builder
                .setReversed(true)
                .splineTo(Vector2d(12.0, -(25.0 + HEIGHT / 2.0) - 1.5), 90.deg) // purple pixel
                .stopAndAdd(whenPurple)
        val yellow = purple
                .splineToLinearHeading(Pose2d(49.5, -37.0, 180.deg), 0.deg)
                .stopAndAdd(whenYellow)
                .build()

        yellow
    }

    override val right = run {
        val purple = builder
                .setReversed(true)
                .splineTo(Vector2d(22.5, -44.0), 90.deg)
                .stopAndAdd(whenPurple)
        val yellow = purple
                .splineToLinearHeading(Pose2d(49.5, -44.0, 180.deg), 0.deg)
                .stopAndAdd(whenYellow)
                .build()

        yellow
    }

    companion object {
        val start = Pose2d(12.0, -63.0, 270.deg)
    }
}