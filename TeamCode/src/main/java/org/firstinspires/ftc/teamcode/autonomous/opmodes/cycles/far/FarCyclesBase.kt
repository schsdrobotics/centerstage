package org.firstinspires.ftc.teamcode.autonomous.opmodes.cycles.far

import com.arcrobotics.ftclib.command.ParallelCommandGroup
import com.arcrobotics.ftclib.command.SequentialCommandGroup
import com.arcrobotics.ftclib.command.WaitCommand
import org.firstinspires.ftc.teamcode.autonomous.framework.Alliance
import org.firstinspires.ftc.teamcode.autonomous.framework.AutonomousOpMode
import org.firstinspires.ftc.teamcode.autonomous.framework.Side
import org.firstinspires.ftc.teamcode.hardware.Robot
import org.firstinspires.ftc.teamcode.hardware.Robot.puncher
import org.firstinspires.ftc.teamcode.hardware.cycles.UnsafeLiftZero
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.ActionCommand
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.deposit.commands.ScoreDeposit
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.deposit.commands.TransferDeposit
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.intake.commands.IntakeOut
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.intake.commands.IntakeTo
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.intake.commands.StopIntake
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.lift.Lift
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.lift.commands.MoveLiftTo
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.puncher.commands.DropPixels
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.puncher.commands.PunchPixels

open class FarCyclesBase(side: Alliance, position: Side) : AutonomousOpMode(side, position) {
	override fun first() {
		Robot.intake.target = 35.0; Robot.intake.periodic()
		PunchPixels(puncher).initialize()
	}

	val preloads by lazy {
		SequentialCommandGroup(
			IntakeTo(30.0, Robot.intake),

			ActionCommand(path.purple),
			IntakeOut(Robot.intake) { 0.7 },
			WaitCommand(1000),
			StopIntake(Robot.intake),

			TransferDeposit(Robot.deposit, false),
			PunchPixels(puncher)
		)
	}

	override fun actions() = SequentialCommandGroup(
		preloads,

		SequentialCommandGroup(
			ActionCommand(path.cycles.initial.stacks)
				.alongWith(UnsafeLiftZero(Robot.lift)),

			PunchPixels(puncher),

//			ParallelCommandGroup(
//				ActionCommand(path.cycles.initial.backstage),
//
//				SequentialCommandGroup(
//					WaitCommand(750),
//					StopIntake(Robot.intake)
//						.andThen(RaiseIntake(Robot.intake))
//						.alongWith(SequentialCommandGroup(
//							MoveLiftTo(Lift.Position.ZERO, Robot.lift),
//							PunchPixels(puncher),
//							WaitCommand(500),
//							MoveLiftTo(Lift.Position.LOW, Robot.lift),
//							ScoreDeposit(Robot.deposit, false),
//						))
//				),
//			),

			ParallelCommandGroup(
				ActionCommand(path.cycles.initial.backstage),

				SequentialCommandGroup(
					WaitCommand(750),
					MoveLiftTo(Lift.Position.LOW, Robot.lift),
					ScoreDeposit(Robot.deposit, false),
				),
			),

			WaitCommand(250),
			DropPixels(puncher),
			WaitCommand(750),
			TransferDeposit(Robot.deposit, false),
			WaitCommand(350),
			UnsafeLiftZero(Robot.lift),

			ActionCommand(path.extra),
			ActionCommand(path.park)
		)
	)
}