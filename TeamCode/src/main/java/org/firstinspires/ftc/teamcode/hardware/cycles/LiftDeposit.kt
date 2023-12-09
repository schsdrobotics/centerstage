package org.firstinspires.ftc.teamcode.hardware.cycles

import org.mercurialftc.mercurialftc.scheduler.commands.SequentialCommandGroup

object LiftDeposit {
    // lift: LiftSubsystem, wrist: WristSubsystem, deposit: DepositSubsystem
    fun command() = SequentialCommandGroup()
        .addCommands()
}