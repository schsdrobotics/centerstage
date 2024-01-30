package org.firstinspires.ftc.teamcode.hardware.subsystem.rework.drive

import com.arcrobotics.ftclib.command.CommandBase
import com.arcrobotics.ftclib.command.InstantCommand

class ResetYawCommand(private val drive: Drive) : InstantCommand({ drive.reset() })