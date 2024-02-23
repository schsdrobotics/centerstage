package org.firstinspires.ftc.teamcode.hardware.subsystem.rework.intake.commands

import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.intake.Intake


class IntakeToHeight(height: Double, intake: Intake) : IntakeTo(intake.angleForHeight(height), intake) {
	constructor(height: Int, intake: Intake) : this(height.toDouble(), intake)
}