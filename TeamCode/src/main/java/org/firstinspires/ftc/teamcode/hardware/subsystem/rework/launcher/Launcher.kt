package org.firstinspires.ftc.teamcode.hardware.subsystem.rework.launcher

import org.firstinspires.ftc.teamcode.hardware.Robot.LauncherHardware.Configuration.HOLD
import org.firstinspires.ftc.teamcode.hardware.Robot.LauncherHardware.Configuration.RELEASE
import org.firstinspires.ftc.teamcode.hardware.Robot.LauncherHardware.servo
import org.firstinspires.ftc.teamcode.hardware.subsystem.rework.EfficientSubsystem

class Launcher : EfficientSubsystem() {
    fun launch() { servo.position = RELEASE }
    fun hold() { servo.position = HOLD }

    override fun periodic() { }
    override fun read() {}

    override fun write() {}

    override fun reset() {}
}