package org.firstinspires.ftc.teamcode.hardware.subsystem.rework.lift.commands

import com.arcrobotics.ftclib.command.CommandBase
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.lift.Lift

class ForcefulLiftAdjustment(val ticks: Int, private val lift: Lift) : CommandBase() {
	init { addRequirements(lift) }

	override fun initialize() = lift.forcefulAdjust(ticks)

	override fun isFinished() = true
}