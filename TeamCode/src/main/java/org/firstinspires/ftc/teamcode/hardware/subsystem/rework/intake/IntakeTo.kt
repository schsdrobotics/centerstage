package org.firstinspires.ftc.teamcode.hardware.subsystem.rework.intake

import com.arcrobotics.ftclib.command.CommandBase

open class IntakeTo(val position: Double, private val intake: Intake) : CommandBase() {
    init { addRequirements(intake) }

    override fun initialize() = intake.to(position)

    override fun isFinished() = true
}