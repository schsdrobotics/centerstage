package org.firstinspires.ftc.teamcode.autonomous.implementations

import com.acmerobotics.roadrunner.Action
import com.acmerobotics.roadrunner.NullAction
import com.acmerobotics.roadrunner.Pose2d
import com.acmerobotics.roadrunner.TrajectoryActionBuilder
import com.acmerobotics.roadrunner.Vector2d
import org.firstinspires.ftc.teamcode.autonomous.framework.Auto
import org.firstinspires.ftc.teamcode.autonomous.framework.AutoActions
import org.firstinspires.ftc.teamcode.autonomous.framework.AutonomousSide
import org.firstinspires.ftc.teamcode.autonomous.framework.Cycle
import org.firstinspires.ftc.teamcode.autonomous.framework.Cycles
import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive
import org.firstinspires.ftc.teamcode.util.extensions.deg

class Close(drive: MecanumDrive, color: AutonomousSide) : Auto(drive, color) {
    override val start = Pose2d(12.0, -64.0, Math.toRadians(89.995))

    private fun cycleGenerator(begin: Pose2d, first: Boolean = true): Pair<Cycle, Pose2d> {
        val stackX = -69.0 + HEIGHT - 4.5

        val stack = if (!first) {
            drive.actionBuilder(begin)
                    .setTangent(0.deg)
                    .lineToXConstantHeading(stackX)
                    .endTrajectory()
        } else {
            drive.actionBuilder(begin)
                    .setTangent(110.deg)
                    .splineToLinearHeading(pose(stackX, -9.0, 180.deg), 180.deg)
                    .endTrajectory()
        }

        val backstage = drive.actionBuilder(pose(stackX, -9.0, 180.deg))
                .setTangent(0.deg)
                .lineToXConstantHeading(47.5)

        val end = pose(47.5, -9.0, 180.deg)

        return Pair(Cycle(stack.build(), backstage.build()), end)
    }

    private fun yellowGenerator(begin: Pose2d, y: Double): Pair<Action, Pose2d> {
        val end = pose(47.5, y, 180.deg)

        val yellow = drive.actionBuilder(begin)
                .setTangent(0.deg)
                .splineToLinearHeading(end, 0.deg)
                .endTrajectory()

        return Pair(yellow.build(), end)
    }

    override val left = run {
        val purple = drive.actionBuilder(start)
                .splineTo(point(8.0, -42.0), 135.deg)
                .endTrajectory()

        val (yellow, end) = yellowGenerator(pose(8.0, -42.0, 135.deg), -29.5)

        val (initial, begin) = cycleGenerator(end)
        val (rest, _) = cycleGenerator(begin, first = false)

        AutoActions(purple.build(), yellow, Cycles(initial, rest), NullAction())
    }

    override val middle = run {
        val pixelY = -(25.0 + APOTHEM) - 1.5

        val purple = drive.actionBuilder(start)
                .splineTo(Vector2d(12.0, pixelY), 90.deg) // purple pixel
                .endTrajectory()

        val (yellow, end) = yellowGenerator(pose(12.0, pixelY, 90.deg), -35.5)

        val (initial, begin) = cycleGenerator(end)
        val (rest, _) = cycleGenerator(begin, first = false)

        AutoActions(purple.build(), yellow, Cycles(initial, rest), NullAction())
    }

    override val right = run {
        val purple = drive.actionBuilder(start)
                .splineTo(Vector2d(19.0, -43.0), 60.deg)
                .endTrajectory()

        val (yellow, end) = yellowGenerator(pose(19.0, -43.0, 60.deg), -42.5)

        val (initial, begin) = cycleGenerator(end)
        val (rest, _) = cycleGenerator(begin, first = false)

        AutoActions(purple.build(), yellow, Cycles(initial, rest), NullAction())
    }

    override val park = {
        builder: TrajectoryActionBuilder -> builder
            .splineToConstantHeading(Vector2d(60.0, -60.0), 0.0)
            .build()
    }
}