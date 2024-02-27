package org.firstinspires.ftc.teamcode.autonomous.implementations

import com.acmerobotics.roadrunner.NullAction
import com.acmerobotics.roadrunner.TrajectoryActionBuilder
import org.firstinspires.ftc.teamcode.autonomous.framework.Alliance
import org.firstinspires.ftc.teamcode.autonomous.framework.Auto
import org.firstinspires.ftc.teamcode.autonomous.framework.AutoActions
import org.firstinspires.ftc.teamcode.autonomous.framework.SingleCycle
import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive
import org.firstinspires.ftc.teamcode.util.extensions.deg

class FarPreloads(drive: MecanumDrive, color: Alliance) : Auto(drive, color) {
	override val start = pose(-36.0, -64.0, if (color == Alliance.Blue) 270.invertibleDeg else 90.invertibleDeg)

	override val left = run {
		val poses = object {
			val purple = pose(-35.0, -32.0, 180.deg)
			val stacks = pose(-55.0, -11.5, 180.deg)
			val yellow = pose(52.0, -29.0, 180.deg)
			val extra = pose(47.5, -12.0, 180.deg)
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
			.splineToLinearHeading(poses.yellow, (-45).invertibleDeg)
			.build()

		val extra = drive.actionBuilder(poses.yellow)
			.setTangent(180.deg)
			.splineToLinearHeading(poses.extra, 0.deg)
			.build()

		AutoActions(purple, yellow, SingleCycle(stacks = stacks, backstage = NullAction()), extra = extra, park = getPark()(drive.actionBuilder(poses.extra)))
	}

	override val middle = run {
		val y = (-(25.0 + HEIGHT / 2.0) - 1.5 + 24.0)

		val poses = object {
			val purple = pose(-35.0, y, 270.deg)
			val stacks = pose(-55.0, -11.5, 180.deg)
			val yellow = pose(52.0, -35.0, 180.deg)
			val extra = pose(47.5, -12.0, 180.deg)
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
			.splineToLinearHeading(poses.yellow, (-45).invertibleDeg)
			.build()

		val extra = drive.actionBuilder(poses.yellow)
			.setTangent(180.deg)
			.splineToLinearHeading(poses.extra, 0.deg)
			.build()

		AutoActions(purple, yellow, SingleCycle(stacks = stacks, backstage = NullAction()), extra = extra, park = getPark()(drive.actionBuilder(poses.extra)))
	}

	override val right = run {
		val poses = object {
			val purple = pose(-35.0, -32.0, 0.deg)
			val stacks = pose(-55.0, -11.5, 180.deg)
			val yellow = pose(52.0, -41.0, 180.deg)
			val extra = pose(47.5, -12.0, 180.deg)
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
			.splineToLinearHeading(poses.yellow, (-45).invertibleDeg)
			.build()

		val extra = drive.actionBuilder(poses.yellow)
			.setTangent(180.deg)
			.splineToLinearHeading(poses.extra, 0.deg)
			.build()

		AutoActions(purple, yellow, SingleCycle(stacks = stacks, backstage = NullAction()), extra = extra, park = getPark()(drive.actionBuilder(poses.extra)))
	}

	override fun getPark() = { builder: TrajectoryActionBuilder ->
		builder
			.splineToLinearHeading(pose(60.0, -12.5, 90.deg), 180.invertibleDeg)
			.build()
	}
}
