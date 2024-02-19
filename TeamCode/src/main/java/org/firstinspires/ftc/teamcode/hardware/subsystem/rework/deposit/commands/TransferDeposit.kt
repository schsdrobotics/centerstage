package org.firstinspires.ftc.teamcode.hardware.subsystem.rework.deposit.commands

import org.firstinspires.ftc.teamcode.hardware.Robot.DepositHardware.Configuration.TRANSFER_ANGLE
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.deposit.Deposit

class TransferDeposit(deposit: Deposit) : FlipDeposit(TRANSFER_ANGLE, deposit)