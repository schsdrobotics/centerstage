package org.firstinspires.ftc.teamcode.hardware.subsystem.rework.puncher.commands

import com.arcrobotics.ftclib.command.CommandBase
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.puncher.Puncher

class DropOnePixel(private val puncher: Puncher) : CommandBase() {
    init { addRequirements(puncher) }

    override fun initialize() = puncher.to(Puncher.State.ONE)

    override fun isFinished() = true
}