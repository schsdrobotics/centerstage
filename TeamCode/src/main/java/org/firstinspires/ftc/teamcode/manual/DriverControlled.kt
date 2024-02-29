package org.firstinspires.ftc.teamcode.manual

import com.acmerobotics.roadrunner.now
import com.arcrobotics.ftclib.command.CommandOpMode
import com.arcrobotics.ftclib.command.InstantCommand
import com.arcrobotics.ftclib.command.ParallelCommandGroup
import com.arcrobotics.ftclib.command.button.GamepadButton
import com.arcrobotics.ftclib.command.button.Trigger
import com.arcrobotics.ftclib.gamepad.GamepadKeys
import com.arcrobotics.ftclib.gamepad.GamepadKeys.Button
import com.arcrobotics.ftclib.gamepad.TriggerReader
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.hardware.Robot
import org.firstinspires.ftc.teamcode.hardware.Robot.deposit
import org.firstinspires.ftc.teamcode.hardware.Robot.drive
import org.firstinspires.ftc.teamcode.hardware.Robot.gamepad
import org.firstinspires.ftc.teamcode.hardware.Robot.intake
import org.firstinspires.ftc.teamcode.hardware.Robot.launcher
import org.firstinspires.ftc.teamcode.hardware.Robot.lift
import org.firstinspires.ftc.teamcode.hardware.Robot.puncher
import org.firstinspires.ftc.teamcode.hardware.Robot.relayer
import org.firstinspires.ftc.teamcode.hardware.Robot.secondary
import org.firstinspires.ftc.teamcode.hardware.cycles.LiftTo
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.Relayer
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.deposit.Deposit
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.deposit.commands.AdjustDeposit
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.deposit.commands.TransferDeposit
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.drive.ResetYawCommand
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.intake.commands.IntakeCycle
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.intake.commands.IntakeIn
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.intake.commands.IntakeOut
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.intake.commands.StopIntake
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.launcher.commands.HoldCommand
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.launcher.commands.LaunchCommand
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.lift.Lift.Position.*
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.lift.commands.AdjustLift
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.lift.commands.ForcefulLiftAdjustment
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.lift.commands.GoCommand
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.lift.commands.MoveLiftTo
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.puncher.commands.CyclePuncher
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.puncher.commands.DropPixels
import org.firstinspires.ftc.teamcode.util.extensions.currentDraw

@TeleOp(group = "!")
class DriverControlled : CommandOpMode() {
	val times = mutableListOf<Double>()

	override fun initialize() {
		Robot.initialize(hardwareMap, telemetry, gamepad1, gamepad2)
		Robot.reset()
		Robot.initialize(hardwareMap, telemetry, gamepad1, gamepad2)

		GamepadButton(gamepad, Button.A).whenPressed(LiftTo(ZERO, lift, deposit))
		GamepadButton(gamepad, Button.X).whenPressed(LiftTo(LOW, lift, deposit))
		GamepadButton(gamepad, Button.Y).whenPressed(LiftTo(MID, lift, deposit))
		GamepadButton(gamepad, Button.B).whenPressed(LiftTo(HIGH, lift, deposit))

		GamepadButton(gamepad, Button.LEFT_STICK_BUTTON).whenPressed(ResetYawCommand(drive))

		GamepadButton(gamepad, Button.RIGHT_BUMPER).whenPressed(IntakeCycle(intake))
		GamepadButton(gamepad, Button.LEFT_BUMPER).whenPressed(ParallelCommandGroup(
			CyclePuncher(puncher),
			InstantCommand({ lift.target += 40 }),
			GoCommand(lift)
		))

		Trigger { TriggerReader(gamepad, GamepadKeys.Trigger.RIGHT_TRIGGER).isDown }
			.whileActiveContinuous(
				ParallelCommandGroup(
					IntakeIn(intake) { gamepad.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER) },
					DropPixels(puncher),
					MoveLiftTo(INTAKE, lift)
				)
			)
			.whenInactive(
				ParallelCommandGroup(
					StopIntake(intake),
					TransferDeposit(deposit),
					MoveLiftTo(0, lift)
				)
			)

