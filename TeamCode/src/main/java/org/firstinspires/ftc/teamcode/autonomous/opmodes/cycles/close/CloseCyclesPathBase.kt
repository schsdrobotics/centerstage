package org.firstinspires.ftc.teamcode.autonomous.opmodes.cycles.close

import com.arcrobotics.ftclib.command.Command
import com.arcrobotics.ftclib.command.SequentialCommandGroup
import com.arcrobotics.ftclib.command.WaitCommand
import org.firstinspires.ftc.teamcode.autonomous.framework.AutonomousOpMode
import org.firstinspires.ftc.teamcode.autonomous.framework.Side
import org.firstinspires.ftc.teamcode.autonomous.framework.Alliance
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.ActionCommand

open class CloseCyclesPathBase(side: Alliance, position: Side) : AutonomousOpMode(side, position) {
	override fun first() {}

	val preloads by lazy {
		SequentialCommandGroup(
			ActionCommand(path.purple),
			WaitCommand(3000),
			ActionCommand(path.yellow)
		)
	}

	val initial by lazy {
		SequentialCommandGroup(
			ActionCommand(path.cycles.initial.stacks),
			WaitCommand(3000),
			ActionCommand(path.cycles.initial.backstage),
			WaitCommand(3000),
		)
	}

	val rest by lazy {
		SequentialCommandGroup(
			ActionCommand(path.cycles.rest.stacks),
			WaitCommand(3000),
			ActionCommand(path.cycles.rest.backstage),
			WaitCommand(3000),
		)
	}

	override fun actions(): Command =
		preloads
			.andThen(initial)
			.andThen(rest)
			.andThen(ActionCommand(path.park))
}