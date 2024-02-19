package org.firstinspires.ftc.teamcode.hardware.subsystem.rework.lift.commands

import com.arcrobotics.ftclib.command.CommandBase
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.lift.Lift

class LiftStopCommand(private val lift: Lift) : CommandBase() {
    init { addRequirements(lift) }

    override fun initialize() = lift.stop()

    override fun isFinished() = true
}