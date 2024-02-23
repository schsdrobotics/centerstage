package org.firstinspires.ftc.teamcode.hardware.cycles

import com.arcrobotics.ftclib.command.ParallelCommandGroup
import com.arcrobotics.ftclib.command.SequentialCommandGroup
import com.arcrobotics.ftclib.command.WaitCommand
import org.firstinspires.ftc.teamcode.hardware.Robot
import org.firstinspires.ftc.teamcode.hardware.Robot.deposit
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.deposit.commands.TransferDeposit
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.intake.Intake
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.intake.commands.IntakeIn
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.intake.commands.IntakeToStackHeight
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.lift.Lift
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.lift.commands.MoveLiftTo

class IntakeCycle(intake: Intake, lift: Lift, height: Int, duration: Long) : SequentialCommandGroup(
	MoveLiftTo(Lift.Position.INTAKE.ticks + 50, lift),
	IntakeIn(intake) { 1.0 },
	ParallelCommandGroup(
		TransferDeposit(deposit, false),

		SequentialCommandGroup(
			SequentialCommandGroup(
				IntakeToStackHeight(height, Robot.intake),
				WaitCommand(500)
			),

			SequentialCommandGroup(
				IntakeToStackHeight(height - 1, Robot.intake),
				WaitCommand(500)
			),
		),

		WaitCommand(duration)
	),
)