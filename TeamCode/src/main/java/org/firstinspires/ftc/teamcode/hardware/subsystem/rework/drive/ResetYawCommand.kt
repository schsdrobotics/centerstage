package org.firstinspires.ftc.teamcode.hardware.subsystem.rework.drive

import com.arcrobotics.ftclib.command.CommandBase

class ResetYawCommand(private val drive: Drive) : CommandBase() {
    init { addRequirements(drive) }

    override fun initialize() = drive.reset()

    override fun isFinished() = true
}