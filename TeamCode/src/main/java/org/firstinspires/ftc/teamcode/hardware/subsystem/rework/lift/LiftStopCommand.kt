package org.firstinspires.ftc.teamcode.hardware.subsystem.rework.lift

import com.arcrobotics.ftclib.command.CommandBase

class LiftStopCommand(private val lift: Lift) : CommandBase() {
    init { addRequirements(lift) }

    override fun initialize() = lift.stop()

    override fun isFinished() = true
}