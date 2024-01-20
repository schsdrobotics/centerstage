package org.firstinspires.ftc.teamcode.hardware.subsystem.rework.launcher

import com.arcrobotics.ftclib.command.CommandBase
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.drive.Drive

class LaunchCommand(private val launcher: Launcher) : CommandBase() {
    init { addRequirements(launcher) }

    override fun initialize() = launcher.launch()

    override fun isFinished() = true
}