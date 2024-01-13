package org.firstinspires.ftc.teamcode.hardware.subsystem.rework.spatula

import com.arcrobotics.ftclib.command.CommandBase

class ToCommand(val state: Spatula.State, private val spatula: Spatula) : CommandBase() {
    init { addRequirements(spatula) }

    override fun initialize() = spatula.to(state)

    override fun isFinished() = true
}