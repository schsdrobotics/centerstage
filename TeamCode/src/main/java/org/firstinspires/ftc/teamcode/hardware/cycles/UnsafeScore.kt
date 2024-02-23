package org.firstinspires.ftc.teamcode.hardware.cycles

import com.acmerobotics.roadrunner.Vector2d
import com.arcrobotics.ftclib.command.SequentialCommandGroup
import com.arcrobotics.ftclib.command.WaitCommand
import org.firstinspires.ftc.teamcode.hardware.Robot
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.deposit.commands.ScoreDeposit
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.lift.Lift
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.lift.commands.MoveLiftTo
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.puncher.commands.DropOnePixel
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.puncher.commands.DropPixels
import org.firstinspires.ftc.teamcode.roadrunner.GoToPoint
import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive

class UnsafeScore(lift: Lift, position: Lift.Position, drive: MecanumDrive, pause: Long = 250) : SequentialCommandGroup(
	MoveLiftTo(position, lift),
	ScoreDeposit(Robot.deposit, false),
	WaitCommand(pause),
	DropOnePixel(Robot.puncher),
	WaitCommand(pause),
	GoToPoint(drive, Vector2d(45.0, -17.0)),
	DropPixels(Robot.puncher),
	WaitCommand(pause),
)