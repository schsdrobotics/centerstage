package org.firstinspires.ftc.teamcode.hardware.cycles

import com.arcrobotics.ftclib.command.InstantCommand
import com.arcrobotics.ftclib.command.ParallelCommandGroup
import com.arcrobotics.ftclib.command.SequentialCommandGroup
import com.arcrobotics.ftclib.command.WaitCommand
import org.firstinspires.ftc.teamcode.hardware.Robot.DepositHardware.Configuration.TRANSFER_ANGLE
import org.firstinspires.ftc.teamcode.hardware.Robot.DepositHardware.Configuration.VERTICAL_OFFSET
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.deposit.Deposit
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.deposit.commands.AlignDeposit
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.deposit.commands.ScoreDeposit
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.deposit.commands.TransferDeposit
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.deposit.commands.UnlockDeposit
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.lift.Lift
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.lift.commands.MoveLiftTo

class LiftTo(position: Lift.Position, lift: Lift, deposit: Deposit) : InstantCommand({ to(position, lift, deposit).schedule() }) {
	companion object {
		fun to(target: Lift.Position, lift: Lift, deposit: Deposit) = when {
			// default case: at zero, target non-zero
			// align, go, score
			lift.atZero && target != Lift.Position.ZERO -> {
				SequentialCommandGroup(
					UnlockDeposit(deposit),
					TransferDeposit(deposit, false),
					MoveLiftTo(target, lift),
					ScoreDeposit(deposit)
				)
			}

			// adjustment case: lift not zeroed, target isn't zero
			// go + score
			lift.position >= Lift.Position.CLEAR.ticks && target != Lift.Position.ZERO -> {
				SequentialCommandGroup(
					MoveLiftTo(target, lift),
					ScoreDeposit(deposit)
				)
			}

			// target is zero, and aligned
			target == Lift.Position.ZERO && deposit.state.vertical == (TRANSFER_ANGLE + VERTICAL_OFFSET) -> {
				SequentialCommandGroup(
					UnlockDeposit(deposit),
					ParallelCommandGroup(
						AlignDeposit(deposit, false),
						WaitCommand(250),
						MoveLiftTo(Lift.Position.ZERO, lift),
					),
					TransferDeposit(deposit),
				)
			}

			// restorative case: target is zero
			// ensure lift is high enough before dropping
			// align, wait 300ms, (go + transfer)
			target == Lift.Position.ZERO -> {
				SequentialCommandGroup(
					if (!lift.cleared) MoveLiftTo(Lift.Position.LOW, lift) else InstantCommand(),
					UnlockDeposit(deposit),
					TransferDeposit(deposit),
					ParallelCommandGroup(
						MoveLiftTo(Lift.Position.ZERO, lift),
						TransferDeposit(deposit)
					)
				)
			}

			// base case
			else -> {
				SequentialCommandGroup(
					MoveLiftTo(target, lift),
					ScoreDeposit(deposit)
				)
			}
		}
	}
}