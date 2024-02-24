package org.firstinspires.ftc.teamcode.autonomous.implementations

import com.acmerobotics.roadrunner.AccelConstraint
import com.acmerobotics.roadrunner.Action
import com.acmerobotics.roadrunner.Arclength
import com.acmerobotics.roadrunner.MinMax
import com.acmerobotics.roadrunner.NullAction
import com.acmerobotics.roadrunner.Pose2d
import com.acmerobotics.roadrunner.Pose2dDual
import com.acmerobotics.roadrunner.TrajectoryActionBuilder
import com.acmerobotics.roadrunner.VelConstraint
import org.firstinspires.ftc.teamcode.autonomous.framework.Alliance
import org.firstinspires.ftc.teamcode.autonomous.framework.Auto
import org.firstinspires.ftc.teamcode.autonomous.framework.AutoActions
import org.firstinspires.ftc.teamcode.autonomous.framework.Cycle
import org.firstinspires.ftc.teamcode.autonomous.framework.Cycles
import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive
import org.firstinspires.ftc.teamcode.util.extensions.deg

class Far(drive: MecanumDrive, color: Alliance) : Auto(drive, color) {
    override val start = pose(-36.0 - MINOR_APOTHEM, -64.0, Math.toRadians(89.995))

    private fun yellowGenerator(begin: Pose2d, y: Double): Triple<Cycle, Pose2d, Action> {
        val stackVelConstraint = VelConstraint { pose: Pose2dDual<Arclength>, _, _ ->
            if (pose.position.y.value() < signed(-12.5)) {
                50.0
            } else {
                MAX_WHEEL_VEL
            }
        }

        val stackAccelConstraint = AccelConstraint { pose: Pose2dDual<Arclength>, _, _ ->
            if (pose.position.y.value() < signed(-12.5)) {
                MinMax(-25.0, 30.0)
            } else {
                MinMax(PROFILE_DECEL, PROFILE_ACCEL)
            }
        }

        val stackX = -(69.0 - HEIGHT + 6.5)
        val stackPose = pose(stackX, -11.5, 180.invertibleDeg)
        val stackTangent = 90.invertibleDeg

        val stack = drive.actionBuilder(begin)
            .setTangent(0.deg)
//            .splineToLinearHeading(stackPose, 180.invertibleDeg, stackVelConstraint, stackAccelConstraint)
            .lineToXLinearHeading(begin.position.x + 1.0, 180.invertibleDeg)
            .endTrajectory()

        val backstageTangent = (0).deg

        val end = pose(49.75, y, 180.deg)

        val backstage = drive.actionBuilder(pose(begin.position.x + 1.0, -11.5, 180.invertibleDeg))
            .setTangent(0.invertibleDeg)
            .lineToXConstantHeading(end.position.x - 10.0)
            .setTangent(90.invertibleDeg)
            .lineToYConstantHeading(end.position.y)
            .setTangent(0.invertibleDeg)
            .lineToXConstantHeading(end.position.x)

        val intermediate = pose(47.0, -16.25, 150.deg)

        val adjustment = drive.actionBuilder(end)
            .splineToLinearHeading(intermediate, 90.invertibleDeg)
            .build()

        return Triple(Cycle(backstage = backstage.build(), stacks = stack.build()), end, adjustment)
    }

    private fun cycleGenerator(begin: Pose2d): Pair<Cycle, Pose2d> {
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

        val stackX = -(69.0 - HEIGHT + 6.5)
        val stackTangent = 170.invertibleDeg

        val stackPose = pose(stackX, -11.5, 180.deg)

        val stack = drive.actionBuilder(begin)
            .setTangent(stackTangent)
            .splineToLinearHeading(stackPose, 180.deg, stackVelConstraint, stackAccelConstraint)
            .endTrajectory()

        val intermediate = pose(47.0, -16.25, 150.invertibleDeg)

        val backstageTangent = (0).deg

        val backstage = drive.actionBuilder(stackPose)
            .setTangent(backstageTangent)
            .splineToSplineHeading(intermediate, 0.deg)

        return Pair(Cycle(backstage = backstage.build(), stacks = stack.build()), intermediate)
    }

    override val left = run {
        val purplePose = pose(-33.0, -31.0, 180.deg)

        val purple = drive.actionBuilder(start)
            .splineToSplineHeading(purplePose, 90.invertibleDeg)
            .endTrajectory()

        val (initial, begin, adjustment) = yellowGenerator(purplePose, -29.5)
        val (rest, end) = cycleGenerator(begin)

        AutoActions(purple.build(), NullAction(), Cycles(initial, rest), getPark()(drive.actionBuilder(end)), adjustment)
    }

    override val middle = run {
        val purplePose = pose(-36.0, -12.0, 270.deg)

        val purple = drive.actionBuilder(start)
            .splineToLinearHeading(purplePose, 90.invertibleDeg)
            .endTrajectory()

        val (initial, begin, adjustment) = yellowGenerator(purplePose, -35.5)
        val (rest, end) = cycleGenerator(begin)

        AutoActions(purple.build(), NullAction(), Cycles(initial, rest), getPark()(drive.actionBuilder(end)), adjustment)
    }

    override val right = run {
        val purplePose = pose(-35.0, -32.0, 0.deg)

        val purple = drive.actionBuilder(start)
            .splineToLinearHeading(purplePose, 90.invertibleDeg)
            .endTrajectory()

        val (initial, begin, adjustment) = yellowGenerator(purplePose, -42.5)
        val (rest, end) = cycleGenerator(begin)

        AutoActions(purple.build(), NullAction(), Cycles(initial, rest), getPark()(drive.actionBuilder(end)), adjustment)
    }

    override fun getPark() = { builder: TrajectoryActionBuilder ->
        builder
            .setReversed(true)
            .setTangent(0.invertibleDeg)
            .lineToXConstantHeading(47.0)
            .setTangent(90.invertibleDeg)
            .lineToYLinearHeading(signed(-12.5), 90.invertibleDeg)
            .setTangent(0.invertibleDeg)
            .lineToXConstantHeading(60.0)
            .build()
    }
}