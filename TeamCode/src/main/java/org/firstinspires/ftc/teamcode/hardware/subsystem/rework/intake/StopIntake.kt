package org.firstinspires.ftc.teamcode.hardware.subsystem.rework.intake

import com.arcrobotics.ftclib.command.CommandBase

class StopIntake(private val intake: Intake) : CommandBase() {
    init { addRequirements(intake) }

    override fun initialize() = intake.stop()

    override fun isFinished() = true
}