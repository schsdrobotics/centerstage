package org.firstinspires.ftc.teamcode.hardware.subsystem.rework.intake.commands

import com.arcrobotics.ftclib.command.CommandBase
import org.firstinspires.ftc.teamcode.hardware.Robot.IntakeHardware.Configuration.DOWN_ANGLE
import org.firstinspires.ftc.teamcode.hardware.Robot.IntakeHardware.Configuration.UP_ANGLE
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.intake.Intake

class IntakeCycle(private val intake: Intake) : CommandBase() {
    init { addRequirements(intake) }

    override fun initialize() {
        intake.target = when (intake.target) {
            UP_ANGLE -> DOWN_ANGLE
            DOWN_ANGLE -> UP_ANGLE
            else -> (UP_ANGLE + DOWN_ANGLE) / 2.0
        }
    }

    override fun isFinished() = true
}