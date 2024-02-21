package org.firstinspires.ftc.teamcode.hardware.subsystem.rework.deposit.commands

import org.firstinspires.ftc.teamcode.hardware.Robot.DepositHardware.Configuration.SCORE_ANGLE
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.deposit.Deposit

class ScoreDeposit(deposit: Deposit, shouldCheck: Boolean
= true) : FlipDeposit(SCORE_ANGLE, deposit, shouldCheck)