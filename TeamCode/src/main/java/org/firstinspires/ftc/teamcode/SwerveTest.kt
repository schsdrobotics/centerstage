package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.ServoImplEx
import kotlin.math.hypot

@TeleOp
class SwerveTest : OpMode() {
	val motor by lazy { hardwareMap["motor"] as DcMotorEx }
	val servo by lazy { hardwareMap["servo"] as ServoImplEx }

	override fun init() { motor; servo }

	override fun loop() {
		motor.power = hypot(gamepad1.left_stick_x.toDouble(), gamepad1.left_stick_y.toDouble())
		servo.position = (gamepad1.right_stick_x.toDouble() + 1) / 2.0
	}
}