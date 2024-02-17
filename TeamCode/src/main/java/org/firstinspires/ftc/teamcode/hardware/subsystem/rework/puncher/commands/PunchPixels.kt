package org.firstinspires.ftc.teamcode.hardware.subsystem.rework.puncher.commands

import com.arcrobotics.ftclib.command.CommandBase
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.puncher.Puncher

class PunchPixels(private val puncher: Puncher) : CommandBase() {
	init { addRequirements(puncher) }

	override fun initialize() = puncher.to(Puncher.State.TWO)

	override fun isFinished() = true
}