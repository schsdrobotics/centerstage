package org.firstinspires.ftc.teamcode.autonomous.opmodes.cycles.close

import com.arcrobotics.ftclib.command.ParallelCommandGroup
import com.arcrobotics.ftclib.command.SequentialCommandGroup
import org.firstinspires.ftc.teamcode.autonomous.framework.AutonomousOpMode
import org.firstinspires.ftc.teamcode.autonomous.framework.AutonomousPosition
import org.firstinspires.ftc.teamcode.autonomous.framework.AutonomousSide
import org.firstinspires.ftc.teamcode.hardware.Robot.IntakeHardware.Configuration.DOWN_ANGLE
import org.firstinspires.ftc.teamcode.hardware.Robot.intake
import org.firstinspires.ftc.teamcode.hardware.Robot.lift
import org.firstinspires.ftc.teamcode.hardware.Robot.puncher
import org.firstinspires.ftc.teamcode.hardware.cycles.IntakeCycle
import org.firstinspires.ftc.teamcode.hardware.cycles.SweepIntake
import org.firstinspires.ftc.teamcode.hardware.cycles.UnsafeLiftZero
import org.firstinspires.ftc.teamcode.hardware.cycles.UnsafeScore
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.ActionCommand
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.intake.commands.DropIntake
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.intake.commands.RaiseIntake
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.intake.commands.StopIntake
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.lift.Lift
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.lift.commands.MoveLiftTo
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.puncher.commands.PunchPixels

open class CloseCyclesBase(side: AutonomousSide, position: AutonomousPosition) : AutonomousOpMode(side, position) {
	override fun first() {
		intake.target = DOWN_ANGLE; intake.periodic()
	}

	val preloads = SequentialCommandGroup(
		DropIntake(intake),
		ActionCommand(path.purple),
		RaiseIntake(intake),

		ActionCommand(path.yellow),
		UnsafeScore(Lift.Position.LOW)
	)

	val initial = SequentialCommandGroup(
		ActionCommand(path.cycles.initial.stacks)
			.alongWith(UnsafeLiftZero()),

		SweepIntake(6, 4) { 150 },

		IntakeCycle(1500),

		ParallelCommandGroup(
			ActionCommand(path.cycles.initial.backstage),
			SequentialCommandGroup(
				StopIntake(intake),

				RaiseIntake(intake)
					.alongWith(PunchPixels(puncher)),

				MoveLiftTo(Lift.Position.ZERO, lift)
			),
		),

		UnsafeScore(Lift.Position.MID),
		UnsafeLiftZero()
	)

	val rest = SequentialCommandGroup(
		ActionCommand(path.cycles.rest.stacks)
			.alongWith(UnsafeLiftZero()),

		SweepIntake(4, 2) { 150 },

		IntakeCycle(1500),

		ParallelCommandGroup(
			ActionCommand(path.cycles.rest.backstage),
			SequentialCommandGroup(
				StopIntake(intake),

				RaiseIntake(intake)
					.alongWith(PunchPixels(puncher)),

				MoveLiftTo(Lift.Position.ZERO, lift)
			),
		),

		UnsafeScore(Lift.Position.MID),
		UnsafeLiftZero()
	)

	override fun actions() =
		preloads
			.andThen(initial)
			.andThen(rest)
			.andThen(ActionCommand(path.park))
}