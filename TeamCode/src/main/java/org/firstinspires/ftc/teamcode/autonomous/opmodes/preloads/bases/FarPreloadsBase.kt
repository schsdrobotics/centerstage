package org.firstinspires.ftc.teamcode.autonomous.opmodes.preloads.bases

import com.arcrobotics.ftclib.command.ParallelCommandGroup
import com.arcrobotics.ftclib.command.SequentialCommandGroup
import com.arcrobotics.ftclib.command.WaitCommand
import com.arcrobotics.ftclib.command.WaitUntilCommand
import org.firstinspires.ftc.teamcode.autonomous.framework.Alliance
import org.firstinspires.ftc.teamcode.autonomous.framework.AutonomousOpMode
import org.firstinspires.ftc.teamcode.autonomous.framework.Side
import org.firstinspires.ftc.teamcode.hardware.Robot
import org.firstinspires.ftc.teamcode.hardware.Robot.intake
import org.firstinspires.ftc.teamcode.hardware.cycles.UnsafeLiftZero
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.ActionCommand
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.deposit.commands.ScoreDeposit
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.deposit.commands.TransferDeposit
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.intake.commands.IntakeIn
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.intake.commands.IntakeOut
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.intake.commands.IntakeTo
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.intake.commands.IntakeToStackHeight
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.intake.commands.RaiseIntake
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.intake.commands.StopIntake
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.lift.Lift
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.lift.commands.MoveLiftTo
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.puncher.commands.DropPixels
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.puncher.commands.PunchPixels

open class FarPreloadsBase(side: Alliance, position: Side) : AutonomousOpMode(side, position) {
	override fun first() {
		intake.target = 20.0; intake.periodic()
		PunchPixels(Robot.puncher).initialize()
	}

	override fun actions() = SequentialCommandGroup(
		IntakeTo(20.0, intake),

		ActionCommand(path.purple),
		IntakeOut(intake) { 0.65 },
		WaitCommand(1500),
		StopIntake(intake),

		ActionCommand(path.cycles.initial.stacks),
		IntakeToStackHeight(5, intake),
		IntakeIn(intake) { 0.7 },

		TransferDeposit(Robot.deposit, false),
		PunchPixels(Robot.puncher),

		ParallelCommandGroup(
			ActionCommand(path.yellow),
			SequentialCommandGroup(
				RaiseIntake(intake),
				WaitUntilCommand { drive.pose.position.x >= 8.0 },
				MoveLiftTo(Lift.Position.LOW, Robot.lift),
				ScoreDeposit(Robot.deposit, false),
				MoveLiftTo(Lift.Position.LOW.ticks - 150, Robot.lift),
				WaitUntilCommand { drive.pose.position.x >= 51.0 },
				WaitCommand(1000),
				DropPixels(Robot.puncher),
			)
		),

		ActionCommand(path.extra),

		MoveLiftTo(Lift.Position.LOW.ticks, Robot.lift),
		TransferDeposit(Robot.deposit, false),
		WaitCommand(250),
		UnsafeLiftZero(Robot.lift),

		ActionCommand(path.park)
	)
}