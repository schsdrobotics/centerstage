package org.firstinspires.ftc.teamcode.hardware.subsystem.rework.intake.commands

import com.arcrobotics.ftclib.command.CommandBase
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.intake.Intake

class IntakeCycle(private val intake: Intake) : CommandBase() {
    init { addRequirements(intake) }

    override fun initialize() {
        intake.target = when (intake.target) {
            Intake.UP_ANGLE -> Intake.DOWN_ANGLE
            Intake.DOWN_ANGLE -> Intake.UP_ANGLE
            else -> (Intake.UP_ANGLE + Intake.DOWN_ANGLE) / 2.0
        }
    }

    override fun isFinished() = true
}