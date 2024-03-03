package org.firstinspires.ftc.teamcode.hardware.subsystem.rework.intake.commands

import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.intake.Intake

class IntakeToStackHeight(var height: Double, var intake: Intake) : IntakeTo(when (height) {
	5.0 -> 33.0
	4.0 -> 28.0
	3.0 -> 20.0
	2.0 -> 16.0
	1.0 -> 10.0

	else -> ((5.8 * height) + 4)
}, intake) {
	constructor(height: Int, intake: Intake) : this(height.toDouble(), intake)
}