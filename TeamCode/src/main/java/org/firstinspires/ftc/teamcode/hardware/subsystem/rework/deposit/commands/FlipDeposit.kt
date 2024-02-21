package org.firstinspires.ftc.teamcode.hardware.subsystem.rework.deposit.commands

import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.deposit.Deposit

open class FlipDeposit(angle: Double, deposit: Deposit, shouldCheck: Boolean) : MoveDeposit(Deposit.State(angle, 0.0), deposit, shouldCheck)