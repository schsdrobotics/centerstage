package org.firstinspires.ftc.teamcode.autonomous.opmodes.cycles.far

import com.arcrobotics.ftclib.command.ParallelCommandGroup
import com.arcrobotics.ftclib.command.SequentialCommandGroup
import com.arcrobotics.ftclib.command.WaitCommand
import org.firstinspires.ftc.teamcode.autonomous.framework.Alliance
import org.firstinspires.ftc.teamcode.autonomous.framework.AutonomousOpMode
import org.firstinspires.ftc.teamcode.autonomous.framework.Side
import org.firstinspires.ftc.teamcode.hardware.Robot
import org.firstinspires.ftc.teamcode.hardware.cycles.IntakeCycle
import org.firstinspires.ftc.teamcode.hardware.cycles.UnsafeLiftZero
import org.firstinspires.ftc.teamcode.hardware.cycles.UnsafeScore
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.ActionCommand
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.deposit.commands.TransferDeposit
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.intake.commands.IntakeOut
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.intake.commands.IntakeTo
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.intake.commands.RaiseIntake
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.intake.commands.StopIntake
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.lift.Lift
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.lift.commands.MoveLiftTo
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.puncher.commands.PunchPixels

open class FarCyclesBase(side: Alliance, position: Side) : AutonomousOpMode(side, position) {
	override fun first() {
		Robot.intake.target = 35.0; Robot.intake.periodic()
		PunchPixels(Robot.puncher).initialize()
	}

	val preloads by lazy {
		SequentialCommandGroup(
			IntakeTo(20.0, Robot.intake),

			ActionCommand(path.purple),
			IntakeOut(Robot.intake) { 0.65 },
			WaitCommand(750),
			StopIntake(Robot.intake),

			ParallelCommandGroup(
				SequentialCommandGroup(
					TransferDeposit(Robot.deposit, false),
					PunchPixels(Robot.puncher)
				)
			)
		)
	}

	override fun actions() = SequentialCommandGroup(
		preloads,

		SequentialCommandGroup(
			ActionCommand(path.cycles.initial.stacks)
				.alongWith(UnsafeLiftZero(Robot.lift)),

			IntakeCycle(Robot.intake, Robot.lift, 6, 500),

			ParallelCommandGroup(
				ActionCommand(path.cycles.initial.backstage),

				SequentialCommandGroup(
					WaitCommand(750),
					StopIntake(Robot.intake)
						.andThen(RaiseIntake(Robot.intake))
						.alongWith(SequentialCommandGroup(
							MoveLiftTo(Lift.Position.ZERO, Robot.lift),
							PunchPixels(Robot.puncher)
						))
				),
			),

			UnsafeScore(Robot.lift, Lift.Position.MID, drive),

			UnsafeLiftZero(Robot.lift),

			ActionCommand(path.extra)
		)
	)
}