package org.firstinspires.ftc.teamcode.hardware.subsystem.rework.puncher.commands

import com.arcrobotics.ftclib.command.CommandBase
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.puncher.Puncher

class CyclePuncher(private val puncher: Puncher) : CommandBase() {
    init { addRequirements(puncher) }

    override fun initialize() = puncher.next()

    override fun isFinished() = true
}