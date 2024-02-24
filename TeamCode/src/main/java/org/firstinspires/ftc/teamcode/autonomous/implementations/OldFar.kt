package org.firstinspires.ftc.teamcode.autonomous.implementations

import com.acmerobotics.roadrunner.TrajectoryActionBuilder
import org.firstinspires.ftc.teamcode.autonomous.framework.Alliance
import org.firstinspires.ftc.teamcode.autonomous.framework.Auto
import org.firstinspires.ftc.teamcode.autonomous.framework.AutoActions
import org.firstinspires.ftc.teamcode.autonomous.framework.SingleCycle
import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive
import org.firstinspires.ftc.teamcode.util.extensions.deg

class OldFar(drive: MecanumDrive, color: Alliance) : Auto(drive, color) {
	override val start = pose(-36.0, -64.0, if (color == Alliance.Blue) 270.invertibleDeg else 90.invertibleDeg)

	override val left = run {
		val purple = drive.actionBuilder(start)
			.splineToConstantHeading(point(-46.5, -46.5), 90.invertibleDeg)
			.build()

		val cycle = drive.actionBuilder(pose(-46.5, -46.5, start.heading.log()))
			.setTangent(0.invertibleDeg)
			.lineToXConstantHeading(-36.0)
			.setTangent(45.invertibleDeg)
			.splineToLinearHeading(pose(-30.0, -11.5, 180.deg), 0.deg)
			.lineToXConstantHeading(-56.0)
			.build()

		val white = drive.actionBuilder(pose(-55.0, -11.5, 180.deg))
			.setTangent(0.invertibleDeg)
			.splineToConstantHeading(point(35.5, -10.0), 0.deg)
			.splineToConstantHeading(point(47.0, -44.0), 0.deg)
			.build()

		val yellow = drive.actionBuilder(pose(47.0, -44.0, 180.deg))
			.splineToConstantHeading(point(47.0, -31.0), 0.invertibleDeg)
			.build()

		AutoActions(purple, yellow, SingleCycle(cycle, white), getPark()(drive.actionBuilder(pose(47.0, -31.0, 180.deg))))
	}

	override val middle = run {
		val y = (-(25.0 + HEIGHT / 2.0) - 1.5 + 24.0)

		val purple = drive.actionBuilder(start)
			.lineToYLinearHeading(signed(y), 270.invertibleDeg)
			.build()

		val cycle = drive.actionBuilder(pose(-36.0, y, 270.invertibleDeg))
			.setTangent(180.invertibleDeg)
			.splineToLinearHeading(pose(-56.0, -11.5, 180.deg), 180.invertibleDeg)
			.build()

		val white = drive.actionBuilder(pose(-55.0, -11.5, 180.deg))
			.setTangent(250.invertibleDeg)
			.splineToLinearHeading(pose(-51.0, -10.0, 180.deg), 0.invertibleDeg)
			.splineToConstantHeading(point(35.5, -10.0), 0.invertibleDeg)
			.splineToConstantHeading(point(47.0, -31.0), 0.invertibleDeg)
			.build()

		val yellow = drive.actionBuilder(pose(47.0, -31.0, 180.deg))
			.splineToConstantHeading(point(47.0, -37.0), 0.invertibleDeg)
			.build()

		AutoActions(purple, yellow, SingleCycle(cycle, white), getPark()(drive.actionBuilder(pose(47.0, -37.0, 180.deg))))
	}

	override val right = run {
		val purple = drive.actionBuilder(start)
			.splineTo(point(-30.0, -40.0), 60.invertibleDeg)
			.build()

		val cycle = drive.actionBuilder(pose(-30.0, -40.0, 60.deg))
			.setTangent(180.deg)
			.splineToLinearHeading(pose(-56.0, -11.5, 180.deg), 180.invertibleDeg)
			.build()

		val white = drive.actionBuilder(pose(-55.0, -11.5, 180.deg))
			.setTangent(0.invertibleDeg)
			.splineToSplineHeading(pose(-40.0, -10.0, 180.deg), 0.invertibleDeg)
			.splineToConstantHeading(point(35.5, -10.0), 0.invertibleDeg)
			.splineToConstantHeading(point(47.0, -31.0), 0.invertibleDeg)
			.build()

		val yellow = drive.actionBuilder(pose(47.0, -31.0, 0.deg))
			.splineToConstantHeading(point(47.0, -44.0), 0.invertibleDeg)
			.build()

		AutoActions(purple, yellow, SingleCycle(cycle, white), getPark()(drive.actionBuilder(pose(47.0, -44.0, 180.deg))))
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