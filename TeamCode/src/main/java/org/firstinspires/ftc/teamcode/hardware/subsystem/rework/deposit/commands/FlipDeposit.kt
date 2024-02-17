package org.firstinspires.ftc.teamcode.hardware.subsystem.rework.deposit.commands

import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.deposit.Deposit

open class FlipDeposit(angle: Double, deposit: Deposit) : MoveDeposit(Deposit.State(angle, deposit.state.horizontal), deposit)