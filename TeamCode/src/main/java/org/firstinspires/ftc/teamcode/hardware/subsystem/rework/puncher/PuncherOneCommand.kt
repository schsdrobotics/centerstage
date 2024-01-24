package org.firstinspires.ftc.teamcode.hardware.subsystem.rework.puncher

import com.arcrobotics.ftclib.command.CommandBase

class PuncherOneCommand(private val puncher: Puncher) : CommandBase() {
    init { addRequirements(puncher) }

    override fun initialize() = puncher.to(Puncher.State.ONE)

    override fun isFinished() = true
}