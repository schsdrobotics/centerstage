package org.firstinspires.ftc.teamcode.hardware.cycles

import com.arcrobotics.ftclib.command.InstantCommand
import com.arcrobotics.ftclib.command.ParallelCommandGroup
import com.arcrobotics.ftclib.command.SequentialCommandGroup
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.deposit.Deposit
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.deposit.commands.AlignDeposit
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.deposit.commands.ScoreDeposit
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.deposit.commands.TransferDeposit
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.lift.Lift
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.lift.TargetGoCommand

class LiftTo(position: Lift.Position, lift: Lift, deposit: Deposit) : InstantCommand({ to(position, lift, deposit).schedule() }) {
	companion object {
		fun to(target: Lift.Position, lift: Lift, deposit: Deposit) = when {
			// default case: at zero, target non-zero
			// align, go, score
			lift.atZero && target != Lift.Position.ZERO -> {
				SequentialCommandGroup(
					AlignDeposit(deposit),
					TargetGoCommand(target, lift),
					ScoreDeposit(deposit)
				)
			}

			// adjustment case: lift not zeroed, target isn't zero
			// go + score
			lift.position >= Lift.Position.CLEAR.ticks && target != Lift.Position.ZERO -> {
				SequentialCommandGroup(
					TargetGoCommand(target, lift),
					ScoreDeposit(deposit)
				)
			}

			// target is zero, and aligned
			target == Lift.Position.ZERO && deposit.state.vertical == Deposit.ALIGN_ANGLE -> {
				SequentialCommandGroup(
					AlignDeposit(deposit),
					TargetGoCommand(Lift.Position.ZERO, lift),
					TransferDeposit(deposit)
				)
			}

			// restorative case: target is zero
			// ensure lift is high enough before dropping
			// align, wait 300ms, (go + transfer)
			target == Lift.Position.ZERO && deposit.state.vertical == Deposit.SCORE_ANGLE -> {
				SequentialCommandGroup(
					if (!lift.cleared) TargetGoCommand(300, lift) else InstantCommand(),
					AlignDeposit(deposit),
					ParallelCommandGroup(
						TargetGoCommand(Lift.Position.ZERO, lift),
						TransferDeposit(deposit)
					)
				)
			}

			// base case
			else -> {
				SequentialCommandGroup(
					TargetGoCommand(target, lift),
					ScoreDeposit(deposit)
				)
			}
		}
	}
}