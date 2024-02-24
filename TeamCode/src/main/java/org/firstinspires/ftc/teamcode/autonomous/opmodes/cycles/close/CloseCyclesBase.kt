package org.firstinspires.ftc.teamcode.autonomous.opmodes.cycles.close

import com.acmerobotics.roadrunner.Vector2d
import com.arcrobotics.ftclib.command.ParallelCommandGroup
import com.arcrobotics.ftclib.command.SequentialCommandGroup
import com.arcrobotics.ftclib.command.WaitCommand
import org.firstinspires.ftc.teamcode.autonomous.framework.Alliance
import org.firstinspires.ftc.teamcode.autonomous.framework.AutonomousOpMode
import org.firstinspires.ftc.teamcode.autonomous.framework.Side
import org.firstinspires.ftc.teamcode.hardware.Robot.deposit
import org.firstinspires.ftc.teamcode.hardware.Robot.intake
import org.firstinspires.ftc.teamcode.hardware.Robot.lift
import org.firstinspires.ftc.teamcode.hardware.Robot.puncher
import org.firstinspires.ftc.teamcode.hardware.cycles.DriveUntil
import org.firstinspires.ftc.teamcode.hardware.cycles.UnsafeLiftZero
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.ActionCommand
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.deposit.commands.ScoreDeposit
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.deposit.commands.TransferDeposit
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.intake.commands.IntakeOut
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.intake.commands.RaiseIntake
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.intake.commands.StopIntake
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.lift.Lift
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.lift.commands.MoveLiftTo
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.puncher.commands.DropPixels
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.puncher.commands.PunchPixels
import org.firstinspires.ftc.teamcode.util.extensions.currentDraw

open class CloseCyclesBase(side: Alliance, position: Side) : AutonomousOpMode(side, position) {
	override fun first() {
		intake.target = 35.0; intake.periodic()
		PunchPixels(puncher).initialize()
	}

	override fun actions() = SequentialCommandGroup(
		ParallelCommandGroup(
			SequentialCommandGroup(
				ActionCommand(path.purple),
				IntakeOut(intake) { 1.0 },
				WaitCommand(750),
				StopIntake(intake),
				RaiseIntake(intake),
			),

			SequentialCommandGroup(
				MoveLiftTo(Lift.Position.LOW, lift),
				ScoreDeposit(deposit, false),
				WaitCommand(250),
				MoveLiftTo(Lift.Position.CLEAR.ticks + 25, lift),
			)
		),

		ActionCommand(path.yellow),

		DriveUntil(Vector2d(-0.2, 0.0), 0.0, { currentDraw >= 2.3 }, drive),
		WaitCommand(250),
		DropPixels(puncher),
		WaitCommand(250),

		SequentialCommandGroup(
			MoveLiftTo(Lift.Position.LOW.ticks + 25, lift),
			TransferDeposit(deposit, false),
			WaitCommand(1000),
			UnsafeLiftZero(lift)
		),

		ParallelCommandGroup(
			ActionCommand(path.park),
		),
	)
}