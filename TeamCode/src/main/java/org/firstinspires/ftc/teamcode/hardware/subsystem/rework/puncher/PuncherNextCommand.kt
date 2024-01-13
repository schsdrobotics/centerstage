package org.firstinspires.ftc.teamcode.hardware.subsystem.rework.puncher

import com.arcrobotics.ftclib.command.CommandBase

class PuncherNextCommand(private val puncher: Puncher) : CommandBase() {
    init { addRequirements(puncher) }

    override fun initialize() = puncher.next()

    override fun isFinished() = true
}