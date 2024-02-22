package org.firstinspires.ftc.teamcode.test

import com.arcrobotics.ftclib.drivebase.MecanumDrive
import com.arcrobotics.ftclib.hardware.motors.Motor
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.IMU
import org.firstinspires.ftc.teamcode.util.Feburary

@TeleOp
class AprilTagDriver : OpMode() {
	val feburary by lazy { Feburary(hardwareMap, this) }

	val frontLeft by lazy { Motor(hardwareMap, "frontLeft") }
	val frontRight by lazy { Motor(hardwareMap, "frontRight") }
	val backLeft by lazy { Motor(hardwareMap, "backLeft") }
	val backRight by lazy { Motor(hardwareMap, "backRight") }

	val drive by lazy { MecanumDrive(frontLeft, frontRight, backLeft, backRight) }

	val imu by lazy { (hardwareMap["imu"] as IMU) }

	override fun init() {
		frontLeft.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE)
		frontRight.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE)
		backLeft.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE)
		backRight.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE)

		backLeft.motor.direction = DcMotorSimple.Direction.REVERSE

		drive

		imu.initialize(IMU.Parameters(RevHubOrientationOnRobot(RevHubOrientationOnRobot.LogoFacingDirection.LEFT, RevHubOrientationOnRobot.UsbFacingDirection.DOWN)))

		feburary
	}

	override fun loop() {
		if (gamepad1.a) {
			val fed = feburary.feed()
			drive.driveRobotCentric(fed.x, fed.y, fed.z, true)
		} else {
			drive.driveRobotCentric(gamepad1.left_stick_x.toDouble(), -gamepad1.left_stick_y.toDouble(), -gamepad1.right_stick_x.toDouble(), true)
		}

		feburary.test()

		telemetry.update()
	}
}