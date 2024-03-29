package org.firstinspires.ftc.teamcode.hardware.subsystem.rework.lift.commands

import com.arcrobotics.ftclib.command.CommandBase
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.lift.Lift

class TargetCommand(val ticks: Int, private val lift: Lift) : CommandBase() {
    constructor(position: Lift.Position, lift: Lift) : this(position.ticks, lift)

    init { addRequirements(lift) }

    override fun initialize() = lift.target(ticks)
}