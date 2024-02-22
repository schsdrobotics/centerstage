package org.firstinspires.ftc.teamcode.hardware.cycles

import com.arcrobotics.ftclib.command.SequentialCommandGroup
import com.arcrobotics.ftclib.command.WaitCommand
import org.firstinspires.ftc.teamcode.hardware.Robot
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.intake.commands.IntakeIn
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.lift.Lift
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.lift.commands.MoveLiftTo

class IntakeCycle(duration: Long) : SequentialCommandGroup(
	MoveLiftTo(Lift.Position.INTAKE, Robot.lift),
	IntakeIn(Robot.intake) { 1.0 },
	WaitCommand(duration)
)