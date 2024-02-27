package org.firstinspires.ftc.teamcode.hardware.subsystem.rework.lift.commands

import com.arcrobotics.ftclib.command.SequentialCommandGroup
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.lift.Lift
import kotlin.math.abs

class MoveLiftTo(ticks: Int, val lift: Lift) : SequentialCommandGroup() {
    constructor(position: Lift.Position, lift: Lift) : this(position.ticks, lift)
    constructor(lift: Lift) : this(lift.position, lift)

    init {
        addCommands(TargetCommand(ticks, lift), GoCommand(lift))

        addRequirements(lift)
    }

    override fun isFinished() = abs(lift.target - lift.position) <= 10
}