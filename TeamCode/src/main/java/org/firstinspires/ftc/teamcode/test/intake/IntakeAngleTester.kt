package org.firstinspires.ftc.teamcode.test.intake

import com.arcrobotics.ftclib.command.CommandOpMode
import com.arcrobotics.ftclib.command.InstantCommand
import com.arcrobotics.ftclib.command.button.GamepadButton
import com.arcrobotics.ftclib.gamepad.GamepadKeys
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.util.Range
import org.firstinspires.ftc.teamcode.hardware.Robot
import org.firstinspires.ftc.teamcode.hardware.Robot.IntakeHardware.Configuration.DOWN_ANGLE
import org.firstinspires.ftc.teamcode.hardware.Robot.IntakeHardware.Configuration.UP_ANGLE
import org.firstinspires.ftc.teamcode.hardware.Robot.gamepad
import org.firstinspires.ftc.teamcode.hardware.Robot.intake
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.intake.commands.DropIntake
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.intake.commands.RaiseIntake

@TeleOp
class IntakeAngleTester : CommandOpMode() {
	var target = DOWN_ANGLE

	// 33, 28, 20, 16, 10
	// intake purple out 750 ms @ -0.5
	override fun initialize() {
		Robot.initialize(hardwareMap, telemetry, gamepad1, gamepad2)

		GamepadButton(gamepad, GamepadKeys.Button.DPAD_UP).whenPressed(RaiseIntake(intake))
		GamepadButton(gamepad, GamepadKeys.Button.DPAD_DOWN).whenPressed(DropIntake(intake))
		GamepadButton(gamepad, GamepadKeys.Button.DPAD_LEFT).whenPressed(InstantCommand({ target = Range.clip(target - 1, DOWN_ANGLE, UP_ANGLE) }))
		GamepadButton(gamepad, GamepadKeys.Button.DPAD_RIGHT).whenPressed(InstantCommand({ target = Range.clip(target + 1, DOWN_ANGLE, UP_ANGLE) }))

		GamepadButton(gamepad, GamepadKeys.Button.A).whenPressed(InstantCommand({ intake.target(target) }))
		GamepadButton(gamepad, GamepadKeys.Button.X).whenPressed(InstantCommand({ intake.forward(1.0) }))
		GamepadButton(gamepad, GamepadKeys.Button.B).whenPressed(InstantCommand({ intake.reverse(1.0) }))
		GamepadButton(gamepad, GamepadKeys.Button.Y).whenPressed(InstantCommand({ intake.stop() }))

		GamepadButton(gamepad, GamepadKeys.Button.LEFT_BUMPER).whenPressed(InstantCommand({ intake.reverse(0.5) }))
		GamepadButton(gamepad, GamepadKeys.Button.RIGHT_BUMPER).whenPressed(InstantCommand({ intake.forward(0.5) }))
	}

	override fun run() {
		super.run()

		Robot.clearBulkCache()
		Robot.read()
		Robot.periodic()
		Robot.write()

		telemetry.addData("target", target)
		telemetry.addData("position", intake.target)

		telemetry.update()
	}
}