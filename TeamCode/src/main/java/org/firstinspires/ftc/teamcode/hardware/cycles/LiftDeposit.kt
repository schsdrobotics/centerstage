package org.firstinspires.ftc.teamcode.hardware.cycles

import org.firstinspires.ftc.teamcode.util.sugar.WaitCommand
import org.mercurialftc.mercurialftc.scheduler.commands.SequentialCommandGroup

object LiftDeposit {
    fun command() = SequentialCommandGroup()
        .addCommands(WaitCommand(500))
}