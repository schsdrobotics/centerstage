package org.firstinspires.ftc.teamcode.hardware.subsystem.rework.deposit.commands

import com.arcrobotics.ftclib.command.InstantCommand
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.deposit.Deposit

class UnlockDeposit(deposit: Deposit) : InstantCommand({ deposit.sad() })