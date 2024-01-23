package org.firstinspires.ftc.teamcode.hardware.subsystem.rework.spatula

import com.arcrobotics.ftclib.command.CommandBase
import kotlin.math.abs

class ToCommand(val state: Spatula.State, private val spatula: Spatula) : CommandBase() {
    init { addRequirements(spatula) }

    val shouldFinish get() = abs(spatula.angle - state.angle) <= 50

    override fun initialize() = spatula.to(state)

    override fun isFinished() = shouldFinish
}