package org.firstinspires.ftc.teamcode.hardware.subsystem.rework.spatula.commands

import com.arcrobotics.ftclib.command.CommandBase
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.spatula.Spatula
import kotlin.math.abs

class FlipSpatula(val state: Spatula.State, private val spatula: Spatula) : CommandBase() {
    init { addRequirements(spatula) }

    val shouldFinish get() = abs(spatula.angle - state.angle) <= 10

    override fun initialize() = spatula.to(state)

    override fun isFinished() = shouldFinish
}