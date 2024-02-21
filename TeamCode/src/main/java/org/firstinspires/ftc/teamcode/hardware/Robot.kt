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
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.EfficientSubsystem
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

	lateinit var hardwares: List<IHardware>

	lateinit var subsystems: List<EfficientSubsystem>

	lateinit var drive: Drive
	lateinit var deposit: Deposit
	lateinit var lift: Lift
	lateinit var launcher: Launcher
	lateinit var puncher: Puncher
	lateinit var intake: Intake
	lateinit var led: Led

	var auto = false
	var count = 0

	fun initialize(hw: HardwareMap, telemetry: Telemetry, gamepad: Gamepad, secondary: Gamepad, auto: Boolean = false) {
		this.hw = hw
		this.telemetry = telemetry

		this.gamepad = GamepadEx(gamepad)
		this.secondary = GamepadEx(secondary)

		this.auto = auto

		hardwares = if (auto) {
			listOf(
				Hubs, DepositHardware, IntakeHardware, LiftHardware,
				PuncherHardware, LauncherHardware, LedHardware
			)
		} else {
			listOf(
				Hubs, DriveHardware, DepositHardware, IntakeHardware,
				LiftHardware, PuncherHardware, LauncherHardware, LedHardware
			)
		}

		hardwares.forEach { it.initialize() }

		if (!auto) drive = Drive(Robot.telemetry, Robot.gamepad)

		lift = Lift(Robot.telemetry)
		deposit = Deposit(Robot.telemetry, lift)
		launcher = Launcher()
		puncher = Puncher(telemetry = telemetry, state = if (auto) Puncher.State.TWO else Puncher.State.NONE)
		intake = Intake()
		led = Led()

		subsystems = if (auto) {
			listOf(deposit, lift, launcher, puncher, intake, led)
		} else {
			listOf(drive, deposit, lift, launcher, puncher, intake, led)
		}

		subsystems.forEach { it.reset() }

		CommandScheduler.getInstance().reset()

		subsystems.forEach { CommandScheduler.getInstance().registerSubsystem(it) }


		deposit.default()

		count = 0
	}

	object Hubs : IHardware {
		lateinit var CONTROL: LynxModule
		lateinit var EXTENSION: LynxModule

		lateinit var hubs: List<LynxModule>

		override fun initialize() {
			for (m in hw.getAll(LynxModule::class.java)) {
				if (m.isParent && LynxConstants.isEmbeddedSerialNumber(m.serialNumber)) {
					CONTROL = m
				} else {
					EXTENSION = m
				}
			}

			hubs = listOf(CONTROL, EXTENSION)

			if (!auto) {
				hubs.forEach { it.bulkCachingMode = LynxModule.BulkCachingMode.MANUAL }
			} else {
				hubs.forEach { it.bulkCachingMode = LynxModule.BulkCachingMode.OFF }
			}

			hubs.forEach { it.sendCommand(LynxSetModuleLEDColorCommand(it, 155.toByte(), 0, 155.toByte())) }
		}
	}

	object DriveHardware : IHardware {
		val frontLeft by lazy { Motor(hw, "frontLeft") }
		val frontRight by lazy { Motor(hw, "frontRight") }
		val backLeft by lazy { Motor(hw, "backLeft") }
		val backRight by lazy { Motor(hw, "backRight") }

		val drive by lazy { MecanumDrive(frontLeft, frontRight, backLeft, backRight) }

		val imu by lazy { (hw["imu"] as IMU) }

		var angle = 0.0

		override fun initialize() {
			frontLeft.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE)
			frontRight.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE)
			backLeft.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE)
			backRight.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE)

			backLeft.motor.direction = DcMotorSimple.Direction.REVERSE

			drive

			imu.initialize(IMU.Parameters(RevHubOrientationOnRobot(RevHubOrientationOnRobot.LogoFacingDirection.LEFT, RevHubOrientationOnRobot.UsbFacingDirection.DOWN)))
		}
	}

	object DepositHardware : IHardware {
		lateinit var right: SimpleServo
		lateinit var left: SimpleServo

		lateinit var angles: DepositAngles

		override fun initialize() {
			right = SimpleServo(hw, "deposit.left", 0.0, SERVO_RANGE)
			left = SimpleServo(hw, "deposit.right", 0.0, SERVO_RANGE).also { it.inverted = true }

			angles = DepositAngles(hw)
		}

		object Configuration {
			const val SERVO_RANGE = 355.0 // degrees

			const val HORIZONTAL_BOUND = 65.0 // +/- degrees

			const val ALIGN_ANGLE = 0.0 // degrees
			const val TRANSFER_ANGLE = 19.0 // degrees
			const val SCORE_ANGLE = 160.0 // degrees

			const val HORIZONTAL_OFFSET = 3.0 // degrees, + is ccw
			const val VERTICAL_OFFSET = 0.0 // degrees, + is towards scoring
		}
	}


	object IntakeHardware : IHardware {
		lateinit var arm: SimpleServo
		lateinit var motor: DcMotorEx

		object Configuration {
			const val RANGE = 300.0 // degrees

			const val DOWN_ANGLE = 0.0 // degrees
			const val UP_ANGLE = 75.0 // degrees

			const val PIXEL_THICKNESS = 0.25
			const val RADIUS = 3.0 // in
		}

		override fun initialize() {
			arm = SimpleServo(hw, "arm", DOWN_ANGLE, RANGE)
			motor = (hw["perp"] as DcMotorEx).also { it.zeroPowerBehavior = BRAKE }
		}

	}

	object LiftHardware : IHardware {
		lateinit var right: DcMotorEx
		lateinit var left: DcMotorEx

		lateinit var motors: List<DcMotorEx>

		override fun initialize() {
			right = hw["rightLift"] as DcMotorEx
			left = hw["leftLift"] as DcMotorEx

			motors = listOf(left, right)
		}
	}

	object PuncherHardware : IHardware {
		lateinit var servo: Servo

		override fun initialize() {
			servo = hw["puncher"] as Servo
		}
	}

	object LauncherHardware : IHardware {
		lateinit var servo: Servo

		override fun initialize() {
			servo = run {
				val it = hw["launcher"] as Servo

				it.direction = Servo.Direction.REVERSE
				it.position = HOLD
				it
			}
		}

		object Configuration {
			const val HOLD = 0.35
			const val RELEASE = 1.0
		}
	}

	object LedHardware : IHardware {
		val led by lazy { hw["led"] as DcMotor }
		override fun initialize() {}
	}

	fun read() {
		subsystems.forEach { it.read() }

		if (!auto && count % 2 == 0) {
			drive.updateHeading()
		}
	}

	fun write() {
		subsystems.forEach { it.write() }
	}

	fun periodic() {
		subsystems.forEach { it.periodic() }
	}

	fun reset() {
		subsystems.forEach { it.reset() }
	}

	fun clearBulkCache() {
		telemetry.clearAll()
		hubs.forEach { it.clearBulkCache() }
	}

	interface IHardware {
		fun initialize()
	}
}