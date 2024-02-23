package org.firstinspires.ftc.teamcode.hardware.subsystem.rework.deposit.commands

import com.arcrobotics.ftclib.command.InstantCommand
import org.firstinspires.ftc.teamcode.hardware.Robot
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.deposit.Deposit

class AdjustDeposit(angle: Double, deposit: Deposit) : InstantCommand({ Robot.DepositHardware.Configuration.VERTICAL_OFFSET += angle })