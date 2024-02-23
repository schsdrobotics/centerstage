package org.firstinspires.ftc.teamcode.hardware.subsystem.rework.launcher

import org.firstinspires.ftc.teamcode.hardware.Robot.LauncherHardware.Configuration.DOWN
import org.firstinspires.ftc.teamcode.hardware.Robot.LauncherHardware.Configuration.HOLD
import org.firstinspires.ftc.teamcode.hardware.Robot.LauncherHardware.Configuration.RELEASE
import org.firstinspires.ftc.teamcode.hardware.Robot.LauncherHardware.Configuration.UP
import org.firstinspires.ftc.teamcode.hardware.Robot.LauncherHardware.pitch
import org.firstinspires.ftc.teamcode.hardware.Robot.LauncherHardware.trigger
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.EfficientSubsystem

class Launcher : EfficientSubsystem() {
    fun launch() { trigger.position = RELEASE }
    fun hold() { trigger.position = HOLD }

    fun down() { pitch.position = DOWN }
    fun up() { pitch.position = UP }

    override fun periodic() { }
    override fun read() {}

    override fun write() {}

    override fun reset() {}
}