package org.firstinspires.ftc.teamcode.hardware.subsystem.rework.intake.commands

import com.arcrobotics.ftclib.command.CommandBase
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.intake.Intake

class IntakeOut(private val intake: Intake, val power: () -> Double) : CommandBase() {
    init { addRequirements(intake) }

    override fun initialize() = intake.reverse(power())

    override fun isFinished() = true
}