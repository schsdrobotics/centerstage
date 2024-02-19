package org.firstinspires.ftc.teamcode.hardware.subsystem.rework.deposit.commands

import org.firstinspires.ftc.teamcode.hardware.Robot.DepositHardware.Configuration.ALIGN_ANGLE
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.deposit.Deposit

class AlignDeposit(deposit: Deposit) : FlipDeposit(ALIGN_ANGLE, deposit)