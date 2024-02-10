package org.firstinspires.ftc.teamcode.autonomous.opmodes.cycles

import com.arcrobotics.ftclib.command.SequentialCommandGroup
import org.firstinspires.ftc.teamcode.autonomous.framework.AutonomousOpMode
import org.firstinspires.ftc.teamcode.autonomous.framework.AutonomousPosition
import org.firstinspires.ftc.teamcode.autonomous.framework.AutonomousSide
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.ActionCommand

open class CyclesBase(side: AutonomousSide, position: AutonomousPosition) : AutonomousOpMode(side, position) {
    override fun actions() = SequentialCommandGroup(
            ActionCommand(path.purple),
            ActionCommand(path.yellow),
            ActionCommand(path.cycles.initial.backstage),
            ActionCommand(path.cycles.initial.stacks),

            ActionCommand(path.cycles.rest.backstage),
            ActionCommand(path.cycles.rest.stacks),

            ActionCommand(path.cycles.rest.backstage),
            ActionCommand(path.cycles.rest.stacks),

            ActionCommand(path.cycles.rest.backstage),
            ActionCommand(path.cycles.rest.stacks),

            ActionCommand(path.cycles.rest.backstage),
            ActionCommand(path.cycles.rest.stacks),
    )
}