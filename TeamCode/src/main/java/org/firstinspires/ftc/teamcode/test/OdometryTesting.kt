package org.firstinspires.ftc.teamcode.test

import com.acmerobotics.roadrunner.Pose2d
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive
import org.firstinspires.ftc.teamcode.roadrunner.TwoDeadWheelLocalizer

@TeleOp
class OdometryTesting : OpMode() {
	var start = Pose2d(0.0, 0.0, 0.0)
	val drive by lazy { MecanumDrive(hardwareMap, start) }


	override fun init() { drive }

	override fun loop() {
		val it = drive.localizer as TwoDeadWheelLocalizer

		telemetry.addData("perp ticks", it.perp.getPositionAndVelocity().position)
		telemetry.addData("par ticks", it.par.getPositionAndVelocity().position)
	}
}