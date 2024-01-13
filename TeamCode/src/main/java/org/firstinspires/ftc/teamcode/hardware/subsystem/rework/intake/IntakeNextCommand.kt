package org.firstinspires.ftc.teamcode.hardware.subsystem.rework.intake

import com.arcrobotics.ftclib.command.CommandBase

class IntakeNextCommand(private val intake: Intake) : CommandBase() {
    init { addRequirements(intake) }

    override fun initialize() = intake.next()

    override fun isFinished() = true
}