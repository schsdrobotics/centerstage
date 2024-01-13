package org.firstinspires.ftc.teamcode.hardware.subsystem.rework.lift

import com.arcrobotics.ftclib.command.SequentialCommandGroup

class TargetGoCommand(ticks: Int, val lift: Lift) : SequentialCommandGroup() {
    constructor(position: Lift.Position, lift: Lift) : this(position.ticks, lift)

    init {
        addCommands(TargetCommand(ticks, lift), GoCommand(lift))

        addRequirements(lift)
    }
}