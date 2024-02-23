package org.firstinspires.ftc.teamcode.hardware.cycles

import com.acmerobotics.roadrunner.PoseVelocity2d
import com.acmerobotics.roadrunner.Vector2d
import com.arcrobotics.ftclib.command.Command
import com.arcrobotics.ftclib.command.CommandBase
import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive

class DriveIntoBackdrop(
	private val linear: Vector2d,
	private val angular: Double,
	private val shouldFinish: () -> Boolean,
	private val drive: MecanumDrive
) : CommandBase() {
	override fun initialize() {}

	override fun execute() = drive.setDrivePowers(PoseVelocity2d(linear, angular))

	override fun whenFinished(toRun: Runnable?): Command {
		drive.setDrivePowers(PoseVelocity2d(Vector2d(0.0, 0.0), 0.0))
		return super.whenFinished(toRun)
	}

	override fun isFinished() = shouldFinish()
}