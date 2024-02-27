package org.firstinspires.ftc.teamcode.autonomous.implementations

import com.acmerobotics.roadrunner.Rotation2d
import com.acmerobotics.roadrunner.TrajectoryActionBuilder
import org.firstinspires.ftc.teamcode.autonomous.framework.Alliance
import org.firstinspires.ftc.teamcode.autonomous.framework.Auto
import org.firstinspires.ftc.teamcode.autonomous.framework.PreloadPark
import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive
import org.firstinspires.ftc.teamcode.util.extensions.deg
import kotlin.math.tan

class OldClose(drive: MecanumDrive, color: Alliance) : Auto(drive, color) {
	override val start = pose(12.0, -64.0, 90.invertibleDeg)

	override val left = run {
		val purple = drive.actionBuilder(start)
			.splineTo(point(8.0, -42.0), 125.invertibleDeg)
			.endTrajectory()

		val yellow = drive.actionBuilder(pose(8.0, -42.0, (125).deg))
			.setTangent(tan(11.5 / 41.5))
			.lineToXLinearHeading(47.0, Rotation2d.exp(180.deg))
			.endTrajectory()

		PreloadPark(purple.build(), yellow.build(), park = getPark()(drive.actionBuilder(pose(47.0, -(41.5 - 11.5), 180.deg))))
	}

	override val middle = run {
		val purple = drive.actionBuilder(start)
			.splineTo(point(12.0, (-(25.0 + HEIGHT / 2.0) - 1.5)), 90.invertibleDeg) // purple pixel
			.endTrajectory()

		val yellow = drive.actionBuilder(pose(12.0, (-(25.0 + HEIGHT / 2.0) - 1.5), (90).deg))
			.setTangent(0.deg)
			.splineToLinearHeading(pose(47.0, -37.0, 180.deg), 0.deg)
			.endTrajectory()

		PreloadPark(purple.build(), yellow.build(), park = getPark()(drive.actionBuilder(pose(47.0, -37.0, 180.deg))))
	}

	override val right = run {
		val purple = drive.actionBuilder(start)
			.splineTo(point(19.0, -43.0), 75.invertibleDeg)
			.endTrajectory()

		val yellow = drive.actionBuilder(pose(19.0, -43.0, (75).deg))
			.setTangent((-135).invertibleDeg)
			.lineToYConstantHeading(signed(-47.0))
			.setTangent(0.invertibleDeg)
			.splineToLinearHeading(pose(47.0, -44.0, 180.deg), 0.invertibleDeg)
			.endTrajectory()

		PreloadPark(purple.build(), yellow.build(), park = getPark()(drive.actionBuilder(pose(47.0, -44.0, 180.deg))))
	}

	override fun getPark() = { builder: TrajectoryActionBuilder ->
		builder
			.splineToLinearHeading(pose(60.0, -60.0, 90.deg), 0.0)
			.build()
	}
}