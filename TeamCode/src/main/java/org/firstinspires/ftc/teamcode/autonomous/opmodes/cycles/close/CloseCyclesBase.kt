package org.firstinspires.ftc.teamcode.autonomous.opmodes.cycles.close

import com.arcrobotics.ftclib.command.ParallelCommandGroup
import com.arcrobotics.ftclib.command.SequentialCommandGroup
import com.arcrobotics.ftclib.command.WaitCommand
import org.firstinspires.ftc.teamcode.autonomous.framework.AutonomousOpMode
import org.firstinspires.ftc.teamcode.autonomous.framework.Side
import org.firstinspires.ftc.teamcode.autonomous.framework.Alliance
import org.firstinspires.ftc.teamcode.autonomous.framework.Cycle
import org.firstinspires.ftc.teamcode.hardware.Robot.IntakeHardware.Configuration.DOWN_ANGLE
import org.firstinspires.ftc.teamcode.hardware.Robot.intake
import org.firstinspires.ftc.teamcode.hardware.Robot.lift
import org.firstinspires.ftc.teamcode.hardware.Robot.puncher
import org.firstinspires.ftc.teamcode.hardware.cycles.IntakeCycle
import org.firstinspires.ftc.teamcode.hardware.cycles.UnsafeLiftZero
import org.firstinspires.ftc.teamcode.hardware.cycles.UnsafeScore
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.ActionCommand
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.intake.commands.DropIntake
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.intake.commands.RaiseIntake
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.intake.commands.StopIntake
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.lift.Lift
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.lift.commands.MoveLiftTo
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.puncher.commands.PunchPixels

open class CloseCyclesBase(side: Alliance, position: Side) : AutonomousOpMode(side, position) {
	override fun first() {
		intake.target = DOWN_ANGLE; intake.periodic()
	}

	val preloads by lazy {
		SequentialCommandGroup(
			DropIntake(intake),
			ActionCommand(path.purple),
			RaiseIntake(intake),

			ActionCommand(path.yellow),
			UnsafeScore(lift, Lift.Position.LOW)
		)
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

			UnsafeScore(lift, Lift.Position.MID),

			UnsafeLiftZero(lift)
		)
	}

	val initial by lazy { generate(path.cycles.initial, 5) }
	val rest by lazy { generate(path.cycles.rest, 3) }

	override fun actions() =
		preloads
			.andThen(initial)
//			.andThen(rest)
//			.andThen(ActionCommand(path.park))
}