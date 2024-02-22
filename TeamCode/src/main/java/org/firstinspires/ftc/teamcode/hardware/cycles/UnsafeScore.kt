package org.firstinspires.ftc.teamcode.hardware.cycles

import com.arcrobotics.ftclib.command.SequentialCommandGroup
import com.arcrobotics.ftclib.command.WaitCommand
import org.firstinspires.ftc.teamcode.hardware.Robot
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.deposit.commands.ScoreDeposit
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.lift.Lift
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.lift.commands.MoveLiftTo
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.puncher.commands.DropPixels

class UnsafeScore(position: Lift.Position, pause: Long = 250) : SequentialCommandGroup(
	MoveLiftTo(position, Robot.lift),
	ScoreDeposit(Robot.deposit, false),
	WaitCommand(pause),
	DropPixels(Robot.puncher),
	WaitCommand(pause),
)