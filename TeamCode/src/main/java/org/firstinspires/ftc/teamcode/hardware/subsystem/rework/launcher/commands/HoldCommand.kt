package org.firstinspires.ftc.teamcode.hardware.subsystem.rework.launcher.commands

import com.arcrobotics.ftclib.command.CommandBase
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.launcher.Launcher

class HoldCommand(private val launcher: Launcher) : CommandBase() {
    init { addRequirements(launcher) }

    override fun initialize() = launcher.hold()

    override fun isFinished() = true
}