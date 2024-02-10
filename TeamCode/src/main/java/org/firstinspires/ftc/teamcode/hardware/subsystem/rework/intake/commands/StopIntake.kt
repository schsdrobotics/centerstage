package org.firstinspires.ftc.teamcode.hardware.subsystem.rework.intake.commands

import com.arcrobotics.ftclib.command.CommandBase
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.intake.Intake

class StopIntake(private val intake: Intake) : CommandBase() {
    init { addRequirements(intake) }

    override fun initialize() = intake.stop()

    override fun isFinished() = true
}