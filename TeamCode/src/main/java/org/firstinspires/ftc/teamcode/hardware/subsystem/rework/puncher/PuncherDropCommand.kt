package org.firstinspires.ftc.teamcode.hardware.subsystem.rework.puncher

import com.arcrobotics.ftclib.command.CommandBase

class PuncherDropCommand(private val puncher: Puncher) : CommandBase() {
    init { addRequirements(puncher) }

    override fun initialize() = puncher.to(Puncher.State.NONE)

    override fun isFinished() = true
}