		Trigger { TriggerReader(gamepad, GamepadKeys.Trigger.LEFT_TRIGGER).isDown }
			.whileActiveContinuous(
				ParallelCommandGroup(
					IntakeOut(intake) { gamepad.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER) },
					DropPixels(puncher),
					MoveLiftTo(INTAKE, lift)
				)
			)
			.whenInactive(
				ParallelCommandGroup(
					StopIntake(intake),
					TransferDeposit(deposit),
					MoveLiftTo(0, lift)
				)
			)

		GamepadButton(secondary, Button.RIGHT_BUMPER).whenActive(InstantCommand({ relayer.selected = Relayer.SelectedHub.Expansion }))
		GamepadButton(secondary, Button.LEFT_BUMPER).whenActive(InstantCommand({ relayer.selected = Relayer.SelectedHub.Control }))

		GamepadButton(secondary, Button.START).whenPressed(InstantCommand({ lift.reset() }))
		GamepadButton(secondary, Button.BACK).whenPressed(InstantCommand({
			deposit.headingTarget = when (deposit.headingTarget) {
				90.0 -> 270.0
				270.0 -> 90.0
				else -> 270.0
			}
		}))

		GamepadButton(gamepad, Button.DPAD_RIGHT).whenPressed(AdjustLift(100, lift).andThen(GoCommand(lift)))
		GamepadButton(gamepad, Button.DPAD_LEFT).whenPressed(AdjustLift(-100, lift).andThen(GoCommand(lift)))

		GamepadButton(secondary, Button.DPAD_DOWN).whenPressed(ForcefulLiftAdjustment(-25, lift).andThen(GoCommand(lift)))
		GamepadButton(secondary, Button.DPAD_UP).whenPressed(ForcefulLiftAdjustment(25, lift).andThen(GoCommand(lift)))

		GamepadButton(secondary, Button.DPAD_LEFT).whenPressed(AdjustDeposit(-1.0, deposit))
		GamepadButton(secondary, Button.DPAD_RIGHT).whenPressed(AdjustDeposit(1.0, deposit))

		GamepadButton(secondary, Button.X)
			.whenActive(InstantCommand({ relayer.selectedToIndicator(Relayer.Indicator.Purple) }))
		GamepadButton(secondary, Button.Y)
			.whenActive(InstantCommand({ relayer.selectedToIndicator(Relayer.Indicator.Green) }))
		GamepadButton(secondary, Button.A)
			.whenActive(InstantCommand({ relayer.selectedToIndicator(Relayer.Indicator.White) }))
		GamepadButton(secondary, Button.B)
			.whenActive(InstantCommand({ relayer.selectedToIndicator(Relayer.Indicator.Yellow) }))

		GamepadButton(secondary, Button.LEFT_BUMPER)
			.whenActive(HoldCommand(launcher))

		GamepadButton(secondary, Button.RIGHT_BUMPER)
			.whenActive(LaunchCommand(launcher))

		Trigger { TriggerReader(secondary, GamepadKeys.Trigger.RIGHT_TRIGGER).isDown }
			.whenActive(InstantCommand({ launcher.up() }))

		Trigger { TriggerReader(secondary, GamepadKeys.Trigger.LEFT_TRIGGER).isDown }
			.whenActive(InstantCommand({ launcher.down() }))

		Robot.DepositHardware.right.turnToAngle(Deposit.Kinematics.inverse(deposit.align).right)
		Robot.DepositHardware.left.turnToAngle(Deposit.Kinematics.inverse(deposit.align).left)
	}

	override fun run() {
		val start = now()

		super.run()

		Robot.clearBulkCache()
		Robot.read()
		Robot.periodic()
		Robot.write()

		val end = now()
		val time = end - start

		times.add(1.0 / time)

//		telemetry.addData("average cycles/s (hZ)", times.average().roundToInt())
		telemetry.addData("current", currentDraw)
		telemetry.update()
	}
}