package org.firstinspires.ftc.teamcode.hardware.subsystem.rework.intake

import com.arcrobotics.ftclib.command.CommandBase

class ReverseCommand(private val intake: Intake, val power: () -> Double) : CommandBase() {
    init { addRequirements(intake) }

    override fun initialize() = intake.reverse(power())

    override fun isFinished() = true
}