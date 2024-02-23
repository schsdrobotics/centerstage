package org.firstinspires.ftc.teamcode.hardware.cycles

import com.arcrobotics.ftclib.command.SequentialCommandGroup
import com.arcrobotics.ftclib.command.WaitCommand
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.intake.Intake
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.intake.commands.IntakeToStackHeight

class SweepIntake(intake: Intake, first: Int, last: Int, duration: (Int) -> Long) : SequentialCommandGroup(
	*(first..last).map {
		SequentialCommandGroup(
			IntakeToStackHeight(it, intake),
			WaitCommand(duration(it))
		)
	}.toTypedArray()
)