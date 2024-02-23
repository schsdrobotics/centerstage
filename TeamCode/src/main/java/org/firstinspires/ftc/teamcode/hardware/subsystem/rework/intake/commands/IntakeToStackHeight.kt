package org.firstinspires.ftc.teamcode.hardware.subsystem.rework.intake.commands

import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.intake.Intake

class IntakeToStackHeight(var height: Int, var intake: Intake) : IntakeTo(when (height) {
	5 -> 33.0
	4 -> 28.0
	3 -> 20.0
	2 -> 16.0
	1 -> 10.0

	else -> ((5.8 * height) + 4)
}, intake)