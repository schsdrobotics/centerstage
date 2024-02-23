package org.firstinspires.ftc.teamcode.autonomous.opmodes.cycles.close

import com.arcrobotics.ftclib.command.ParallelCommandGroup
import com.arcrobotics.ftclib.command.SequentialCommandGroup
import com.arcrobotics.ftclib.command.WaitCommand
import org.firstinspires.ftc.teamcode.autonomous.framework.Alliance
import org.firstinspires.ftc.teamcode.autonomous.framework.AutonomousOpMode
import org.firstinspires.ftc.teamcode.autonomous.framework.Cycle
import org.firstinspires.ftc.teamcode.autonomous.framework.Side
import org.firstinspires.ftc.teamcode.hardware.Robot.deposit
import org.firstinspires.ftc.teamcode.hardware.Robot.intake
import org.firstinspires.ftc.teamcode.hardware.Robot.lift
import org.firstinspires.ftc.teamcode.hardware.Robot.puncher
import org.firstinspires.ftc.teamcode.hardware.cycles.IntakeCycle
import org.firstinspires.ftc.teamcode.hardware.cycles.UnsafeLiftZero
import org.firstinspires.ftc.teamcode.hardware.cycles.UnsafeScore
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.ActionCommand
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.deposit.commands.ScoreDeposit
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.deposit.commands.TransferDeposit
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.intake.commands.IntakeOut
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.intake.commands.IntakeTo
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.intake.commands.RaiseIntake
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.intake.commands.StopIntake
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.lift.Lift
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.lift.commands.MoveLiftTo
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.puncher.commands.DropOnePixel
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.puncher.commands.DropPixels
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.puncher.commands.PunchPixels

open class CloseCyclesBase(side: Alliance, position: Side) : AutonomousOpMode(side, position) {
	override fun first() {
		intake.target = 35.0; intake.periodic()
		PunchPixels(puncher).initialize()
	}

	val preloads by lazy {

	}

	fun generate(cycle: Cycle, n: Int): SequentialCommandGroup {
		return SequentialCommandGroup(
			ActionCommand(cycle.stacks)
				.alongWith(UnsafeLiftZero(lift)),

			IntakeCycle(intake, lift, n, 500),

			ParallelCommandGroup(
				ActionCommand(cycle.backstage),

				SequentialCommandGroup(
					WaitCommand(750),
					StopIntake(intake)
						.andThen(RaiseIntake(intake))
						.alongWith(SequentialCommandGroup(
							MoveLiftTo(Lift.Position.ZERO, lift),
							PunchPixels(puncher)
						))
				),
			),

			UnsafeScore(lift, Lift.Position.MID, drive),

			UnsafeLiftZero(lift)
		)
	}

	val initial by lazy { generate(path.cycles.initial, 5) }
	val rest by lazy { generate(path.cycles.rest, 3) }

	override fun actions() = SequentialCommandGroup(
		IntakeTo(20.0, intake),

		ActionCommand(path.purple),
		IntakeOut(intake) { 0.7 },
		WaitCommand(750),
		StopIntake(intake),

		ParallelCommandGroup(
			SequentialCommandGroup(
				TransferDeposit(deposit, false),
				PunchPixels(puncher)
			)
		),

		MoveLiftTo(Lift.Position.LOW, lift),
		ScoreDeposit(deposit, false),
		WaitCommand(150),
		DropOnePixel(puncher),

		ActionCommand(path.yellow),

		DropPixels(puncher)
	)
		.andThen(ParallelCommandGroup(
			RaiseIntake(intake),

			SequentialCommandGroup(
				TransferDeposit(deposit, false),
				WaitCommand(500),
				UnsafeLiftZero(lift)
			),

			ActionCommand(path.park)
		))

}