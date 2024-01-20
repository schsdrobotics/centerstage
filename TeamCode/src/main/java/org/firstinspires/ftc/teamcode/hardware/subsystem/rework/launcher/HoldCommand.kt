package org.firstinspires.ftc.teamcode.hardware.subsystem.rework.launcher

import com.arcrobotics.ftclib.command.CommandBase

class HoldCommand(private val launcher: Launcher) : CommandBase() {
    init { addRequirements(launcher) }

    override fun initialize() = launcher.hold()

    override fun isFinished() = true
}