package org.firstinspires.ftc.teamcode.hardware.subsystem.rework.launcher.commands

import com.arcrobotics.ftclib.command.CommandBase
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.launcher.Launcher

class LaunchCommand(private val launcher: Launcher) : CommandBase() {
    init { addRequirements(launcher) }

    override fun initialize() = launcher.launch()

    override fun isFinished() = true
}