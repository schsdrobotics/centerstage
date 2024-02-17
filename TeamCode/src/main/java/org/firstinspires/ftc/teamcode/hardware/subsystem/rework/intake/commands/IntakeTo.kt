package org.firstinspires.ftc.teamcode.hardware.subsystem.rework.intake.commands

import com.arcrobotics.ftclib.command.CommandBase
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.intake.Intake

open class IntakeTo(val position: Double, private val intake: Intake) : CommandBase() {
    init { addRequirements(intake) }

    override fun initialize() = intake.target(position)

    override fun isFinished() = true
}