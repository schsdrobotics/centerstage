package org.firstinspires.ftc.teamcode.hardware.subsystem.rework.intake

import com.arcrobotics.ftclib.command.CommandBase

class ForwardCommand(private val intake: Intake) : CommandBase() {
    init { addRequirements(intake) }

    override fun initialize() = intake.forward()

    override fun isFinished() = true
}