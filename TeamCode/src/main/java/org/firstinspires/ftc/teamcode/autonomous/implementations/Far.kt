package org.firstinspires.ftc.teamcode.autonomous.implementations

import com.acmerobotics.roadrunner.NullAction
import com.acmerobotics.roadrunner.Pose2d
import org.firstinspires.ftc.teamcode.autonomous.framework.Auto
import org.firstinspires.ftc.teamcode.autonomous.framework.AutoActions
import org.firstinspires.ftc.teamcode.autonomous.framework.AutonomousSide
import org.firstinspires.ftc.teamcode.autonomous.framework.Cycle
import org.firstinspires.ftc.teamcode.autonomous.framework.Cycles
import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive
import org.firstinspires.ftc.teamcode.util.extensions.deg

class Far(drive: MecanumDrive, color: AutonomousSide) : Auto(drive, color) {
    override val start = pose(-36.0, -64.0, Math.toRadians(89.995))

    private fun cycleGenerator(begin: Pose2d, first: Boolean = true): Pair<Cycle, Pose2d> {
        val stackX = -(69.0 - HEIGHT + 4.5)
        val stackTangent = if (first) 115.deg else 170.deg

        val stackPose = pose(stackX, -11.5, 180.deg)

        val stack = drive.actionBuilder(begin)
            .setTangent(stackTangent)
            .splineToSplineHeading(stackPose, 170.deg)
            .endTrajectory()

        val intermediate = pose(47.0, -16.25, 150.deg)

        val backstageTangent = (0).deg

        val backstage = drive.actionBuilder(stackPose)
            .setTangent(backstageTangent)
            .splineToSplineHeading(intermediate, 0.deg)

        return Pair(Cycle(backstage = backstage.build(), stacks = stack.build()), intermediate)
    }

    override val left = run {
        val purplePose = pose(-33.0, -31.0, 180.deg)

        val purple = drive.actionBuilder(start)
            .splineToSplineHeading(purplePose, 90.deg)
            .endTrajectory()

        val (initial, begin) = cycleGenerator(purplePose)
        val (rest, end) = cycleGenerator(begin, first = false)

        AutoActions(purple.build(), NullAction(), Cycles(initial, rest), park(end))
    }

    override val middle = run {
        val pixelY = -(25.0 + APOTHEM) - 1.5
        val purplePose = pose(-36.0, pixelY, 90.deg)

        val purple = drive.actionBuilder(start)
            .splineTo(purplePose.position, purplePose.heading.log())
            .endTrajectory()

        val (initial, begin) = cycleGenerator(purplePose)
        val (rest, end) = cycleGenerator(begin, first = false)

        AutoActions(purple.build(), NullAction(), Cycles(initial, rest), park(end))
    }

    override val right = run {
        val purplePose = pose(-32.0, -40.0, 50.deg)

        val purple = drive.actionBuilder(start)
            .splineTo(purplePose.position, purplePose.heading.log())
            .endTrajectory()

        val (initial, begin) = cycleGenerator(purplePose)
        val (rest, end) = cycleGenerator(begin, first = false)

        AutoActions(purple.build(), NullAction(), Cycles(initial, rest), park(end))
    }

    override fun park(target: Pose2d) =
        drive
            .actionBuilder(target)
            .setTangent(0.deg)
            .splineToSplineHeading(pose(60.0, -10.0, 90.deg), 0.deg)
            .build()
}