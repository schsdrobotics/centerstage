package org.firstinspires.ftc.teamcode.hardware.subsystem.rework.lift

import com.arcrobotics.ftclib.command.CommandBase

class AdjustCommand(val ticks: Int, private val lift: Lift) : CommandBase() {
    init { addRequirements(lift) }

    override fun initialize() = lift.adjust(ticks)

    override fun isFinished() = true
}