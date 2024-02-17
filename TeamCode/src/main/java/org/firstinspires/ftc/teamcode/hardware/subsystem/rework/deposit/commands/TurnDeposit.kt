package org.firstinspires.ftc.teamcode.hardware.subsystem.rework.deposit.commands

import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.deposit.Deposit

class TurnDeposit(angle: Double, deposit: Deposit) : MoveDeposit(Deposit.State(deposit.state.vertical, angle), deposit)