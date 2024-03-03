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

class FarCycles(drive: MecanumDrive, color: Alliance) : Auto(drive, color) {
	override val start = pose(-36.0, -64.0, if (color == Alliance.Blue) 270.invertibleDeg else 90.invertibleDeg)

	object StageConstraints {
		// Pose2dDual<Arclength>, PosePath, Double) → Double
		val velocity = VelConstraint { pose, _, _ ->
			when (pose.position.x.value()) {
				in -36.0..12.0 -> MAX_WHEEL_VEL / 1.5
				else -> MAX_WHEEL_VEL
			}
		}

		val accel = AccelConstraint { pose, _, _ ->
			when (pose.position.x.value()) {
				in -36.0..12.0 -> MinMax(PROFILE_DECEL / 1.5, PROFILE_ACCEL / 1.5)
				else -> MinMax(PROFILE_DECEL, PROFILE_ACCEL)
			}
		}
	}

	val score = pose(50.0, -12.5, (-45 - 180).deg)
	val lineUp = pose(12.0, -12.0, 180.deg)

	val whiteOffset = 49.0

	override val left = run {
		val poses = object {
			val purple = pose(-35.0, -32.0, 180.deg)
			val yellow = pose(53.0, -29.0, 180.deg)
			val stacks = pose(-56.0, -13.0, 180.deg)
		}

		val purple = drive.actionBuilder(start)
			.splineToLinearHeading(poses.purple, 90.invertibleDeg)
			.build()

		val stacks = drive.actionBuilder(poses.purple)
			.setTangent(75.invertibleDeg)
			.splineToLinearHeading(poses.stacks, 180.invertibleDeg)
			.build()

		val yellow = drive.actionBuilder(poses.stacks)
			.setTangent(0.invertibleDeg)
			.splineToLinearHeading(poses.yellow, (-45).invertibleDeg, StageConstraints.velocity, StageConstraints.accel)
			.build()

		val white = pose(whiteOffset, signed(poses.yellow.position.y), poses.yellow.heading.log())

		val fourth = drive.actionBuilder(poses.yellow)
			.setTangent(0.0)
			.splineToLinearHeading(white, (-45).invertibleDeg, StageConstraints.velocity, StageConstraints.accel)
			.build()

		val extra = drive.actionBuilder(white)
			.setTangent(180.invertibleDeg)
			.splineToLinearHeading(lineUp, 180.invertibleDeg)
			.build()

		val second = drive.actionBuilder(lineUp)
			.setTangent(180.invertibleDeg)
			.splineToLinearHeading(poses.stacks, 180.invertibleDeg)
			.build()

		val third = drive.actionBuilder(poses.stacks)
			.setTangent(0.invertibleDeg)
			.splineToLinearHeading(score, 345.invertibleDeg)
			.build()

		AutoActions(
			purple,
			yellow,
			SingleCycle(stacks = stacks, backstage = NullAction()),
			extras = listOf(extra, second, third, fourth),
			park = getPark()(drive.actionBuilder(score))
		)
	}

	override val middle = run {
		val y = (-(25.0 + HEIGHT / 2.0) - 3.5 + 24.0)

		val poses = object {
			val purple = pose(-35.0, y, 270.deg)
			val yellow = pose(53.0, -36.5, 180.deg)
			val stacks = pose(-56.0, -13.0, 180.deg)
		}

		val purple = drive.actionBuilder(start)
			.splineToLinearHeading(poses.purple, 90.invertibleDeg)
			.build()

		val stacks = drive.actionBuilder(poses.purple)
			.setTangent(180.invertibleDeg)
			.splineToLinearHeading(poses.stacks, 180.invertibleDeg)
			.build()

		val yellow = drive.actionBuilder(poses.stacks)
			.setTangent(0.invertibleDeg)
			.splineToLinearHeading(poses.yellow, (-45).invertibleDeg, StageConstraints.velocity, StageConstraints.accel)
			.build()

		val white = pose(whiteOffset, signed(poses.yellow.position.y), poses.yellow.heading.log())

		val fourth = drive.actionBuilder(poses.yellow)
			.setTangent(0.0)
			.splineToLinearHeading(white, (-45).invertibleDeg, StageConstraints.velocity, StageConstraints.accel)
			.build()

		val extra = drive.actionBuilder(white)
			.setTangent(180.invertibleDeg)
			.splineToLinearHeading(lineUp, 180.invertibleDeg)
			.build()

		val second = drive.actionBuilder(lineUp)
			.setTangent(180.invertibleDeg)
			.splineToLinearHeading(poses.stacks, 180.invertibleDeg)
			.build()

		val third = drive.actionBuilder(poses.stacks)
			.setTangent(0.invertibleDeg)
			.splineToLinearHeading(score, 345.invertibleDeg)
			.build()

		AutoActions(
			purple,
			yellow,
			SingleCycle(stacks = stacks, backstage = NullAction()),
			extras = listOf(extra, second, third, fourth),
			park = getPark()(drive.actionBuilder(score))
		)
	}

	override val right = run {
		val poses = object {
			val purple = pose(-35.0, -32.0, 0.deg)
			val yellow = pose(53.0, -41.0, 180.deg)
			val stacks = pose(-56.0, -13.0, 180.deg)
		}

		val purple = drive.actionBuilder(start)
			.splineToLinearHeading(poses.purple, 90.invertibleDeg)
			.build()

		val stacks = drive.actionBuilder(poses.purple)
			.setTangent(-(90).invertibleDeg)
			.splineToLinearHeading(poses.stacks, 180.invertibleDeg)
			.build()

		val yellow = drive.actionBuilder(poses.stacks)
			.setTangent(0.invertibleDeg)
			.splineToLinearHeading(poses.yellow, (-45).invertibleDeg, StageConstraints.velocity, StageConstraints.accel)
			.build()

		val white = pose(whiteOffset, signed(poses.yellow.position.y), poses.yellow.heading.log())

		val fourth = drive.actionBuilder(poses.yellow)
			.setTangent(0.0)
			.splineToLinearHeading(white, (-45).invertibleDeg, StageConstraints.velocity, StageConstraints.accel)
			.build()

		val extra = drive.actionBuilder(white)
			.setTangent(180.invertibleDeg)
			.splineToLinearHeading(lineUp, 180.invertibleDeg)
			.build()

		val second = drive.actionBuilder(lineUp)
			.setTangent(180.invertibleDeg)
			.splineToLinearHeading(poses.stacks, 180.invertibleDeg)
			.build()

		val third = drive.actionBuilder(poses.stacks)
			.setTangent(0.invertibleDeg)
			.splineToLinearHeading(score, 345.invertibleDeg)
			.build()

		AutoActions(
			purple,
			yellow,
			SingleCycle(stacks = stacks, backstage = NullAction()),
			extras = listOf(extra, second, third, fourth),
			park = getPark()(drive.actionBuilder(score))
		)
	}

	override fun getPark() = { builder: TrajectoryActionBuilder ->
		builder
			.splineToLinearHeading(pose(60.0, -12.5, 90.deg), 0.invertibleDeg)
			.build()
	}
}