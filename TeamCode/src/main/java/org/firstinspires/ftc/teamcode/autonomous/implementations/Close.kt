package org.firstinspires.ftc.teamcode.autonomous.implementations

import com.acmerobotics.roadrunner.AccelConstraint
import com.acmerobotics.roadrunner.Action
import com.acmerobotics.roadrunner.Arclength
import com.acmerobotics.roadrunner.MinMax
import com.acmerobotics.roadrunner.Pose2d
import com.acmerobotics.roadrunner.Pose2dDual
import com.acmerobotics.roadrunner.VelConstraint
import org.firstinspires.ftc.teamcode.autonomous.framework.Auto
import org.firstinspires.ftc.teamcode.autonomous.framework.AutoActions
import org.firstinspires.ftc.teamcode.autonomous.framework.Alliance
import org.firstinspires.ftc.teamcode.autonomous.framework.Cycle
import org.firstinspires.ftc.teamcode.autonomous.framework.Cycles
import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive
import org.firstinspires.ftc.teamcode.util.extensions.deg


class Close(drive: MecanumDrive, color: Alliance) : Auto(drive, color) {
    override val start = pose(24.0 - MINOR_APOTHEM, -64.0, Math.toRadians(89.995))

    private fun cycleGenerator(begin: Pose2d, first: Boolean = true): Pair<Cycle, Pose2d> {
        val stackVelConstraint = VelConstraint { pose: Pose2dDual<Arclength>, _, _ ->
            if (pose.position.y.value() < -12.5) {
                50.0
            } else {
                MAX_WHEEL_VEL
            }
        }

        val stackAccelConstraint = AccelConstraint { pose: Pose2dDual<Arclength>, _, _ ->
            if (pose.position.y.value() < -12.5) {
                MinMax(-25.0, 30.0)
            } else {
                MinMax(PROFILE_DECEL, PROFILE_ACCEL)
            }
        }

        val stackX = -69.0 + HEIGHT - 5.8
        val stackTangent = if (first) 90.deg else 150.deg

        val stackPose = pose(stackX, -10.75, 180.deg)

        val stack = drive.actionBuilder(begin)
            .setTangent(stackTangent)
            .splineToLinearHeading(stackPose, 180.deg, stackVelConstraint, stackAccelConstraint)
            .endTrajectory()

        val intermediate = pose(44.5, -18.0, 150.deg)

        val backstageTangent = (0).deg

        val backstage = drive.actionBuilder(stackPose)
            .setTangent(backstageTangent)
            .splineToLinearHeading(intermediate, (-40).deg)

        return Pair(Cycle(backstage = backstage.build(), stacks = stack.build()), intermediate)
    }

    private fun yellowGenerator(begin: Pose2d, y: Double): Pair<Action, Pose2d> {
        val end = pose(47.25, y, 180.deg)

//        val builder = drive.customActionBuilder(
//            begin,
//            { _, _, _, -> 80.0 },
//            { _, _, _, -> MinMax(-30.0, 80.0) },
//            TurnConstraints(PI, -PI, PI)
//        )

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
        val (rest, last) = cycleGenerator(begin, first = false)

        AutoActions(purple.build(), yellow, Cycles(initial, rest), park(last))
    }

    override val middle = run {
        val pixelY = -(25.0 + MAJOR_APOTHEM) - 1.5

        val purple = drive.actionBuilder(start)
            .splineTo(point(12.0, pixelY), 90.deg) // purple pixel
            .endTrajectory()

        val (yellow, end) = yellowGenerator(pose(12.0, pixelY, 90.deg), -35.5)

        val (initial, begin) = cycleGenerator(end)
        val (rest, last) = cycleGenerator(begin, first = false)

        AutoActions(purple.build(), yellow, Cycles(initial, rest), park(last))
    }

    override val right = run {
        val purple = drive.actionBuilder(start)
            .splineTo(point(19.0, -43.0), 60.deg)
            .endTrajectory()

        val (yellow, end) = yellowGenerator(pose(19.0, -43.0, 60.deg), -42.5)

        val (initial, begin) = cycleGenerator(end)
        val (rest, last) = cycleGenerator(begin, first = false)

        AutoActions(purple.build(), yellow, Cycles(initial, rest), park(last))
    }

    override fun park(target: Pose2d) =
        drive
            .actionBuilder(target)
            .setTangent(0.deg)
            .splineToSplineHeading(pose(60.0, -8.0, 90.deg), 0.deg)
            .build()
}