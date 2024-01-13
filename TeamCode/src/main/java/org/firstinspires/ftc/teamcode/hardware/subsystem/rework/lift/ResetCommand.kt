package org.firstinspires.ftc.teamcode.hardware.subsystem.rework.lift

import com.arcrobotics.ftclib.command.CommandBase

class ResetCommand(private val lift: Lift) : CommandBase() {
    init { addRequirements(lift) }

    override fun initialize() = lift.reset()

    override fun isFinished() = true
}