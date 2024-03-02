package org.firstinspires.ftc.teamcode.autonomous.opmodes.cycles.bases

import com.arcrobotics.ftclib.command.ParallelCommandGroup
import com.arcrobotics.ftclib.command.SequentialCommandGroup
import com.arcrobotics.ftclib.command.WaitCommand
import com.arcrobotics.ftclib.command.WaitUntilCommand
import org.firstinspires.ftc.teamcode.autonomous.framework.Alliance
import org.firstinspires.ftc.teamcode.autonomous.framework.AutonomousOpMode
import org.firstinspires.ftc.teamcode.autonomous.framework.Nature
import org.firstinspires.ftc.teamcode.autonomous.framework.Side
import org.firstinspires.ftc.teamcode.hardware.Robot
import org.firstinspires.ftc.teamcode.hardware.Robot.intake
import org.firstinspires.ftc.teamcode.hardware.Robot.puncher
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
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.puncher.commands.DropOnePixel
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.puncher.commands.DropPixels
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.puncher.commands.PunchPixels

open class FarCyclesBase(side: Alliance, position: Side) : AutonomousOpMode(side, position, Nature.Cycles) {
	override fun first() {
		intake.target = 20.0; intake.periodic()
		PunchPixels(puncher).initialize()
	}

	override fun actions() = SequentialCommandGroup(
		// score purple
		IntakeTo(20.0, intake),

		ActionCommand(path.purple),
		IntakeOut(intake) { 0.65 },
		WaitCommand(1000),
		StopIntake(intake),
		RaiseIntake(intake),

		DropPixels(puncher),
		MoveLiftTo(Lift.Position.INTAKE, Robot.lift),

		// get from stack
		ActionCommand(path.cycles.initial.stacks),
		IntakeToStackHeight(5, intake),
		IntakeIn(intake) { 1.0 },

		WaitCommand(2000),

		MoveLiftTo(Lift.Position.ZERO, Robot.lift),

		TransferDeposit(Robot.deposit, false),
		WaitCommand(350),
		PunchPixels(puncher),

		// score yellow

		ParallelCommandGroup(
			ActionCommand(path.yellow),
			SequentialCommandGroup(
				StopIntake(intake),
				RaiseIntake(intake),
				WaitUntilCommand { drive.pose.position.x >= 8.0 },
				MoveLiftTo(Lift.Position.LOW, Robot.lift),
				ScoreDeposit(Robot.deposit, false),
				MoveLiftTo(200, Robot.lift),
				WaitUntilCommand { drive.pose.position.x >= 53.0 },
				WaitCommand(500),
				DropOnePixel(puncher),
				WaitCommand(500),
				DropPixels(puncher)
			)
		),

		ActionCommand(path.extras[0]),

		// zero lift

		MoveLiftTo(Lift.Position.LOW.ticks, Robot.lift),
		TransferDeposit(Robot.deposit, false),
		WaitCommand(250),
		UnsafeLiftZero(Robot.lift),

		// start: cycle

		ParallelCommandGroup(
			ActionCommand(path.extras[1]),
			DropPixels(puncher),
			SequentialCommandGroup(
				WaitUntilCommand { drive.pose.position.x <= 0.0 },
				MoveLiftTo(Lift.Position.INTAKE, Robot.lift)
			),
		),

		IntakeToStackHeight(2, intake),
		IntakeIn(intake) { 1.0 },

		WaitCommand(2000),

		MoveLiftTo(Lift.Position.ZERO, Robot.lift),

		TransferDeposit(Robot.deposit, false),
		PunchPixels(puncher),

		ParallelCommandGroup(
			ActionCommand(path.extras[2]),
			SequentialCommandGroup(
				StopIntake(intake),
				RaiseIntake(intake),
				WaitUntilCommand { drive.pose.position.x >= 8.0 },
				MoveLiftTo(Lift.Position.MID, Robot.lift),
				ScoreDeposit(Robot.deposit, false),
				WaitUntilCommand { drive.pose.position.x >= 52.0 },
				WaitCommand(500),
				DropOnePixel(puncher),
				WaitCommand(500),
				DropPixels(puncher)
			)
		),

		// end cycle

		// zero left
		TransferDeposit(Robot.deposit, false),
		WaitCommand(350),
		UnsafeLiftZero(Robot.lift),

		ActionCommand(path.park)
	)
}
