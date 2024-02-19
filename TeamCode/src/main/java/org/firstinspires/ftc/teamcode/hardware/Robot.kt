package org.firstinspires.ftc.teamcode.hardware

import com.arcrobotics.ftclib.command.CommandScheduler
import com.arcrobotics.ftclib.drivebase.MecanumDrive
import com.arcrobotics.ftclib.gamepad.GamepadEx
import com.arcrobotics.ftclib.hardware.SimpleServo
import com.arcrobotics.ftclib.hardware.motors.Motor
import com.qualcomm.hardware.lynx.LynxModule
import com.qualcomm.hardware.lynx.commands.standard.LynxSetModuleLEDColorCommand
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior.BRAKE
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.Gamepad
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.IMU
import com.qualcomm.robotcore.hardware.Servo
import com.qualcomm.robotcore.hardware.configuration.LynxConstants
import org.firstinspires.ftc.robotcore.external.Telemetry
import org.firstinspires.ftc.teamcode.hardware.Robot.DepositHardware.Configuration.SERVO_RANGE
import org.firstinspires.ftc.teamcode.hardware.Robot.Hubs.hubs
import org.firstinspires.ftc.teamcode.hardware.Robot.IntakeHardware.Configuration.DOWN_ANGLE
import org.firstinspires.ftc.teamcode.hardware.Robot.IntakeHardware.Configuration.RANGE
import org.firstinspires.ftc.teamcode.hardware.Robot.LauncherHardware.Configuration.HOLD
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.deposit.Deposit
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.drive.Drive
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.intake.Intake
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.launcher.Launcher
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.led.Led
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.lift.Lift
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.puncher.Puncher
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.deposit.Deposit.Angles as DepositAngles


object Robot {
	lateinit var telemetry: Telemetry
	lateinit var gamepad: GamepadEx
	lateinit var secondary: GamepadEx
	lateinit var hw: HardwareMap

	val subsystems by lazy { listOf(drive, deposit, lift, launcher, puncher, intake, led) }

	val drive by lazy { Drive(telemetry, gamepad) }
	val deposit by lazy { Deposit(telemetry, lift) }
	val lift by lazy { Lift(telemetry) }
	val launcher by lazy { Launcher() }
	val puncher by lazy { Puncher() }
	val intake by lazy { Intake() }
	val led by lazy { Led() }

	var count = 0

	fun initialize(hw: HardwareMap, telemetry: Telemetry, gamepad: Gamepad, secondary: Gamepad) {
		CommandScheduler.getInstance().reset()

		this.hw = hw
		this.gamepad = GamepadEx(gamepad)
		this.secondary = GamepadEx(secondary)
		this.telemetry = telemetry

		Hubs.initialize()


		subsystems.forEach { CommandScheduler.getInstance().registerSubsystem(it) }
	}

	object Hubs {
		lateinit var CONTROL: LynxModule
		lateinit var EXTENSION: LynxModule

		val hubs by lazy { listOf(CONTROL, EXTENSION) }

		fun initialize() {
			for (m in hw.getAll(LynxModule::class.java)) {
				m.bulkCachingMode = LynxModule.BulkCachingMode.MANUAL

				if (m.isParent && LynxConstants.isEmbeddedSerialNumber(m.serialNumber)) {
					CONTROL = m
				} else {
					EXTENSION = m
				}
			}

			hubs.forEach { it.bulkCachingMode = LynxModule.BulkCachingMode.MANUAL }
			hubs.forEach { it.sendCommand(LynxSetModuleLEDColorCommand(it, 155.toByte(), 0, 155.toByte())) }
		}
	}

	object DriveHardware {
		val frontLeft by lazy { Motor(hw, "frontLeft") }
		val frontRight by lazy { Motor(hw, "frontRight") }
		val backLeft by lazy { Motor(hw, "backLeft") }
		val backRight by lazy { Motor(hw, "backRight") }

		val drive by lazy { MecanumDrive(frontLeft, frontRight, backLeft, backRight) }

		val imu by lazy { (hw["imu"] as IMU) }

		var angle = 0.0

		init {
			frontLeft.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE)
			frontRight.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE)
			backLeft.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE)
			backRight.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE)

			backLeft.motor.direction = DcMotorSimple.Direction.REVERSE

			drive

			imu.initialize(IMU.Parameters(RevHubOrientationOnRobot(RevHubOrientationOnRobot.LogoFacingDirection.LEFT, RevHubOrientationOnRobot.UsbFacingDirection.DOWN)))
		}
	}

	object DepositHardware {
		val right by lazy { SimpleServo(hw, "deposit.right", 0.0, SERVO_RANGE).also { it.inverted = true } }
		val left by lazy { SimpleServo(hw, "deposit.left", 0.0, SERVO_RANGE) }
		val angles = DepositAngles(hw)

		object Configuration {
			const val SERVO_RANGE = 355.0 // degrees

			const val HORIZONTAL_BOUND = 65.0 // +/- degrees

			const val ALIGN_ANGLE = 0.0 // degrees
			const val TRANSFER_ANGLE = 0.0 // degrees
			const val SCORE_ANGLE = 160.0 // degrees

			const val HORIZONTAL_OFFSET = 3.0 // degrees
			const val VERTICAL_OFFSET = 0.0 // degrees
		}
	}

	object IntakeHardware {
		val arm by lazy { SimpleServo(hw, "arm", DOWN_ANGLE, RANGE) }
		val motor by lazy { (hw["perp"] as DcMotorEx).also { it.zeroPowerBehavior = BRAKE } }

		object Configuration {
			const val RANGE = 300.0 // degrees

			const val DOWN_ANGLE = 0.0 // degrees
			const val UP_ANGLE = 75.0 // degrees

			const val PIXEL_THICKNESS = 0.25
			const val RADIUS = 3.0 // in
		}
	}

	object LiftHardware {
		val right by lazy { hw["rightLift"] as DcMotorEx }
		val left by lazy { hw["leftLift"] as DcMotorEx }

		val motors by lazy { listOf(left, right) }
	}

	object PuncherHardware { val servo by lazy { hw["puncher"] as Servo } }

	object LauncherHardware {
		val servo by lazy {
			val it = hw["launcher"] as Servo

			it.direction = Servo.Direction.REVERSE
			it.position = HOLD
			it
		}

		object Configuration {
			const val HOLD = 0.35
			const val RELEASE = 1.0
		}
	}

	object LedHardware { val led by lazy { hw["led"] as DcMotor } }

	fun read() {
		subsystems.forEach { it.read() }

		if (count % 2 == 0) {
			drive.updateHeading() }
	}

	fun write() { subsystems.forEach { it.write() } }

	fun periodic() { subsystems.forEach { it.periodic() } }

	fun reset() { subsystems.forEach { it.reset() } }

	fun clearBulkCache() { hubs.forEach { it.clearBulkCache() } }
}