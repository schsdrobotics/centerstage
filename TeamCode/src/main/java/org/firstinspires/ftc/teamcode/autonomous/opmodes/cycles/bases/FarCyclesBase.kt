package org.firstinspires.ftc.teamcode.autonomous.opmodes.cycles.bases

import com.acmerobotics.roadrunner.Vector2d
import com.arcrobotics.ftclib.command.InstantCommand
import com.arcrobotics.ftclib.command.ParallelCommandGroup
import com.arcrobotics.ftclib.command.SequentialCommandGroup
import com.arcrobotics.ftclib.command.WaitCommand
import org.firstinspires.ftc.teamcode.autonomous.framework.Alliance
import org.firstinspires.ftc.teamcode.autonomous.framework.AutonomousOpMode
import org.firstinspires.ftc.teamcode.autonomous.framework.Side
import org.firstinspires.ftc.teamcode.hardware.Robot
import org.firstinspires.ftc.teamcode.hardware.Robot.puncher
import org.firstinspires.ftc.teamcode.hardware.cycles.DriveUntil
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

open class FarCyclesBase(side: Alliance, position: Side) : AutonomousOpMode(side, position) {
	override fun first() {
		Robot.intake.target = 35.0; Robot.intake.periodic()
		PunchPixels(puncher).initialize()
	}

	override fun actions() = SequentialCommandGroup(
		ActionCommand(path.purple),
		IntakeOut(Robot.intake) { 1.0 },
		WaitCommand(750),
		StopIntake(Robot.intake),
		RaiseIntake(Robot.intake),

		ActionCommand(path.cycles.initial.backstage),
		ActionCommand(path.cycles.initial.stacks),
		ActionCommand(path.yellow),

		SequentialCommandGroup(
			MoveLiftTo(Lift.Position.LOW, Robot.lift),
			ScoreDeposit(Robot.deposit, false),
			WaitCommand(250),
			MoveLiftTo(Lift.Position.CLEAR.ticks + 25, Robot.lift),
		),

		DriveUntil(Vector2d(-0.2, 0.0), 0.0, { currentDraw >= 2.0 }, drive),
		WaitCommand(250),
		DropPixels(puncher),
		WaitCommand(250),
		TransferDeposit(Robot.deposit, false),
		WaitCommand(250),

		SequentialCommandGroup(
			MoveLiftTo(Lift.Position.LOW, Robot.lift),
			TransferDeposit(Robot.deposit, false),
			WaitCommand(1000),
		),

		ParallelCommandGroup(
			ActionCommand(path.park),
			InstantCommand({ requestOpModeStop() })
		),
	)
}