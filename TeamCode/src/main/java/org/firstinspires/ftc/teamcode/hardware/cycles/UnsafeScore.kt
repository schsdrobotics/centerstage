package org.firstinspires.ftc.teamcode.hardware.cycles

import com.acmerobotics.roadrunner.Vector2d
import com.arcrobotics.ftclib.command.SequentialCommandGroup
import com.arcrobotics.ftclib.command.WaitCommand
import org.firstinspires.ftc.teamcode.hardware.Robot
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.deposit.commands.ScoreDeposit
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.drive.Adjust
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.lift.Lift
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.lift.commands.MoveLiftTo
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.puncher.commands.DropOnePixel
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.puncher.commands.DropPixels
import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive

class UnsafeScore(lift: Lift, position: Int, drive: MecanumDrive, pause: Long = 250) : SequentialCommandGroup(
	MoveLiftTo(position, lift),
	ScoreDeposit(Robot.deposit, false),
	WaitCommand(pause),
	DropOnePixel(Robot.puncher),
	WaitCommand(pause),
	Adjust(Vector2d(-0.4, 0.0), 0.0, { Robot.Hubs.current() >= 5.5 }, drive),
	DropPixels(Robot.puncher),
	WaitCommand(pause),
	Adjust(Vector2d(0.4, 0.0), 0.0, { true }, drive),
	WaitCommand(pause),
	Adjust(Vector2d(0.0, 0.0), 0.0, { true }, drive),
) {
	constructor(lift: Lift, position: Lift.Position, drive: MecanumDrive, pause: Long = 250) : this(lift, position.ticks, drive, pause)
}