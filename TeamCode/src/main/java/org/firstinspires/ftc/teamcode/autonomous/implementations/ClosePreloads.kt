package org.firstinspires.ftc.teamcode.autonomous.implementations

import com.acmerobotics.roadrunner.AccelConstraint
import com.acmerobotics.roadrunner.MinMax
import com.acmerobotics.roadrunner.NullAction
import com.acmerobotics.roadrunner.TrajectoryActionBuilder
import com.acmerobotics.roadrunner.VelConstraint
import org.firstinspires.ftc.teamcode.autonomous.framework.Alliance
import org.firstinspires.ftc.teamcode.autonomous.framework.Auto
import org.firstinspires.ftc.teamcode.autonomous.framework.AutoActions
import org.firstinspires.ftc.teamcode.autonomous.framework.SingleCycle
import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive
import org.firstinspires.ftc.teamcode.util.extensions.deg

class ClosePreloads(drive: MecanumDrive, color: Alliance) : Auto(drive, color) {
	override val start = pose(12.0, -64.0, if (color == Alliance.Blue) 270.invertibleDeg else 90.invertibleDeg)

	object StageConstraints {
		// Pose2dDual<Arclength>, PosePath, Double) → Double
		val velocity = VelConstraint { pose, _, _ ->
			when (pose.position.x.value()) {
//				in -36.0..12.0 -> MAX_WHEEL_VEL / 1.5
				else -> MAX_WHEEL_VEL
			}
		}

		val accel = AccelConstraint { pose, _, _ ->
			when (pose.position.x.value()) {
//				in -36.0..12.0 -> MinMax(PROFILE_DECEL / 1.5, PROFILE_ACCEL / 1.5)
				else -> MinMax(PROFILE_DECEL, PROFILE_ACCEL)
			}
		}
	}

	override val left = run {
		val poses = object {
			val purple = pose(-35.0 + 48.0, -32.0, 180.deg)
			val yellow = pose(52.0, -29.0, 180.deg)
			val extra = pose(35.0, -60.0, 180.deg)
		}

		val purple = drive.actionBuilder(start)
			.splineToLinearHeading(poses.purple, 90.invertibleDeg)
			.build()

		val yellow = drive.actionBuilder(poses.purple)
			.setTangent(0.invertibleDeg)
			.splineToLinearHeading(poses.yellow, (-0).invertibleDeg, StageConstraints.velocity, StageConstraints.accel)
			.build()

		val extra = drive.actionBuilder(poses.yellow)
			.setTangent(180.deg)
			.splineToLinearHeading(poses.extra, 0.deg)
			.build()

		AutoActions(purple, yellow, SingleCycle(stacks = NullAction(), backstage = NullAction()), extra = extra, park = getPark()(drive.actionBuilder(poses.extra)))
	}

	override val middle = run {
		val y = (-(25.0 + HEIGHT / 2.0) - 1.5)

		val poses = object {
			val purple = pose(-35.0 + 48.0, y, 90.deg)
			val yellow = pose(52.0, -36.5, 180.deg)
			val extra = pose(35.0, -60.0, 180.deg)
		}

		val purple = drive.actionBuilder(start)
			.splineToLinearHeading(poses.purple, 90.invertibleDeg)
			.build()

		val yellow = drive.actionBuilder(poses.purple)
			.setTangent(0.invertibleDeg)
			.splineToLinearHeading(poses.yellow, (-45).invertibleDeg, StageConstraints.velocity, StageConstraints.accel)
			.build()

		val extra = drive.actionBuilder(poses.yellow)
			.setTangent(180.deg)
			.splineToLinearHeading(poses.extra, 0.deg)
			.build()

		AutoActions(purple, yellow, SingleCycle(stacks = NullAction(), backstage = NullAction()), extra = extra, park = getPark()(drive.actionBuilder(poses.extra)))
	}

	override val right = run {
		val poses = object {
			val purple = pose(36.0, -32.0, 180.deg)
			val yellow = pose(52.0, -41.0, 180.deg)
			val extra = pose(35.0, -60.0, 180.deg)
		}

		val purple = drive.actionBuilder(start)
			.splineToLinearHeading(poses.purple, 90.invertibleDeg)
			.build()

		val yellow = drive.actionBuilder(poses.purple)
			.setTangent(0.invertibleDeg)
			.splineToLinearHeading(poses.yellow, (-45).invertibleDeg, StageConstraints.velocity, StageConstraints.accel)
			.build()

		val extra = drive.actionBuilder(poses.yellow)
			.setTangent(180.deg)
			.splineToLinearHeading(poses.extra, 0.deg)
			.build()

		AutoActions(purple, yellow, SingleCycle(stacks = NullAction(), backstage = NullAction()), extra = extra, park = getPark()(drive.actionBuilder(poses.extra)))
	}

	override fun getPark() = { builder: TrajectoryActionBuilder ->
		builder
			.splineToLinearHeading(pose(60.0, -60.0, 90.deg), 180.invertibleDeg)
			.build()
	}
}