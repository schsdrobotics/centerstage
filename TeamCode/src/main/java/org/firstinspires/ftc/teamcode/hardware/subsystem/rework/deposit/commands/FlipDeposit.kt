package org.firstinspires.ftc.teamcode.hardware.subsystem.rework.deposit.commands

import org.firstinspires.ftc.teamcode.hardware.Robot.DepositHardware.Configuration.HORIZONTAL_OFFSET
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.deposit.Deposit

open class FlipDeposit(angle: Double, deposit: Deposit, shouldCheck: Boolean) : MoveDeposit(Deposit.State(angle, HORIZONTAL_OFFSET), deposit, shouldCheck)